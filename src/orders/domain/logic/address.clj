(ns orders.domain.logic.address
  "Address validation logic.")

(defn validate
  [address]
  (when-not (and (some? (:street-type address))
                 (some? (:street-name address))
                 (some? (:number address))
                 (some? (:district address))
                 (some? (:city address))
                 (some? (:state address))
                 (some? (:zip-code address)))
    (throw (ex-info "Invalid address: required fields not filled" {:address address}))))
