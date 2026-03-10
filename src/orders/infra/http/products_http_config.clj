(ns orders.infra.http.products-http-config)

(def default-options
  {:content-type :json
   :accept :json
   :throw-exceptions false
   :socket-timeout 30000
   :conn-timeout 5000})

(defn request-url
  [base-url path]
  (str base-url path))
