(ns orders.domain.logic.order-test
  (:require [clojure.test :refer [deftest testing is]]
            [orders.domain.logic.order :as order-logic]))

(deftest validate-test
  (testing "valid order does not throw"
    (is (nil? (order-logic/validate {:customer-id "cust-1" :items [{:product-id "p1"}]}))))
  (testing "missing customer-id throws"
    (is (thrown-with-msg? clojure.lang.ExceptionInfo
                          #"customer-id is required"
                          (order-logic/validate {:items [{:product-id "p1"}]}))))
  (testing "nil customer-id throws"
    (is (thrown-with-msg? clojure.lang.ExceptionInfo
                          #"customer-id is required"
                          (order-logic/validate {:customer-id nil :items [{:product-id "p1"}]}))))
  (testing "empty items throws"
    (is (thrown-with-msg? clojure.lang.ExceptionInfo
                          #"at least one item"
                          (order-logic/validate {:customer-id "cust-1" :items []}))))
  (testing "nil items throws"
    (is (thrown-with-msg? clojure.lang.ExceptionInfo
                          #"at least one item"
                          (order-logic/validate {:customer-id "cust-1" :items nil})))))
