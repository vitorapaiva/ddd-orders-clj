(ns orders.domain.value-objects.item
  "Value object: order item. SRP: single responsibility for representing item."
  (:require [orders.domain.logic.item :as logic]))

(defn item
  [{:keys [product-id quantity price]}]
  (let [i {:product-id product-id
           :quantity quantity
           :price price}]
    (logic/validate i)
    i))

(defn subtotal
  [item]
  (* (:quantity item) (:price item)))
