(ns org.cyverse.oai-ore
  (:use [clojure.data.xml :only [alias-uri element]]))

(defprotocol RdfSerializable
  (to-rdf [_] "Serializes the object as RDF/XML."))

(def ^:private namespaces
  {:dc      "http://purl.org/dc/elements/1.1/"
   :dcterms "http://purl.org/dc/terms/"
   :foaf    "http://xmlns.com/foaf/0.1/"
   :ore     "http://www.openarchives.org/ore/terms/"
   :owl     "http://www.w3.org/2002/07/owl#"
   :rdf     "http://www.w3.org/1999/02/22-rdf-syntax-ns#"
   :rdfs    "http://www.w3.org/2000/01/rdf-schema#"})

(apply alias-uri (apply concat namespaces))

(defn- aggregates-element [file-uri]
  (element ::ore/aggregates {::rdf/resource file-uri}))

(deftype Aggregation [uri file-uris avus]
  RdfSerializable
  (to-rdf [_]
    (element ::rdf/Description {::rdf/about uri}
      (concat [(element ::rdf/type {::rdf/resource "http://www.openarchives.org/ore/terms/Aggregation"})]
              (mapv aggregates-element file-uris)))))

(deftype Archive [archive-uri aggregation-uri]
  RdfSerializable
  (to-rdf [_]
    (element ::rdf/Description {::rdf/about archive-uri}
      [(element ::rdf/type {::rdf/resource "http://www.openarchives.org/ore/terms/ResourceMap"})
       (element ::ore/describes {::rdf/resource aggregation-uri})])))

(deftype ArchivedFile [file-uri]
  RdfSerializable
  (to-rdf [_]
    (element ::rdf/Description {::rdf/about file-uri})))

(deftype Ore [descriptions]
  RdfSerializable
  (to-rdf [_]
    (element ::rdf/RDF (into {} (map (fn [[k v]] [(keyword "xmlns" (name k)) v]) namespaces))
      (mapv to-rdf descriptions))))

(defn build-ore [aggregation-uri archive-uri file-uris & [avus]]
  (Ore. (concat [(Aggregation. aggregation-uri file-uris avus)
                 (Archive. archive-uri aggregation-uri)]
                (mapv (fn [file-uri] (ArchivedFile. file-uri)) file-uris))))
