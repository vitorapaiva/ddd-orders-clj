(ns orders.domain.logic.status-transitions-test
  (:require [clojure.test :refer [deftest testing is]]
            [orders.domain.logic.status-transitions :as st]))

(deftest valid-transition?-test
  (testing "allowed transitions from pending-payment"
    (is (st/valid-transition? :pending-payment :products-reserved))
    (is (st/valid-transition? :pending-payment :cancelled))
    (is (not (st/valid-transition? :pending-payment :delivered))))
  (testing "allowed transitions from products-reserved"
    (is (st/valid-transition? :products-reserved :payment-processed))
    (is (st/valid-transition? :products-reserved :cancelled)))
  (testing "delivered has no transitions"
    (is (not (st/valid-transition? :delivered :shipped)))
    (is (not (st/valid-transition? :delivered :cancelled))))
  (testing "cancelled has no transitions"
    (is (not (st/valid-transition? :cancelled :pending-payment))))
  (testing "unknown status returns false"
    (is (not (st/valid-transition? :unknown-status :pending-payment)))))

(deftest validate-transition!-test
  (testing "valid transition does not throw"
    (is (nil? (st/validate-transition! :pending-payment :products-reserved)))
    (is (nil? (st/validate-transition! :shipped :delivered))))
  (testing "invalid transition throws ex-info"
    (is (thrown-with-msg? clojure.lang.ExceptionInfo
                          #"Invalid status transition"
                          (st/validate-transition! :pending-payment :delivered)))
    (is (thrown-with-msg? clojure.lang.ExceptionInfo
                          #"Invalid status transition"
                          (st/validate-transition! :delivered :shipped)))))
