(ns orders.ports.inbound.update-order-status-test
  (:require [clojure.test :refer [deftest testing is]]
            [orders.ports.inbound.update-order-status :as update-status]
            [orders.ports.outbound :as outbound]
            [orders.test-helpers :as helpers]
            [orders.domain.entities.order :as order]
            [orders.domain.value-objects.address :as address]
            [orders.domain.value-objects.item :as item]))

(def valid-address
  (address/address {:street-type "Rua" :street-name "X" :number "1" :complement nil
                    :district "Y" :city "Z" :state "SP" :zip-code "00000"}))

(def valid-order
  (order/order {:customer-id "c"
                :shipping-address valid-address
                :billing-address valid-address
                :items [(item/item {:product-id "p1" :quantity 1 :price 10.0})]}))

(deftest execute-test
  (testing "updates status when order exists"
    (let [repo (helpers/create-in-memory-repository)
          order-in-progress (assoc valid-order :status :products-reserved)
          _ (outbound/save repo order-in-progress)
          order-id (:id order-in-progress)
          {:keys [publisher]} (helpers/create-mock-event-publisher)
          result (update-status/execute repo publisher order-id :payment-processed)]
      (is (:success result))
      (is (= :payment-processed (:status (:order result))))))
  (testing "returns error when order not found"
    (let [repo (helpers/create-in-memory-repository)
          {:keys [publisher]} (helpers/create-mock-event-publisher)
          result (update-status/execute repo publisher "non-existent" :payment-processed)]
      (is (not (:success result)))
      (is (= "Order not found" (:error result)))))
  (testing "invalid transition throws"
    (let [repo (helpers/create-in-memory-repository)
          _ (outbound/save repo valid-order)
          order-id (:id valid-order)
          {:keys [publisher]} (helpers/create-mock-event-publisher)]
      (is (thrown? clojure.lang.ExceptionInfo
                   (update-status/execute repo publisher order-id :delivered))))))
