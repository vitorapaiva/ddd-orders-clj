(ns orders.domain.value-objects.address-test
  (:require [clojure.test :refer [deftest testing is]]
            [orders.domain.value-objects.address :as address]))

(def valid-address-data
  {:street-type "Rua"
   :street-name "das Flores"
   :number "123"
   :complement "Apto 45"
   :district "Centro"
   :city "São Paulo"
   :state "SP"
   :zip-code "01234-567"})

(deftest address-test
  (testing "creates valid address"
    (let [a (address/address valid-address-data)]
      (is (= "Rua" (:street-type a)))
      (is (= "das Flores" (:street-name a)))
      (is (= "São Paulo" (:city a)))))
  (testing "invalid address throws"
    (is (thrown? clojure.lang.ExceptionInfo
                 (address/address (dissoc valid-address-data :street-type))))))
