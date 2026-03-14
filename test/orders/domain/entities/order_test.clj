(ns orders.domain.entities.order-test
  (:require [clojure.test :refer [deftest testing is]]
            [orders.domain.entities.order :as order]
            [orders.domain.value-objects.item :as item-vo]
            [orders.domain.value-objects.address :as address-vo]))

(def valid-address
  (address-vo/address {:street-type "Rua" :street-name "X" :number "1" :complement nil
                      :district "Y" :city "Z" :state "SP" :zip-code "00000"}))

(def valid-items
  [(item-vo/item {:product-id "p1" :quantity 2 :price 10.0})])

(deftest order-test
  (testing "creates order with correct structure"
    (let [o (order/order {:customer-id "cust-1"
                         :shipping-address valid-address
                         :billing-address valid-address
                         :items valid-items})]
      (is (some? (:id o)))
      (is (= "cust-1" (:customer-id o)))
      (is (= :pending-payment (:status o)))
      (is (= 20.0 (:total o)))
      (is (some? (:created-at o)))
      (is (some? (:updated-at o)))))
  (testing "invalid order throws"
    (is (thrown? clojure.lang.ExceptionInfo
                 (order/order {:customer-id nil :shipping-address valid-address
                              :billing-address valid-address :items valid-items})))))

(deftest update-status-test
  (testing "valid transition updates status"
    (let [o (order/order {:customer-id "c" :shipping-address valid-address
                          :billing-address valid-address :items valid-items})
          updated (order/update-status o :products-reserved)]
      (is (= :products-reserved (:status updated)))
      (is (some? (:updated-at updated)))))
  (testing "invalid transition throws"
    (let [o (order/order {:customer-id "c" :shipping-address valid-address
                          :billing-address valid-address :items valid-items})]
      (is (thrown-with-msg? clojure.lang.ExceptionInfo
                            #"Invalid status transition"
                            (order/update-status o :delivered))))))
