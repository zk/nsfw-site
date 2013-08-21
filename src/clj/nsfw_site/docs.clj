(ns nsfw-site.docs
  (:require [nsfw]))

(nsfw/defroute "/docs"
  docs [r]
  (nsfw/render-html
   [:default-head]
   [:page-body
    [:div.container
     [:div.row
      [:div.col-lg-12
       [:div.page-lead
        [:h1 "Docs"]
        [:p.lead "Take a look at our tasty docs."]]]]
     [:div.row
      [:div.col-lg-3
       [:sub-nav
        [:nav-item {:href "#"}
         "Hello World"]]]]]]))