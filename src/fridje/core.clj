(ns fridje.core
  (:require [fridje.api :as api])
  (:gen-class))

(defn -main
  "Runs the message server"
  [& args]
  (api/run-server 8080))
