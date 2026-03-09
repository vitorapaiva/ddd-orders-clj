(ns orders.ports.inbound
  (:require [orders.domain.entities :as entities]
            [orders.domain.events :as events]
            [orders.ports.outbound :as outbound]))

(defn close-order
  [repository products-service event-publisher order-data]
  (try
    (let [order (entities/order order-data)]
      (outbound/save repository order)
      (let [items-to-reserve (entities/products-for-reservation order)
            reservation-result (outbound/reserve-products products-service (:id order) items-to-reserve)]
        
        (if (:success reservation-result)
          (let [updated-order (entities/update-status order :products-reserved)]
            (outbound/update-order repository updated-order)
            (outbound/publish event-publisher (events/order-created updated-order))
            {:success true :order updated-order})
          {:success false :error (:error reservation-result)})))
    (catch Exception e
      {:success false :error (.getMessage e)})))

(defn update-order-status
  [repository event-publisher order-id new-status]
  (try
    (let [order (outbound/find-by-id repository order-id)]
      (if order
        (let [previous-status (:status order)
              updated-order (entities/update-status order new-status)]
          (outbound/update-order repository updated-order)
          (outbound/publish event-publisher (events/order-updated updated-order previous-status))
          {:success true :order updated-order})
        {:success false :error "Order not found"}))
    (catch Exception e
      {:success false :error (.getMessage e)})))

(defn get-order
  [repository order-id]
  (let [order (outbound/find-by-id repository order-id)]
    (if order
      {:success true :order order}
      {:success false :error "Order not found"})))

(defn list-orders
  [repository]
  {:success true :orders (outbound/list-all repository)})
