(defproject naan "0.1.0-SNAPSHOT"
  :description "naan goes well with korma"
  :url "https://github.com/polygloton/naan"
  :license {:name "MIT"
            :url "http://opensource.org/licenses/MIT"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [clj-time "0.5.1"]
                 [korma "0.3.0-RC5"]]
  :profiles {:dev {:dependencies [[com.h2database/h2 "1.3.172"]]}})
