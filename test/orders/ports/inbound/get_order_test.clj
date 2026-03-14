(ns orders.ports.inbound.get-order-test
  (:require [clojure.test :refer [deftest testing is]]
            [orders.ports.inbound.get-order :as get-order]
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
  (testing "returns order when found"
    (let [repo (helpers/create-in-memory-repository)
          _ (outbound/save repo valid-order)
          result (get-order/execute repo (:id valid-order))]
      (is (:success result))
      (is (= (:id valid-order) (:id (:order result))))))
  (testing "returns error when not found"
    (let [repo (helpers/create-in-memory-repository)
          result (get-order/execute repo "non-existent")]
      (is (not (:success result)))
      (is (= "Order not found" (:error result))))))
