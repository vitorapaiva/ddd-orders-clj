(ns orders.domain.value-objects.money
  "Value object: valor monetário. SRP: única responsabilidade de representar e validar dinheiro.")

(defn- validate
  [m]
  (when-not (and (some? (:amount m))
                 (>= (:amount m) 0))
    (throw (ex-info "Invalid money value" {:money m}))))

(defn money
  [amount]
  (let [m {:amount amount :currency "BRL"}]
    (validate m)
    m))
