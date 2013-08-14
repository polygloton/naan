(ns naan.test.core-test
  (:use
    [naan.test.test-helper]
    [clojure.test]
    [korma.db :only [with-db]])
  (:require
    [naan.core :as naan]))

(def ^:dynamic *db* nil)
(def ^:dynamic *users* nil)

(use-fixtures :once
  (fn [all-tests]
    (binding [*db* (get-db)]
      (with-db *db*
        (create-tables!)
        (binding [*users* (get-users *db*)]
          (all-tests))
        (drop-tables!)))))

(use-fixtures :each
  (fn [individual-test]
    (individual-test)
    (truncate-tables!)))

(deftest create-test
  (testing "Creating and reading a User"
    (let [_ (naan/create *users* {:id 1, :first "Foo", :last "Bar"})
          user (naan/read *users* 1)]

      (is (= 1 (:id user)))

      (is (= "Foo" (:first user)))

      (is (= "Bar" (:last user))))))