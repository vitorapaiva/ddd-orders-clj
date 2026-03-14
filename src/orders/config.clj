(ns orders.config
  (:require [clojure.edn :as edn]
            [clojure.java.io :as io]))

(defn load-config
  []
  (-> (io/resource "config.edn")
      slurp
      edn/read-string))

(def env-overrides
  [[[:db :host] "DB_HOST" identity]
   [[:db :port] "DB_PORT" #(some-> % Integer/parseInt)]
   [[:db :database] "DB_DATABASE" identity]
   [[:db :user] "DB_USER" identity]
   [[:db :password] "DB_PASSWORD" identity]
   [[:products-service :base-url] "PRODUCTS_SERVICE_URL" identity]
   [[:server :port] "SERVER_PORT" #(some-> % Integer/parseInt)]])

(defn get-config
  []
  (let [config (load-config)]
    (reduce
     (fn [cfg [path env-key parse-fn]]
       (assoc-in cfg path
                 (or (some-> (System/getenv env-key) parse-fn)
                     (get-in config path))))
     config
     env-overrides)))
