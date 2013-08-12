(ns nsfw-site.demos
  (:require [nsfw]))

(nsfw/defcomp demo-nav [{:keys [tab]} body]
  [:ul.nav.demo-nav
   {:data-offset-top 200
    :data-spy "affix"}
   [:li {:data-tab :autoload
         :class (when (= :autoload tab) "active")}
    [:a {:href "/examples/autoload"} "Autoload"]]
   [:li {:data-tab :the-list
         :class (when (= :the-list tab) "active")}
    [:a {:href "/examples/the-list"} "The List"]]
   [:li.the-list-redux {:class (when (= :the-list-redux tab) "active")}
    [:a {:href "/examples/the-list-redux"} "The List (Redux)"]]])

(nsfw/defcomp demo [{:keys [tab js-entry]} body]
  [:page-body
   {:class "page-demo"
    :active-tab :demos
    :script "/js/app.js"
    :js-entry js-entry}
   [:div.container
    [:div.row
     [:div.col-lg-12
      [:div.page-lead
       [:h1 "Demos"]
       [:p.lead "Bite-size examples of the NSFW framework."]]]]
    [:div.row
     [:div.col-lg-3
      [:demo-nav {:tab tab}]]
     [:div.col-lg-9.demo-content
      body]]]])

(nsfw/defroute "/examples/the-list"
  the-list [r]
  (nsfw/render
   [:default-head]
   [:demo
    {:tab :the-list
     :js-entry "nsfw_site.demos.thelist.main()"}
    [:div#the-list
     [:markdown {:src "src/md/the-list.md"}]]]))

(nsfw/defroute "/examples/the-list-redux"
  the-list-redux [r]
  (nsfw/render
   [:default-head]
   [:demo
    {:tab :the-list-redux
     :js-entry "nsfw_site.demos.redux.main()"}
    [:div#the-list
     [:markdown {:src "src/md/the-list-redux.md"}]]]))

(nsfw/defroute "/examples/data-binding"
  data-binding [r]
  (nsfw/render
   [:default-head]
   [:demo
    {:tab :the-list-redux
     :js-entry "nsfw_site.demos.redux.main()"}
    [:div#the-list
     [:markdown {:src "src/md/the-list-redux.md"}]]]))

(nsfw/defroute "/examples/autoload"
  autoload [r]
  (nsfw/render
   [:default-head]
   [:demo
    {:tab :autoload
     :js-entry 'nsfw-site.app.entry}
    [:div#autoload
     [:markdown {:src "src/md/autoload.md"}]]]))