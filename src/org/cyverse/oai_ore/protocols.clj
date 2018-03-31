(ns org.cyverse.oai-ore.protocols)

(defprotocol RdfSerializable
  (to-rdf [] "Serializes the object as RDF/XML."))
