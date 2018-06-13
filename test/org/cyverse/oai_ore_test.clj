(ns org.cyverse.oai-ore-test
  (:require [clojure.data.xml :as xml]
            [clojure.java.io :as io]
            [clojure.test :refer :all]
            [org.cyverse.oai-ore :refer :all]))

;; URIs to use when building test OREs.
(def ^:private agg-uri "http://foo.org")
(def ^:private arch-uri "http://foo.org/bar.xml")
(def ^:private archived-files
  [{:id  "73cd1d79-28c9-4a52-9060-cf9c65905922"
    :uri "http://foo.org/73cd1d79-28c9-4a52-9060-cf9c65905922"}
   {:id  "6772f358-9c10-4fa0-9fec-1376816b37e4"
    :uri "http://foo.org/6772f358-9c10-4fa0-9fec-1376816b37e4"}])

;; Comparing generated XML to parsed XML requires us to serialize and parse the generated XML.
(defn- test-ore [filename ore]
  (is (= (xml/parse (io/reader (io/resource filename)))
         (xml/parse-str (xml/emit-str (to-rdf ore))))))

(deftest test-empty-ore
  (testing "Empty ORE."
    (test-ore "empty-ore.rdf" (build-ore agg-uri arch-uri []))
    (test-ore "empty-ore.rdf" (build-ore agg-uri arch-uri [] [{:attr "datacite.title" :value ""}]))))

(deftest test-ore-with-files
  (testing "ORE with files."
    (test-ore "ore-with-files.rdf" (build-ore agg-uri arch-uri archived-files))
    (test-ore "ore-with-files.rdf" (build-ore agg-uri arch-uri archived-files [{:attr "datacite.title" :value ""}]))))

(deftest test-ore-with-title
  (testing "ORE with title."
    (test-ore "ore-with-title.rdf"
              (build-ore agg-uri arch-uri archived-files [{:attr "datacite.title" :value "The Title"}]))
    (test-ore "ore-with-title.rdf"
              (build-ore agg-uri arch-uri archived-files [{:attr "datacite.title" :value "The Title"}
                                                          {:attr "datacite.foo" :value "Bar"}]))
    (test-ore "ore-with-title.rdf"
              (build-ore agg-uri arch-uri archived-files [{:attr "datacite.title" :value "The Title"}
                                                          {:attr "datacite.publisher" :value ""}]))))

(deftest test-ore-with-publisher
  (testing "ORE with publisher."
    (test-ore "ore-with-publisher.rdf"
              (build-ore agg-uri arch-uri archived-files [{:attr "datacite.publisher" :value "The Publisher"}]))
    (test-ore "ore-with-publisher.rdf"
              (build-ore agg-uri arch-uri archived-files [{:attr "datacite.publisher" :value "The Publisher"}
                                                          {:attr "datacite.foo" :value "Bar"}]))
    (test-ore "ore-with-publisher.rdf"
              (build-ore agg-uri arch-uri archived-files [{:attr "datacite.publisher" :value "The Publisher"}
                                                          {:attr "datacite.title" :value ""}]))))

(deftest test-ore-with-creator
  (testing "ORE with creator."
    (test-ore "ore-with-creator.rdf"
              (build-ore agg-uri arch-uri archived-files [{:attr "datacite.creator" :value "The Creator"}]))
    (test-ore "ore-with-creator.rdf"
              (build-ore agg-uri arch-uri archived-files [{:attr "datacite.creator" :value "The Creator"}
                                                          {:attr "datacite.foo" :value "Bar"}]))
    (test-ore "ore-with-creator.rdf"
              (build-ore agg-uri arch-uri archived-files [{:attr "datacite.creator" :value "The Creator"}
                                                          {:attr "datacite.title" :value ""}]))))

(deftest test-ore-with-type
  (testing "ORE with type."
    (test-ore "ore-with-type.rdf"
              (build-ore agg-uri arch-uri archived-files [{:attr "datacite.resourcetype" :value "The Type"}]))
    (test-ore "ore-with-type.rdf"
              (build-ore agg-uri arch-uri archived-files [{:attr "datacite.resourcetype" :value "The Type"}
                                                          {:attr "datacite.foo" :value "Bar"}]))
    (test-ore "ore-with-type.rdf"
              (build-ore agg-uri arch-uri archived-files [{:attr "datacite.resourcetype" :value "The Type"}
                                                          {:attr "datacite.title" :value ""}]))))

(deftest test-ore-with-contributor
  (testing "ORE with contributor"
    (test-ore "ore-with-contributor.rdf"
              (build-ore agg-uri arch-uri archived-files [{:attr "contributorName" :value "The Contributor"}]))
    (test-ore "ore-with-contributor.rdf"
              (build-ore agg-uri arch-uri archived-files [{:attr "contributorName" :value "The Contributor"}
                                                          {:attr "datacite.foo" :value "Bar"}]))
    (test-ore "ore-with-contributor.rdf"
              (build-ore agg-uri arch-uri archived-files [{:attr "contributorName" :value "The Contributor"}
                                                          {:attr "datacite.title" :value ""}]))))

(deftest test-ore-with-subject
  (testing "ORE with subject."
    (test-ore "ore-with-subject.rdf"
              (build-ore agg-uri arch-uri archived-files [{:attr "Subject" :value "The Subject"}]))
    (test-ore "ore-with-subject.rdf"
              (build-ore agg-uri arch-uri archived-files [{:attr "Subject" :value "The Subject"}
                                                          {:attr "datacite.foo" :value "Bar"}]))
    (test-ore "ore-with-subject.rdf"
              (build-ore agg-uri arch-uri archived-files [{:attr "Subject" :value "The Subject"}
                                                          {:attr "datacite.title" :value ""}]))))

(deftest test-ore-with-rights
  (testing "ORE with rights."
    (test-ore "ore-with-rights.rdf"
              (build-ore agg-uri arch-uri archived-files [{:attr "Rights" :value "The Rights"}]))
    (test-ore "ore-with-rights.rdf"
              (build-ore agg-uri arch-uri archived-files [{:attr "Rights" :value "The Rights"}
                                                          {:attr "datacite.foo" :value "Bar"}]))
    (test-ore "ore-with-rights.rdf"
              (build-ore agg-uri arch-uri archived-files [{:attr "Rights" :value "The Rights"}
                                                          {:attr "datacite.title" :value ""}]))))

(deftest test-ore-with-description
  (testing "ORE with description."
    (test-ore "ore-with-description.rdf"
              (build-ore agg-uri arch-uri archived-files [{:attr "Description" :value "The Description"}]))
    (test-ore "ore-with-description.rdf"
              (build-ore agg-uri arch-uri archived-files [{:attr "Description" :value "The Description"}
                                                          {:attr "datacite.foo" :value "Bar"}]))
    (test-ore "ore-with-description.rdf"
              (build-ore agg-uri arch-uri archived-files [{:attr "Description" :value "The Description"}
                                                          {:attr "datacite.title" :value ""}]))))

(deftest test-ore-with-identifier
  (testing "ORE with identifier."
    (test-ore "ore-with-identifier.rdf"
              (build-ore agg-uri arch-uri archived-files [{:attr "Identifier" :value "The Identifier"}]))
    (test-ore "ore-with-identifier.rdf"
              (build-ore agg-uri arch-uri archived-files [{:attr "Identifier" :value "The Identifier"}
                                                          {:attr "datacite.foo" :value "Bar"}]))
    (test-ore "ore-with-identifier.rdf"
              (build-ore agg-uri arch-uri archived-files [{:attr "Identifier" :value "The Identifier"}
                                                          {:attr "datacite.title" :value ""}]))))

(deftest test-ore-with-box
  (testing "ORE with box."
    (test-ore "ore-with-box.rdf"
              (build-ore agg-uri arch-uri archived-files [{:attr "geoLocationBox" :value "The Box"}]))
    (test-ore "ore-with-box.rdf"
              (build-ore agg-uri arch-uri archived-files [{:attr "geoLocationBox" :value "The Box"}
                                                          {:attr "datacite.foo" :value "Bar"}]))
    (test-ore "ore-with-box.rdf"
              (build-ore agg-uri arch-uri archived-files [{:attr "geoLocationBox" :value "The Box"}
                                                          {:attr "datacite.title" :value ""}]))))

(deftest test-ore-with-location
  (testing "ORE with location."
    (test-ore "ore-with-location.rdf"
              (build-ore agg-uri arch-uri archived-files [{:attr "geoLocationPlace" :value "The Place"}]))
    (test-ore "ore-with-location.rdf"
              (build-ore agg-uri arch-uri archived-files [{:attr "geoLocationPlace" :value "The Place"}
                                                          {:attr "datacite.foo" :value "Bar"}]))
    (test-ore "ore-with-location.rdf"
              (build-ore agg-uri arch-uri archived-files [{:attr "geoLocationPlace" :value "The Place"}
                                                          {:attr "datacite.title" :value ""}]))))

(deftest test-ore-with-point
  (testing "ORE with point."
    (test-ore "ore-with-point.rdf"
              (build-ore agg-uri arch-uri archived-files [{:attr "geoLocationPoint" :value "The Point"}]))
    (test-ore "ore-with-point.rdf"
              (build-ore agg-uri arch-uri archived-files [{:attr "geoLocationPoint" :value "The Point"}
                                                          {:attr "datacite.foo" :value "Bar"}]))
    (test-ore "ore-with-point.rdf"
              (build-ore agg-uri arch-uri archived-files [{:attr "geoLocationPoint" :value "The Point"}
                                                          {:attr "datacite.title" :value ""}]))))
