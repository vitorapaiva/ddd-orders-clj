(ns orders.adapters.outbound.order-adapter
  (:require [cheshire.core :as json]))

(defn order->db
  [order]
  {:id (:id order)
   :customer_id (:customer-id order)
   :shipping_address (json/generate-string (:shipping-address order))
   :billing_address (json/generate-string (:billing-address order))
   :items (json/generate-string (:items order))
   :total (:total order)
   :status (name (:status order))
   :created_at (:created-at order)
   :updated_at (:updated-at order)})

(defn order->json
  [order]
  {:id (:id order)
   :customer-id (:customer-id order)
   :shipping-address (:shipping-address order)
   :billing-address (:billing-address order)
   :items (:items order)
   :total (:total order)
   :status (name (:status order))
   :created-at (str (:created-at order))
   :updated-at (str (:updated-at order))})

(defn orders->json
  [orders]
  (mapv order->json orders))
