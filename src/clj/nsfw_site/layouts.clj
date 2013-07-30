(ns nsfw-site.layouts
  (:require [nsfw.html :as html]))

(def navbar
  [:header.navbar.navbar-inverse
   [:a.navbar-brand {:href "/"}
    [:h1
     [:span.nsfw-icon
      ;; alembic
      "⚗"] "NSFW"]]
   [:ul.navbar-nav.nav.pull-right
    [:li [:a {:href "/"} "Home"]]
    [:li [:a {:href "/getting-started"} "Getting Started"]]
    [:li [:a {:href "/demos/todo"} "Demos"]]]])

(defn main
  [{:keys [title body]}]
  (html/html5
   [:head
    [:meta {:name "viewport" :content "width=device-width, initial-scale=1.0"}]
    (html/stylesheet "/css/bootstrap.min.css")
    (html/stylesheet "/css/app.css")
    [:title (or title "NSFW -- The next-gen web framework.")]]
   [:body
    navbar
    body
    (html/script "/js/app.js")]))

(defn demo
  [{:keys [title body js-entry]}]
  (html/html5
   [:head
    [:meta {:name "viewport" :content "width=device-width, initial-scale=1.0"}]
    (html/stylesheet "/css/bootstrap.min.css")
    (html/stylesheet "/css/app.css")
    (html/stylesheet "/css/shCore.css")
    (html/stylesheet "/css/shClojureExtra.css")
    [:title (or title "NSFW -- The next-gen web framework.")]]
   [:body
    navbar
    body
    (html/script "/js/shCore.js")
    (html/script "/js/shBrushClojure.js")
    (html/script "/js/shBrushSass.js")
    (html/script "/js/app.js")
    (when js-entry
      [:script {:type "text/javascript"} js-entry])]))

(defn intro [{:keys [title body]}]
  (html/html5
   [:head
    [:meta {:name "viewport" :content "width=device-width, initial-scale=1.0"}]
    (html/stylesheet "/css/bootstrap.min.css")
    (html/stylesheet "/css/app.css")
    (html/script "/js/intro.js")
    [:title (or title "NSFW -- The next-gen web framework.")]]
   [:body.page-intro
    navbar
    [:div.bleed-box
     [:div.full-bleed {:style "background-image: url('/img/dog3.jpg');"}]
     [:div.bleed-box-content
      [:div.hero-content
       [:h2 "Get web stuff done with Clojure"]
       [:p
        "NSFW is a framework that optimizes for introspection and "
        "understanding, that helps you build modern webapps using "
        "HTML5, CSS3, and Clojure."]
       [:p "Super-extra alpha."]
       #_[:ul.sections
          [:li [:a.hero-link {:href "/"} "DOM Manipulation / Event Binding"]]]]]]
    body]))
