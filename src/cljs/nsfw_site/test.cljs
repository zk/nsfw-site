(ns nsfw-site.test
  (:require [nsfw.dom :as dom]))

;; Visual test helpers

(defn content [run]
  (let [content (dom/$ [:div])]
    (dom/append content (run))
    [:div.test-content
     (-> content
         (dom/click (fn [e]
                      (-> content
                          dom/empty
                          (dom/append (run))))))
     content]))

(defn section [{:keys [title desc run code]}]
  [:div.test-section
   [:div.row
    [:div.span12
     [:h3 title]]]
   [:div.row
    [:div.col.span5
     (when desc [:p desc])
     (when code [:pre code])]
    [:div.col.span7
     (when run
       (content run))]]])