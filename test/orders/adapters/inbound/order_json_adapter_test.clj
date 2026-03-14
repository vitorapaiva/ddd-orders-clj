(ns orders.adapters.inbound.order-json-adapter-test
  (:require [clojure.test :refer [deftest testing is]]
            [orders.adapters.inbound.order-json-adapter :as adapter]))

(def valid-json
  {:customer-id "cust-1"
   :shipping-address {:street-type "Rua" :street-name "X" :number "1" :complement nil
                     :district "Y" :city "Z" :state "SP" :zip-code "00000"}
   :billing-address {:street-type "Rua" :street-name "X" :number "1" :complement nil
                     :district "Y" :city "Z" :state "SP" :zip-code "00000"}
   :items [{:product-id "p1" :quantity 2 :price 10.0}]})

(deftest json->order-data-test
  (testing "converts JSON to domain format"
    (let [data (adapter/json->order-data valid-json)]
      (is (= "cust-1" (:customer-id data)))
      (is (map? (:shipping-address data)))
      (is (vector? (:items data)))
      (is (= 1 (count (:items data))))
      (is (= "p1" (get-in data [:items 0 :product-id])))))
  (testing "invalid address throws"
    (is (thrown? clojure.lang.ExceptionInfo
                 (adapter/json->order-data
                   (assoc-in valid-json [:shipping-address :street-type] nil))))))
