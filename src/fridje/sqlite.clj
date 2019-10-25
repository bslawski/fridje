(ns fridje.sqlite
  (:require
    [fridje.config :as config]
    [clojure.java.jdbc :as jdbc]
    [jdbc.pool.c3p0 :as c3p0]
    [honeysql.core :as hsql]))


(def db-conn (atom nil))

(defn db
  []
  (or
    @db-conn
    (let [conn (c3p0/make-datasource-spec
                 {:classname "org.sqlite.JDBC"
                  :subprotocol "sqlite"
                  :initial-pool-size 3
                  :subname (config/env-var :db-file)})]
      (reset! db-conn conn)
      conn)))


(defn query
  [query-map]
  (jdbc/query (db) (hsql/format query-map)))


(defn execute
  [query-map]
  (jdbc/execute! (db) (hsql/format query-map)))
