(ns org.cyverse.oai-ore-test
  (:require [clojure.data.xml :as xml]
            [clojure.java.io :as io]
            [clojure.test :refer :all]
            [org.cyverse.oai-ore :refer :all]))

(def ^:private agg-uri "http://foo.org")
(def ^:private arch-uri "http://foo.org/bar.xml")

;; Comparing generated XML to parsed XML requires us to serialize and parse the generated XML.
(defn- test-ore [ore filename]
  (is (= (xml/parse-str (xml/emit-str (to-rdf ore)))
         (xml/parse (io/reader (io/resource filename))))))

(deftest test-empty-ore
  (testing "Empty ORE files."
    (test-ore (build-ore agg-uri arch-uri []) "empty-ore.rdf")))
