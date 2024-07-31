(defproject org.cyverse/oai-ore "1.0.4-SNAPSHOT"
  :description "Library for generating OAI-ORE files."
  :url "https://github.com/cyverse-de/oai-ore"
  :license {:name "BSD"
            :url "http://www.cyverse.org/license"}
  :deploy-repositories [["releases" :clojars]
                        ["snapshots" :clojars]]
  :plugins [[jonase/eastwood "1.4.3"]
            [lein-ancient "0.7.0"]
            [test2junit "1.4.4"]]
  :dependencies [[org.clojure/clojure "1.11.3"]
                 [org.clojure/data.xml "0.2.0-alpha9"]]
  :profiles {:dev {:resource-paths ["test-resources"]}})
