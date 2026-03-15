(ns orders.domain.value-objects.address-test
  (:require [clojure.test :refer [deftest testing is]]
            [orders.domain.value-objects.address :as address]))

(def valid-address-data
  {:street-type "Street"
   :street-name "Main"
   :number "123"
   :complement "Apt 45"
   :district "Center"
   :city "New York"
   :state "NY"
   :zip-code "01234-567"})

(deftest address-test
  (testing "creates valid address"
    (let [a (address/address valid-address-data)]
      (is (= "Street" (:street-type a)))
      (is (= "Main" (:street-name a)))
      (is (= "New York" (:city a)))))
  (testing "invalid address throws"
    (is (thrown? clojure.lang.ExceptionInfo
                 (address/address (dissoc valid-address-data :street-type))))))
