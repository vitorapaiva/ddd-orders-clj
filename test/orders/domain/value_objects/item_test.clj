(ns orders.domain.value-objects.item-test
  (:require [clojure.test :refer [deftest testing is]]
            [orders.domain.value-objects.item :as item]))

(deftest item-test
  (testing "creates valid item"
    (let [i (item/item {:product-id "p1" :quantity 2 :price 10.0})]
      (is (= "p1" (:product-id i)))
      (is (= 2 (:quantity i)))
      (is (= 10.0 (:price i)))))
  (testing "invalid item throws"
    (is (thrown? clojure.lang.ExceptionInfo
                 (item/item {:product-id "p1" :quantity 0 :price 10.0})))))

(deftest subtotal-test
  (testing "calculates subtotal correctly"
    (is (= 20.0 (item/subtotal {:product-id "p1" :quantity 2 :price 10.0})))
    (is (= 29.9 (item/subtotal {:quantity 1 :price 29.9})))))
