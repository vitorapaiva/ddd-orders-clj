(ns orders.domain.entities
  (:require [orders.domain.value-objects :as vo]
            [clj-uuid :as uuid]))

(def valid-statuses
  #{:pending-payment
    :products-reserved
    :payment-processed
    :products-picked
    :shipped
    :delivered
    :cancelled})

(def allowed-transitions
  {:pending-payment #{:products-reserved :cancelled}
   :products-reserved #{:payment-processed :cancelled}
   :payment-processed #{:products-picked :cancelled}
   :products-picked #{:shipped :cancelled}
   :shipped #{:delivered}
   :delivered #{}
   :cancelled #{}})

(defn- valid-transition?
  [current-status new-status]
  (contains? (get allowed-transitions current-status #{}) new-status))

(defn- validate-order
  [order]
  (when-not (some? (:customer-id order))
    (throw (ex-info "Invalid order: customer-id is required" {:order order})))
  (when-not (seq (:items order))
    (throw (ex-info "Invalid order: must contain at least one item" {:order order}))))

(defn- calculate-total
  [items]
  (reduce + 0 (map vo/subtotal items)))

(defn order
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
    (validate-order o)
    o))

(defn update-status
  [order new-status]
  (if (valid-transition? (:status order) new-status)
    (assoc order
           :status new-status
           :updated-at (java.time.Instant/now))
    (throw (ex-info "Invalid status transition"
                    {:current-status (:status order)
                     :new-status new-status}))))

(defn products-for-reservation
  [order]
  (map (fn [item]
         {:product-id (:product-id item)
          :quantity (:quantity item)})
       (:items order)))
