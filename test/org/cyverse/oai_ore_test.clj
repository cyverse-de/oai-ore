(ns org.cyverse.oai-ore-test
  (:require [clojure.data.xml :as xml]
            [clojure.java.io :as io]
            [clojure.test :refer :all]
            [org.cyverse.oai-ore :refer :all]))

(def ^:private agg-uri "http://foo.org")
(def ^:private arch-uri "http://foo.org/bar.xml")
(def ^:private file-uris ["http://foo.org/bar1.txt" "http://foo.org/bar2.txt"])

;; Comparing generated XML to parsed XML requires us to serialize and parse the generated XML.
(defn- test-ore [ore filename]
  (is (= (xml/parse-str (xml/emit-str (to-rdf ore)))
         (xml/parse (io/reader (io/resource filename))))))

(deftest test-empty-ore
  (testing "Empty ORE."
    (test-ore (build-ore agg-uri arch-uri []) "empty-ore.rdf")))

(deftest test-ore-with-files
  (testing "ORE with files."
    (test-ore (build-ore agg-uri arch-uri file-uris) "ore-with-files.rdf")))
