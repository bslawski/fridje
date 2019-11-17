(ns fridje.person
  (:require [crypto.password.bcrypt :as bcrypt]
            [fridje.config :as config]
            [fridje.sqlite :as sqlite]
            [fridje.util :as util]
            [honeysql.helpers :as hsql]))


(defn- save-person
  [{:keys [id] :as person}]
  (when-let [person-data (not-empty
                           (select-keys person [:title :password :facade]))]
    (when id
      (sqlite/execute
        (->
          (hsql/update :people)
          (hsql/sset person-data)
          (hsql/where [:= :id id]))))))


(defn load-default-facade
  []
  (util/read-file-binary
    (config/env-var :default-facade)))


(defn register
  [title password]
  (let [person {:id (util/random-alphanumeric 16)
                :title title
                :password (bcrypt/encrypt password)
                :facade (load-default-facade)}]
    (save-person person)
    (dissoc person :facade :password)))


(defn update-facade
  [id facade]
  (save-person
    {:id id
     :facade (or facade (load-default-facade))}))


(defn get-person
  [id]
  (first
    (sqlite/query
      (->
        (hsql/select [:id :title])
        (hsql/from :people)
        (hsql/where [:id id])))))


(defn get-facade
  [id]
  (:facade
    (first
      (sqlite/query
        (->
          (hsql/select [:facade])
          (hsql/from :people)
          (hsql/where [:id id]))))))


(defn check-login
  [title password]
  (when-let [person (first
                      (sqlite/query
                        (->
                          (hsql/select [:id :title :facade :password])
                          (hsql/from :people)
                          (hsql/where [:= :title title]))))]
    (when (bcrypt/check password (:password person))
      (dissoc person :password))))
