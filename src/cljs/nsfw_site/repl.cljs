(ns nsfw-site.repl
  (:require [nsfw.util :as util]
            [nsfw.dom :as $]
            [nsfw.bind :as bind]))

(declare render)

(defn render-map [data]
  [:table.table-striped.table-bordered
   [:tbody
    (map (fn [[k v]]
           [:tr
            [:td (render k)]
            [:td (render v)]])
         data)]])

(defn render-vector [data]
  [:ul
   (map (fn [v]
          [:li [:pre (pr-str v)]])
        data)])

(defn render-keyword [data]
  [:code (pr-str data)])

(defn render [data]
  (cond
   (map? data) (render-map data)
   (vector? data) (render-vector data)
   (keyword? data) (render-keyword data)
   (string? data) [:code (pr-str data)]
   :else ($/node [:pre (pr-str data)])))

(defn write-data [{:keys [data timestamp] :as payload}]
  [:div.console-entry (render data)])

(defn handle-clj-data [console update delta]
  (fn [{:keys [server-time chunks]}]
    (doseq [chunk (reverse chunks)]
      ($/insert-at console (write-data chunk) 0))
    (util/timeout #(update server-time) delta)))

(defn handle-html-data [console update delta]
  (fn [{:keys [server-time htmls]}]
    (doseq [html (reverse htmls)]
      ($/insert-at console [:div.console-entry ($/parse-html html)] 0))
    (util/timeout #(update server-time) delta)))

(defn ping [console]
  (let [delta 1000]
    (letfn [(update [last-update]
              (bind/ajax
               {:method "GET"
                :path (str "/repl/data?since=" last-update)
                :success (handle-html-data console update delta)
                :error (fn []
                         (util/timeout #(update last-update) delta))}))]
      (update 0))))

(defn ^:export entry []
  (let [console ($/query :#repl)]
    (ping console)))