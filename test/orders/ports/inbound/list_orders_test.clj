(ns orders.ports.inbound.list-orders-test
  (:require [clojure.test :refer [deftest testing is]]
            [orders.ports.inbound.list-orders :as list-orders]
            [orders.ports.outbound :as outbound]
            [orders.test-helpers :as helpers]
            [orders.domain.entities.order :as order]
            [orders.domain.value-objects.address :as address]
            [orders.domain.value-objects.item :as item]))

(def valid-order
  (order/order {:customer-id "c"
                :shipping-address (address/address {:street-type "R" :street-name "X" :number "1"
                                                   :complement nil :district "Y" :city "Z"
                                                   :state "SP" :zip-code "00000"})
                :billing-address (address/address {:street-type "R" :street-name "X" :number "1"
                                                  :complement nil :district "Y" :city "Z"
                                                  :state "SP" :zip-code "00000"})
                :items [(item/item {:product-id "p1" :quantity 1 :price 10.0})]}))

(deftest execute-test
  (testing "returns empty list when no orders"
    (let [repo (helpers/create-in-memory-repository)
          result (list-orders/execute repo)]
      (is (:success result))
      (is (empty? (:orders result)))))
  (testing "returns orders when present"
    (let [repo (helpers/create-in-memory-repository)
          _ (outbound/save repo valid-order)
          result (list-orders/execute repo)]
      (is (:success result))
      (is (= 1 (count (:orders result))))
      (is (= (:id valid-order) (:id (first (:orders result))))))))
