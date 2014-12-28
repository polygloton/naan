(ns naan.core
  (:refer-clojure :exclude [read])
  (:require
    [naan.utilities.time :as time-util]
    [korma.core :as korma]))


;; -----------------------------------------------------
;; -----------------------private-----------------------
;; -----------------------------------------------------

(def ^:dynamic *time-now* nil)
(def ^:dynamic *entity* nil)
(def ^:dynamic *string-key* :name)
(def ^:dynamic *number-key* :id)

(defn- bad-multimethod-key []
  (throw (IllegalArgumentException. "Invalid multimethod invocation")))

(defn- has-attr? [entity attr]
  (some #{attr} (::attributes entity)))

(defn has-field? [entity field]
  (some #{field} (map
                    #(keyword (last (clojure.string/split % #"\.")))
                    (:fields entity))))

(defn- updated-at [attributes]
  (if (some #{:updated_at} (::attributes *entity*))
    (assoc attributes :updated_at *time-now*)
    attributes))

(defn- created-at [attributes]
  (if (some #{:created_at} (::attributes *entity*))
    (assoc attributes :created_at *time-now*)
    attributes))

(def ^:private time-stamps (comp updated-at created-at))

(def ^:private present? (complement nil?))


;; -----------------------------------------------------
;; -----------------------helpers-----------------------
;; -----------------------------------------------------


(defn set-string-key [entity value]
  (assoc entity ::string-key (keyword value)))

(defn get-string-key [entity]
  (cond
    (some #{::string-key} (keys entity)) (::string-key entity)
    (has-attr? entity *string-key*) *string-key*
    (has-field? entity *string-key*) *string-key*
    :else (throw (IllegalArgumentException. (str "Can't find " (:name entity) " by string key " *string-key*)))))

(defn set-number-key [entity value]
  (assoc entity ::number-key (keyword value)))

(defn get-number-key [entity]
  (cond
    (some #{::number-key} (keys entity)) (::number-key entity)
    (has-attr? entity *number-key*) *number-key*
    (has-field? entity *number-key*) *number-key*
    :else (throw (IllegalArgumentException. (str "Can't find " (:name entity) " by number key " *number-key*)))))


;; --------------------------------------------------
;; -----------------------CRUD-----------------------
;; --------------------------------------------------

(defn create-raw [entity attributes]
  (korma/insert entity
    (korma/values attributes)))

(defn create [entity attributes]
  (binding [*time-now* (time-util/time-stamp-now)
            *entity* entity]
    (if (map? attributes)
      (create-raw *entity* (time-stamps attributes))
      (create-raw *entity* (map #(time-stamps %) attributes)))))

(defmulti read
  (fn [_ key]
    (cond
      (number? key) :number
      (string? key) :string
      (map? key) :map)))

(defmethod read :number [entity key]
  (let [number-key (get-number-key entity)]
    (first
      (korma/select entity
        (korma/where {number-key [= key]})
        (korma/limit 1)))))

(defmethod read :string [entity key]
  (let [string-key (get-string-key entity)]
    (first
      (korma/select entity
        (korma/where {string-key [= key]})
        (korma/limit 1)))))

(defmethod read :map [entity where-map]
  (first
    (korma/select entity
      (korma/where where-map)
      (korma/limit 1))))

(defmethod read :default [_ _]
  (bad-multimethod-key))

(defmulti update
  (fn [_ _ key]
    (cond
      (number? key) :number
      (string? key) :string
      (map? key) :map)))

(defmethod update :number [entity attributes key]
  (let [number-key (get-number-key entity)]
    (binding [*time-now* (time-util/time-stamp-now)
              *entity* entity]
      (korma/update *entity*
        (korma/set-fields (updated-at attributes))
        (korma/where {number-key [= key]})))))

(defmethod update :string [entity attributes key]
  (let [string-key (get-string-key entity)]
    (binding [*time-now* (time-util/time-stamp-now)
              *entity* entity]
      (korma/update *entity*
        (korma/set-fields (updated-at attributes))
        (korma/where {string-key [= key]})))))

(defmethod update :map [entity attributes where-map]
  (binding [*time-now* (time-util/time-stamp-now)
            *entity* entity]
    (korma/update *entity*
      (korma/set-fields (updated-at attributes))
      (korma/where where-map))))

(defmethod update :default [_ _ _]
  (bad-multimethod-key))

(defmulti destroy
  (fn [_ key]
    (cond
      (number? key) :number
      (string? key) :string
      (map? key) :map)))

(defmethod destroy :number [entity key]
  (let [number-key (get-number-key entity)]
    (korma/delete entity
      (korma/where {number-key [= key]}))))

(defmethod destroy :string [entity key]
  (let [string-key (get-string-key entity)]
    (korma/delete entity
      (korma/where {string-key [= key]}))))

(defmethod destroy :map [entity where-map]
  (korma/delete entity
    (korma/where where-map)))

(defmethod destroy :default [_ _]
  (bad-multimethod-key))


;; --------------------------------------------------
;; ------------------Search--------------------------
;; --------------------------------------------------

(defn find-all [entity]
  (korma/select entity))

(defn find-one [entity]
  (first (korma/select entity (korma/limit 1))))


;; --------------------------------------------------
;; ------------------Factories-----------------------
;; --------------------------------------------------

(defn build-factory [entity]
  (comp (partial read entity) :GENERATED_KEY (partial create entity)))
