(ns orders.ports.inbound.close-order-test
  (:require [clojure.test :refer [deftest testing is]]
            [orders.ports.inbound.close-order :as close-order]
            [orders.ports.outbound :as outbound]
            [orders.test-helpers :as helpers]
            [orders.domain.value-objects.address :as address]
            [orders.domain.value-objects.item :as item]))

(def valid-address
  (address/address {:street-type "Rua" :street-name "X" :number "1" :complement nil
                    :district "Y" :city "Z" :state "SP" :zip-code "00000"}))

(def valid-order-data
  {:customer-id "cust-1"
   :shipping-address valid-address
   :billing-address valid-address
   :items [(item/item {:product-id "p1" :quantity 2 :price 10.0})]})

(deftest execute-test
  (testing "closes order successfully"
    (let [repo (helpers/create-in-memory-repository)
          {:keys [publisher get-events]} (helpers/create-mock-event-publisher)
          result (close-order/execute repo publisher valid-order-data)]
      (is (:success result))
      (is (some? (:order result)))
      (is (= :products-reserved (:status (:order result))))
      (is (= 1 (count (get-events))))))
  (testing "order is persisted"
    (let [repo (helpers/create-in-memory-repository)
          {:keys [publisher]} (helpers/create-mock-event-publisher)
          result (close-order/execute repo publisher valid-order-data)
          order-id (:id (:order result))
          found (outbound/find-by-id repo order-id)]
      (is (some? found))
      (is (= order-id (:id found))))))
