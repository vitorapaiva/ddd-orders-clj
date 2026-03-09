(ns orders.config
  (:require [clojure.edn :as edn]
            [clojure.java.io :as io]))

(defn load-config
  []
  (-> (io/resource "config.edn")
      slurp
      edn/read-string))

(defn get-config
  []
  (let [config (load-config)]
    (-> config
        (assoc-in [:db :host] (or (System/getenv "DB_HOST") (get-in config [:db :host])))
        (assoc-in [:db :port] (or (some-> (System/getenv "DB_PORT") Integer/parseInt) (get-in config [:db :port])))
        (assoc-in [:db :database] (or (System/getenv "DB_DATABASE") (get-in config [:db :database])))
        (assoc-in [:db :user] (or (System/getenv "DB_USER") (get-in config [:db :user])))
        (assoc-in [:db :password] (or (System/getenv "DB_PASSWORD") (get-in config [:db :password])))
        (assoc-in [:products-service :base-url] (or (System/getenv "PRODUCTS_SERVICE_URL") (get-in config [:products-service :base-url])))
        (assoc-in [:server :port] (or (some-> (System/getenv "SERVER_PORT") Integer/parseInt) (get-in config [:server :port]))))))
