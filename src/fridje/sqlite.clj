(ns fridje.sqlite
  (:require 
    [fridje.config :as config]
    [clojure.java.jdbc :as jdbc]
    [jdbc.pool.c3p0 :as c3p0]
    [honeysql.core :as hsql]))


(def db
  (c3p0/make-datasource-spec
    {:classname "org.sqlite.JDBC"
     :subprotocol "sqlite"
     :initial-pool-size 3
     :subname config/db-file}))

(defn query
  [query-map]
  (jdbc/query db (hsql/format query-map)))
