(ns nsfw-site.demos.todo
  (:require [nsfw.http :as http]
            [nsfw.html :as html]
            [nsfw-site.layouts :as layouts]))

(def body
  {:body
   [:div.page-demo.container
    [:div.row
     [:div.col-lg-10.col-offset-1
      [:h1 "The List"]
      [:p
       "Here's a quick todo app demo using imperative "
       "dom manipulation and event binding."]]]
    [:div.row
     [:div.col-lg-10.col-offset-1
      [:div#todoapp]]]
    [:div.row
     [:div.col-lg-10.col-offset-1
      [:section.code
       [:h3 [:code "src/clj/nsfw_site/demos/todo.clj"]]
       [:pre {:class "brush:clojure"}
        (slurp "src/clj/nsfw_site/demos/todo.clj")]]
      [:section.code
       [:h3 [:code "src/scss/todo.scss"]]
       [:pre {:class "brush:sass"}
        (slurp "src/scss/todo.scss")]]
      [:section.code
       [:h3 [:code "src/clj/nsfw_site/demos/todo.cljs"]]
       [:pre {:class "brush:clojure"}
        (slurp "src/cljs/nsfw_site/demos/todo.cljs")]]]]]
   :js-entry "nsfw_site.demos.todo.main()"})

(defn
  ^{:route "/demos/todo"}
  todo
  [r]
  (-> body
      layouts/demo
      http/html))
