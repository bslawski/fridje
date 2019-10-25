(defproject fridje "0.0.0"
  :description "An easy-to-install message server"
  :license {:name "MIT Public License"
            :url "https://opensource.org/licenses/MIT"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [clojure.jdbc/clojure.jdbc-c3p0 "0.3.3"]
                 [honeysql "0.9.8"]
                 [org.clojure/core.async "0.4.500"]
                 [org.clojure/java.jdbc "0.7.8"]
                 [org.xerial/sqlite-jdbc "3.23.1"]
                 [simple-time "0.2.0"]]
  :main fridje.core
  :aot :all
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
