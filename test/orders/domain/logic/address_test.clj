(ns orders.domain.logic.address-test
  (:require [clojure.test :refer [deftest testing is]]
            [orders.domain.logic.address :as address-logic]))

(def valid-address
  {:street-type "Rua"
   :street-name "das Flores"
   :number "123"
   :complement "Apto 45"
   :district "Centro"
   :city "São Paulo"
   :state "SP"
   :zip-code "01234-567"})

(deftest validate-test
  (testing "valid address does not throw"
    (is (nil? (address-logic/validate valid-address))))
  (testing "missing street-type throws"
    (is (thrown-with-msg? clojure.lang.ExceptionInfo
                          #"required fields not filled"
                          (address-logic/validate (dissoc valid-address :street-type)))))
  (testing "nil city throws"
    (is (thrown-with-msg? clojure.lang.ExceptionInfo
                          #"required fields not filled"
                          (address-logic/validate (assoc valid-address :city nil))))))
