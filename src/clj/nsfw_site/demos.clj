(ns nsfw-site.demos
  (:require [nsfw.html :as html]
            [nsfw-site.layouts :as layouts]
            [nsfw.http :as http]))

(def nav
  [:ul.nav.nav-stacked.demo-nav
   [:li [:a {:href "#"} "Todo"]]])

(defn layout
  [{:keys [title body js-entry active-tab]}]
  (html/html5
   [:head
    [:meta {:name "viewport" :content "width=device-width, initial-scale=1.0"}]
    (html/stylesheet "/css/bootstrap.min.css")
    (html/stylesheet "/css/app.css")
    (html/stylesheet "/css/shCore.css")
    (html/stylesheet "/css/shClojureExtra.css")
    [:title (or title "NSFW -- The next-gen web framework.")]]
   [:body.page-demo
    layouts/navbar
    [:div.container
     [:div.row
      [:div.col-lg-12
       [:div.page-lead
        [:h1 "Demos"]
        [:p.lead "Bite-size examples of the NSFW framework."]]]]
     [:div.row
      [:div.col-lg-3
       [:ul.nav.demo-nav
        [:li {:class (when (= :the-list active-tab)
                       "active")}
         [:a {:href "/demos/the-list"} "The List"]]
        [:li {:class (when (= :the-list-redux active-tab)
                       "active")}
         [:a {:href "/demos/the-list-redux"} "The List (Redux)"]]]]
      [:div.col-lg-9.demo-content
       body]]]
    (html/script "/js/shCore.js")
    (html/script "/js/shBrushClojure.js")
    (html/script "/js/shBrushSass.js")
    (html/script "/js/app.js")
    (when js-entry
      [:script {:type "text/javascript"} js-entry])]))

(def demos-body
  "")

(defn
  ^{:route "/demos"}
  index [r]
  (-> {:body demos-body}
      layout
      http/html))
