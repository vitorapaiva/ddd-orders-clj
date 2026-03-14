(ns orders.domain.logic.status-transitions
  "Order status transition logic.")

(def allowed-transitions
  {:pending-payment #{:products-reserved :cancelled}
   :products-reserved #{:payment-processed :cancelled}
   :payment-processed #{:products-picked :cancelled}
   :products-picked #{:shipped :cancelled}
   :shipped #{:delivered}
   :delivered #{}
   :cancelled #{}})

(defn valid-transition?
  "Returns true if new-status is an allowed transition from current-status."
  [current-status new-status]
  (contains? (get allowed-transitions current-status #{}) new-status))

(defn validate-transition!
  "Validates the transition. Throws ex-info if invalid."
  [current-status new-status]
  (when-not (valid-transition? current-status new-status)
    (throw (ex-info "Invalid status transition"
                   {:current-status current-status
                    :new-status new-status}))))
