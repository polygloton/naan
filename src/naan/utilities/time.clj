(ns naan.utilities.time
  (:require
    [clj-time.core :as time]
    [clj-time.coerce :as time-coerce]))

(defn time-stamp-now []
  (time-coerce/to-timestamp (time/now)))

(defn time-stamp-at [year month date hour minute sec mili]
  (time-coerce/to-timestamp (time/date-time year month date hour minute sec mili)))