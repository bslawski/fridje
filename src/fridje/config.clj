(ns fridje.config
  (:require [clojure.java.io :as io]))


(defmulti env-var
  (fn [var-name]
    (keyword var-name)))

(defmethod env-var :db-file
  [_]
  (or
    (System/getenv "DB_FILE")
    "fridje.db"))

(defmethod env-var :default-facade
  [_]
  (or
    (System/getenv "DEFAULT_FACADE")
    (io/resource "default_facade.jpg")))
