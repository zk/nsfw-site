(ns nsfw-site.repl
  (:require [nsfw]
            [nsfw.html :as html]
            [clojure.string :as str]))

(nsfw/defroute "/repl"
  repl [r]
  (nsfw/render
   [:default-head]
   [:page-body
    {:js-entry "nsfw_site.repl.entry()"
     :class "page-repl"}
    [:div.container
     [:div#repl]]]))

(defn escape [s]
  (-> s
      (str/replace #"<" "&lt;")
      (str/replace #">" "&gt;")))

(defn render-fn [data]
  (let [s (pr-str data)
        [_ ns fn-str] (->> s
                           (re-find #"\s(.*)$")
                           second
                           (re-find #"^([^$]*)\$(.*)"))
        fn-name (-> fn-str
                    (str/split #"@")
                    first)
        fn-name (if (re-find #"^eval" fn-name)
                  "anonymous"
                  fn-name)
        fq-fn-name (str ns "/" fn-name)]
    [:code (str "fn:" fq-fn-name)]))

(defn render [data]
  (html/html
   (cond
    (fn? data) (render-fn data)
    (map? data) [:table
                 (map (fn [[k v]]
                        [:tr
                         [:td (render k)]
                         [:td (render v)]])
                      data)]
    :else [:code (pr-str data)])))

(defonce chunks (atom '()))

(nsfw/defroute "/repl/data"
  repl-data [{:keys [params]}]
  (let [now (System/currentTimeMillis)
        since (Long/parseLong (:since params))
        max-str (:max params)
        max (if max-str
              (Integer/parseInt max-str)
              50)]
    {:body (pr-str {:server-time now
                    :htmls (->> @chunks
                                (take-while #(> (:timestamp %) since))
                                (take max)
                                (map :data)
                                (map render))})}))

(defn post-to-repl [data]
  (swap! chunks conj {:timestamp (System/currentTimeMillis)
                      :data data
                      :type (type data)}))

(def pp post-to-repl)