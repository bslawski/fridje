(ns fridje.message
  (:require [clojure.core.async :as async]
            [fridje.sqlite :as sqlite]
            [fridje.util :as util]
            [honeysql.helpers :as hsql]
            [simple-time.core :as stime]))


(def channels
  (atom {}))

(defn- build-channel
  []
  {:channel (async/chan)
   :last_active (stime/now)})

(defn id-for-channel
  [sender-id recipient-id]
  (str sender-id ">" recipient-id))

(defn get-channel
  [sender-id recipient-id]
  (let [channel-id (id-for-channel sender-id recipient-id)]
    (or
      (:channel (get @channels channel-id))
      (do
        (swap! channels assoc channel-id (build-channel))
        (:channel (get @channels channel-id))))))

(defn channel-active
  [sender-id recipient-id]
  (let [channel-id (id-for-channel sender-id recipient-id)]
    (when-let [channel-map (get @channels channel-id)]
      (swap!
        channels
        assoc
        channel-id
        (assoc channel-map :last_active (stime/now))))))

(defn cleanup-channels
  [max-age]
  (doseq [[channel-id {:keys [last_active channel]}] @channels]
    (when (stime/>
            (stime/- (stime/now) last_active)
            (stime/seconds->timespan max-age))
      (swap! channels dissoc channel-id)
      (async/close! channel))))


(defn- log-message-sent
  [message]
  (sqlite/execute
    (->
      (hsql/insert-into :messages)
      (hsql/values [message]))))

(defn send-message
  [sender recipient payload]
  (let [message {:id (util/random-alphanumeric 16)
                 :sender sender
                 :recipient recipient
                 :payload payload
                 :sent_at (util/current-timestamp)}]
    (log-message-sent message)
    (async/put!
      (get-channel sender recipient)
      message)))


(defn- log-messages-received
  [messages]
  (sqlite/execute
    (->
      (hsql/update :messages)
      (hsql/sset {:received_at (util/current-timestamp)})
      (hsql/where [:in :id (map :id messages)]))))

(defn wait-for-message
  [sender recipient]
  (let [message (async/<!!
                  (get-channel sender recipient))]
    (channel-active sender recipient)
    (log-messages-received [message])
    message))

(defn load-historic-messages
  [sender recipient message-count offset]
  (let [messages (sqlite/query
                   (->
                     (hsql/select :*)
                     (hsql/from :messages)
                     (hsql/where [:= :sender sender])
                     (hsql/merge-where [:= :recipient recipient])
                     (hsql/order-by [:sent_at :desc])
                     (hsql/limit message-count)
                     (hsql/offset offset)))]
    (log-messages-received messages)
    messages))
