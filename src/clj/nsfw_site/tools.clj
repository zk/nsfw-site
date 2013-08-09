(ns nsfw-site.tools
  (:require [nsfw.html :as html]
            [nsfw.http :as http]
            [nsfw]))

(defn
  ^{:route "/tools"}
  index [r]
  (nsfw/render
   [:default-head]
   [:page-body
    {:class "page-tools"
     :active-tab :tools}
    [:div.container
     [:div.row
      [:div.col-lg-10.col-offset-1
       [:div.page-lead
        [:h1 "Tools"]
        [:p "A collection of useful utilities for developing webapps, baked-in to NSFW"]]
       [:markdown {:src "src/md/tools.md"}]]]]]))