(ns orders.adapters.inbound.order-adapter
  (:require [orders.domain.value-objects :as vo]
            [cheshire.core :as json]))

(defn json->order-data
  [data]
  {:customer-id (:customer-id data)
   :shipping-address (vo/address (:shipping-address data))
   :billing-address (vo/address (:billing-address data))
   :items (mapv vo/item (:items data))})

(defn db->order
  [row]
  (when row
    {:id (:id row)
     :customer-id (:customer_id row)
     :shipping-address (json/parse-string (:shipping_address row) true)
     :billing-address (json/parse-string (:billing_address row) true)
     :items (json/parse-string (:items row) true)
     :total (:total row)
     :status (keyword (:status row))
     :created-at (:created_at row)
     :updated-at (:updated_at row)}))
