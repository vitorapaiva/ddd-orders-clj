(ns orders.domain.logic.address
  "Address validation logic.")

(def required-fields
  [:street-type :street-name :number :district :city :state :zip-code])

(defn validate
  [address]
  (when-not (every? #(some? (get address %)) required-fields)
    (throw (ex-info "Invalid address: required fields not filled" {:address address}))))
