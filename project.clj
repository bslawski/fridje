(defproject fridje "0.0.0"
  :description "An easy-to-install message server"
  :license {:name "MIT Public License"
            :url "https://opensource.org/licenses/MIT"}
  :dependencies [[org.clojure/clojure "1.8.0"]]
  :main ^:skip-aot fridje.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
