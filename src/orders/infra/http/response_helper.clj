(ns orders.infra.http.response-helper)

(defn success
  [body status]
  {:status status
   :body body})

(defn error
  [message status]
  {:status status
   :body {:error message}})

(defn result->response
  "Converts use-case result {:success true/false :order :orders :error} to HTTP response.
   success-fn receives the result and returns the body for 2xx response.
   error-status is the HTTP status for error (400 or 404).
   success-status is the HTTP status for success (default 200)."
  ([result success-fn error-status]
   (result->response result success-fn error-status 200))
  ([result success-fn error-status success-status]
   (if (:success result)
     (success (success-fn result) success-status)
     (error (:error result) error-status))))
