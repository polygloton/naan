(ns naan.korma-helpers
  (:require
    [naan.core :as core]
    [korma.core :as korma]
    [korma.config :as korma-config]))

(defn fix-korma-entity-options []
  (korma-config/set-delimiters nil nil))

(defn entity-fields-from-attributes [entity]
  (apply (partial korma/entity-fields entity) (::core/attributes entity)))

(defn attributes [entity & attrs]
  (assoc entity ::core/attributes attrs))