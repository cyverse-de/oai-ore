(ns org.cyverse.oai-ore
  (:use [clojure.data.xml :only [alias-uri element]]))

(def ^:private namespaces
  {:dc      "http://purl.org/dc/elements/1.1/"
   :dcterms "http://purl.org/dc/terms/"
   :foaf    "http://xmlns.com/foaf/0.1/"
   :ore     "http://www.openarchives.org/ore/terms/"
   :owl     "http://www.w3.org/2002/07/owl#"
   :rdf     "http://www.w3.org/1999/02/22-rdf-syntax-ns#"
   :rdfs    "http://www.w3.org/2000/01/rdf-schema#"})

(apply alias-uri (apply concat namespaces))

(deftype Aggregation [uri file-uris avus]
  RdfSerializable
  (to-rdf []
    (element ::rdf/Description {::rdf/about uri}
      (concat [(element ::rdf/type {::rdf/resource "http://www.openarchives.org/ore/terms/Aggregation"})]
              (mapv aggregates-element file-uris)))))

(defn- aggregates-element [file-url]
  (element ::ore/aggregates {::rdf/resource file-url}))

(defn- aggregation-description [aggregation-url file-urls]
  (element ::rdf/Description {::rdf/about aggregation-url}
    (concat [(element ::rdf/type {::rdf/resource "http://www.openarchives.org/ore/terms/Aggregation"})]
            (mapv aggregates-element file-urls))))

(defn- archive-description [archive-url aggregation-url]
  (element ::rdf/Description {::rdf/about archive-url}
    [(element ::rdf/type {::rdf/resource "http://www.openarchives.org/ore/terms/ResourceMap"})
     (element ::ore/describes {::rdf/resource aggregation-url})]))

(defn- file-description [file-url]
  (element ::rdf/Description {::rdf/about file-url}))

(defn generate-ore [aggregation-url archive-url file-urls]
  (element ::rdf/RDF (into {} (map (fn [[k v]] [(keyword "xmlns" (name k)) v]) namespaces))
    (concat [(aggregation-description aggregation-url file-urls)
             (archive-description archive-url aggregation-url)]
            (mapv file-description file-urls))))

(defn aggregation [aggregation-url file-urls])
