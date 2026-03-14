(ns orders.domain.entities.order
  (:require [orders.domain.value-objects.item :as item-vo]
            [orders.domain.logic.order :as order-logic]
            [orders.domain.logic.status-transitions :as st]
            [clj-uuid :as uuid]))

(defn- calculate-total
  [items]
  (reduce + 0 (map item-vo/subtotal items)))

(defn order
  "Creates a new order with status :pending-payment. Validates customer-id and items."
  [{:keys [customer-id shipping-address billing-address items]}]
  (let [o {:id (str (uuid/v4))
           :customer-id customer-id
           :shipping-address shipping-address
           :billing-address billing-address
           :items items
           :total (calculate-total items)
           :status :pending-payment
           :created-at (java.time.Instant/now)
           :updated-at (java.time.Instant/now)}]
    (order-logic/validate o)
    o))

(defn update-status
  "Updates order status if transition is valid. Throws exception otherwise."
  [order new-status]
  (st/validate-transition! (:status order) new-status)
  (assoc order
         :status new-status
         :updated-at (java.time.Instant/now)))
