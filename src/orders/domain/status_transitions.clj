(ns orders.domain.status-transitions)

(def allowed-transitions
  {:pending-payment #{:products-reserved :cancelled}
   :products-reserved #{:payment-processed :cancelled}
   :payment-processed #{:products-picked :cancelled}
   :products-picked #{:shipped :cancelled}
   :shipped #{:delivered}
   :delivered #{}
   :cancelled #{}})

(defn valid-transition?
  [current-status new-status]
  (contains? (get allowed-transitions current-status #{}) new-status))
