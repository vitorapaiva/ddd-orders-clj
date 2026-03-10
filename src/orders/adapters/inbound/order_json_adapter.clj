(ns orders.adapters.inbound.order-json-adapter
  "Adapta dados JSON do corpo da requisição HTTP para o formato de domínio."
  (:require [orders.domain.value-objects.address :as address]
            [orders.domain.value-objects.item :as item]))

(defn json->order-data
  [data]
  {:customer-id (:customer-id data)
   :shipping-address (address/address (:shipping-address data))
   :billing-address (address/address (:billing-address data))
   :items (mapv item/item (:items data))})
