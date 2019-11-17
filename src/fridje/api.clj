(ns fridje.api
  (:use [compojure.core])
  (:require [compojure.handler :as handler]
            [fridje.message :as message]
            [fridje.person :as person]
            [ring.adapter.jetty :as jetty]
            [ring.middleware.json :as ring-middleware]))


(defmacro wrap-get-response
  [{:keys [params]} required-params body]
  `(if (every? #(get ~params %) ~required-params)
     (try
       {:status 200
        :body ~@body}
     (catch Exception e#
       {:status 500
        ;; TODO better 500 error messages
        :body (str (type e#) (.getMessage e#))}))
     {:status 400
      :body (str
              "Request missing required params: "
              (clojure.string/join
                ", "
                (map
                  name
                  (remove #(get ~params %) ~required-params))))}))

(defmacro wrap-post-response
  [{:keys []} required-params body]
  ;; TODO
)


(defroutes endpoints

  (context ["/user"] []

    (POST "/signup" {:keys [title password] :as request}
      (wrap-post-response
        request
        [:title :password]
        (person/register title password)))

    (POST "/facade" {:keys [id facade] :as request}
      (wrap-post-response
        request
        [:id :facade]
        (person/update-facade id facade)))

    (GET "/facade" request
      (wrap-get-response
        request
        [:id]
        (person/get-facade id)))

;;    (POST "/login" request
;;      ;; TODO
;;    )
  )

;;  (context "/message"
;;
;;    ;; TODO live message websocket
;;
;;    (GET "/history" request
;;      ;; TODO
;;    )
;;
;;    (POST "/send" request
;;      ;; TODO
;;    )
;;  )
)


(defn run-server
  [port]
  (let [app (ring-middleware/wrap-json-response
              (handler/site endpoints))] 
    (jetty/run-jetty
      app
      {:port port
       :ssl? false}))) ;; TODO SSL
