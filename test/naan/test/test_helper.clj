(ns naan.test.test-helper
  (:use
    [korma.db :only [h2 create-db]]
    [korma.core :only [create-entity exec-raw pk database]]
    [naan.utilities.korma-helpers :only [attributes entity-fields-from-attributes]]
    [clojure.string :only [lower-case upper-case]]))

(defn create-tables! []
  (exec-raw ["CREATE TABLE USERS(id INT PRIMARY KEY, FIRST VARCHAR(255), LAST VARCHAR(255));"]))

(defn drop-tables! []
  (exec-raw ["DROP TABLE IF EXISTS USERS;"]))

(defn truncate-tables! []
  (let [tables (exec-raw ["SHOW TABLES;"] :results)]
    (doseq [table tables]
      (let [table-name (:TABLE_NAME table)]
        (exec-raw [(str "DELETE FROM " table-name ";")])))))

(defn get-db []
  (create-db (h2 {:db "mem:naan_test"
                  :naming {:keys lower-case
                           :fields upper-case}})))

(defn get-users [db]
  (-> (create-entity "users")
    (attributes :id :first :last)
    (entity-fields-from-attributes)
    (pk :id)
    (database db)))