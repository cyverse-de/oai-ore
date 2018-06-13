(defproject org.cyverse/oai-ore "1.0.2"
  :description "Library for generating OAI-ORE files."
  :url "https://github.com/cyverse-de/oai-ore"
  :license {:name "BSD"
            :url "http://www.cyverse.org/license"}
  :deploy-repositories [["releases" :clojars]
                        ["snapshots" :clojars]]
  :plugins [[test2junit "1.2.2"]]
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/data.xml "0.2.0-alpha5"]]
  :profiles {:dev {:resource-paths ["test-resources"]}})
