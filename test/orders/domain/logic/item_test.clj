(ns orders.domain.logic.item-test
  (:require [clojure.test :refer [deftest testing is]]
            [orders.domain.logic.item :as item-logic]))

(deftest validate-test
  (testing "valid item does not throw"
    (is (nil? (item-logic/validate {:product-id "p1" :quantity 2 :price 10.0}))))
  (testing "missing product-id throws"
    (is (thrown-with-msg? clojure.lang.ExceptionInfo
                          #"product-id is required"
                          (item-logic/validate {:quantity 1 :price 10.0}))))
  (testing "zero quantity throws"
    (is (thrown-with-msg? clojure.lang.ExceptionInfo
                          #"quantity must be greater than zero"
                          (item-logic/validate {:product-id "p1" :quantity 0 :price 10.0}))))
  (testing "negative quantity throws"
    (is (thrown-with-msg? clojure.lang.ExceptionInfo
                          #"quantity must be greater than zero"
                          (item-logic/validate {:product-id "p1" :quantity -1 :price 10.0}))))
  (testing "zero price throws"
    (is (thrown-with-msg? clojure.lang.ExceptionInfo
                          #"price must be greater than zero"
                          (item-logic/validate {:product-id "p1" :quantity 1 :price 0})))))
