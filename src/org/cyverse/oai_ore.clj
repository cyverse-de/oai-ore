(ns org.cyverse.oai-ore
  (:use [clojure.data.xml :only [alias-uri element]])
  (:require [clojure.string :as string]))

(defprotocol RdfSerializable
  (to-rdf [_] "Serializes the object as RDF/XML. This function returns an instance of clojure.data.xml.Element."))

;; These are the namespaces that may be used in the the RDF serialization of the archive. The key is the abbreviation
;; as it should appear in the serialized XML. The value is the namespace URI.
(def ^:private namespaces
  {:dc      "http://purl.org/dc/elements/1.1/"
   :dcterms "http://purl.org/dc/terms/"
   :foaf    "http://xmlns.com/foaf/0.1/"
   :ore     "http://www.openarchives.org/ore/terms/"
   :owl     "http://www.w3.org/2002/07/owl#"
   :rdf     "http://www.w3.org/1999/02/22-rdf-syntax-ns#"
   :rdfs    "http://www.w3.org/2000/01/rdf-schema#"})

;; Alias the namespaces so that they can easily be used when generating the XML.
(apply alias-uri (apply concat namespaces))

(defn- default-attribute-formatter
  "Returns a default attribute formatter for the given XML tag."
  [tag]
  (fn [content] (element tag {} content)))

(defn- default-dcterm-attribute-formatter
  "Returns a default formatter for dcterm attributes."
  [tag parse-type]
  (fn [content] (element tag {::rdf/parseType parse-type} content)))

;; A table that associates datacite terms with attribute names.
(def ^:private attribute-formatter-for
  {"datacite.title"        (default-attribute-formatter ::dc/title)
   "datacite.publisher"    (default-attribute-formatter ::dc/publisher)
   "datacite.creator"      (default-attribute-formatter ::dc/creator)
   "datacite.resourcetype" (default-attribute-formatter ::dc/type)
   "contributorName"       (default-attribute-formatter ::dc/contributor)
   "Subject"               (default-attribute-formatter ::dc/subject)
   "Rights"                (default-attribute-formatter ::dc/rights)
   "Description"           (default-attribute-formatter ::dc/description)
   "Identifier"            (default-attribute-formatter ::dcterms/identifier)
   "geoLocationBox"        (default-dcterm-attribute-formatter ::dcterms/Box "Literal")
   "geoLocationPlace"      (default-dcterm-attribute-formatter ::dcterms/Location "Literal")
   "geoLocationPoint"      (default-dcterm-attribute-formatter ::dcterms/Point "Literal")})

(defn- aggregates-element
  "Gnereates an RDF/XML element indicating that a file is contained within an aggregation."
  [file-uri]
  (element ::ore/aggregates {::rdf/resource file-uri}))

(defn- element-for
  "Generates an RDF/XML element for an AVU."
  [{:keys [attr value]}]
  (when-not (string/blank? value)
    (when-let [formatter (attribute-formatter-for attr)]
      (formatter (string/trim value)))))

(deftype Aggregation [uri file-uris avus]
  RdfSerializable
  (to-rdf [_]
    (element ::rdf/Description {::rdf/about uri}
      (concat [(element ::rdf/type {::rdf/resource "http://www.openarchives.org/ore/terms/Aggregation"})]
              (mapv aggregates-element file-uris)
              (doall (remove nil? (map element-for avus)))))))

(deftype Archive [id uri aggregation-uri]
  RdfSerializable
  (to-rdf [_]
    (element ::rdf/Description {::rdf/about uri}
      [(element ::dcterms/identifier {} id)
       (element ::rdf/type {::rdf/resource "http://www.openarchives.org/ore/terms/ResourceMap"})
       (element ::ore/describes {::rdf/resource aggregation-uri})])))

(deftype ArchivedFile [id file-uri]
  RdfSerializable
  (to-rdf [_]
    (element ::rdf/Description {::rdf/about file-uri}
      [(element ::dcterms/identifier {} id)])))

(deftype Ore [descriptions]
  RdfSerializable
  (to-rdf [_]
    (element ::rdf/RDF (into {} (map (fn [[k v]] [(keyword "xmlns" (name k)) v]) namespaces))
      (mapv to-rdf descriptions))))

(defn build-ore
  "Generates an ORE archive for a data set."
  [aggregation-uri archive archived-files & [avus metadata]]
  (Ore. (concat [(Aggregation. aggregation-uri (mapv :uri archived-files) avus)
                 (Archive. (:id archive) (:uri archive) aggregation-uri)]
                (mapv (fn [{:keys [id uri]}] (ArchivedFile. id uri)) archived-files))))

(def format-id "http://www.openarchives.org/ore/terms")
