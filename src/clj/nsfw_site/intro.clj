(ns nsfw-site.intro
  (:require [clojure.string :as str]
            [clojure.zip :as zip]
            [nsfw.html :as html]
            [nsfw.util :as util]
            [nsfw]
            [hickory.core :as hi]))

(defn escape [s]
  (-> s
      (str/replace #"<" "&lt;")
      (str/replace #">" "&gt;")))

(nsfw/defcomp masthead [opts body]
  (let [{:keys [active-tab]} opts]
    [:header.navbar.navbar-inverse
     {:data-nsfw-component :masthead}
     [:a.navbar-brand {:href "/"}
      [:h1
       [:span.nsfw-icon
        ;; alembic
        "⚗"] "NSFW"]]
     [:ul.navbar-nav.nav.pull-right
      [:li {:class (when (= :home active-tab) "active")}
       [:a {:href "/"} "Home"]]
      [:li {:class (when (= :examples active-tab) "active")}
       [:a {:href "/examples/getting-started"} "Examples"]]]]))

(nsfw/defcomp default-head [{:keys [title]} body]
  [:head
   [:meta {:name "viewport" :content "width=device-width, initial-scale=1.0"}]
   (html/stylesheet "/css/bootstrap.min.css")
   (html/stylesheet "/css/app.css")
   (html/script "/js/app.js")
   [:title (or title "No Such Framework")]])

(nsfw/defcomp default-footer [opts body]
  [:footer
   [:div.row
    [:div.col-lg-9
     [:p "NSFW -- A Next-Gen Web Framework"]
     [:p
      "Brought to you by "
      [:a {:href "https://twitter.com/heyzk"} "@heyzk"]
      ", and the letters G, S, and D."]]
    [:div.col-lg-3
     [:div
      [:p
       [:i.icon-github]
       [:a {:href "#"} "GitHub"]]]]]])

(nsfw/defcomp markdown [{:keys [src]} body]
  [:div.row
   [:div.col-lg-12
    (when src
      (-> src
          slurp
          html/markdown
          hi/parse
          hi/as-hiccup))]])

(defn sym->js [sym]
  (-> sym
      str
      (str/replace #"-" "_")))

(nsfw/defcomp page-body [{:keys [class active-tab scripts js-entry]} body]
  [:body
   {:class class}
   [:masthead {:active-tab active-tab}]
   body
   [:default-footer]
   (when-not (empty? scripts)
     (map html/script scripts))
   (when js-entry
     [:script {:type "text/javascript"}
      (when js-entry
        (if (symbol? js-entry)
          (str (sym->js js-entry) "();")
          js-entry))])])

(def index-body
  [:div.intro-body.container
   [:div.row.call-out
    [:div.col-lg-12
     [:section
      [:h1 "Wat?"]
      [:p.lead "NSFW is a framework designed to scale along with your needs.
           We value understandability through introspection: it should
           be easy to understand what's going on in your app, even as it
           scales to thousands of lines of code, and millions of users."]]]]
   [:div.row.rationale
    [:div.col-lg-12
     [:h2 "ZOMG a Clojure framework?!"]]
    [:div.col-lg-6
     [:section
      [:p "While there are a ton of great libraries for web programming in
           Clojure, something's missing for GSD in that space. Frameworks like
           NSFW provide convention, which helps you scale; in features, in users,
           and in team size."]
      [:p "Don't worry, we won't lock you in. Try NSFW out and you'll see
           what you've been missing."]]]
    [:div.col-lg-6.images
     [:a {:href "http://clojure.org"}
      [:img.clojure-logo
       {:src "/img/clojure-logo.png"
        :width "120"
        :height "120"}]]
     [:a {:href "http://h"}]
     [:img.html5-logo
      {:src "http://www.w3.org/html/logo/downloads/HTML5_Badge_512.png"
       :width "120"
       :height "120"}]
     [:img.sass-logo
      {:src "http://sass-lang.com/images/sass.gif"
       :width "120"
       :height "120"}]]]
   [:div.row.mobile-optimized
    [:div.col-lg-12
     [:h2 "Mobile Optimized"]]
    [:div.col-lg-6
     [:section
      [:p "All NSFW components are tested on a wide range of screen sizes for mobile bliss. The base CSS for NSFW is brought to you by " [:a {:href "http://getbootstrap.com"} "Bootstrap"] ", so you know you're starting out right."]]]
    [:div.col-lg-6
     [:img {:src "/img/mobile-sizes.png"}]]]
   [:div.row.getting-started
    [:div.col-lg-12
     [:h2 "Getting Started"]]
    [:div.col-lg-5
     [:section
      [:p
       "It's easy to get started with NSFW. We've got a starter project hosted "
       [:a {:href "https://github.com/zk/nsfw-starter"} "here"]
       "."]]]
    [:div.col-lg-7
     [:pre
      "
$ git clone https://github.com/zk/nsfw-starter myapp
...
$ cd myapp
$ bin/dev
...
$ open http://localhost:5000
"]]]
   [:br]
   [:br]
   [:br]])

(nsfw/defroute "/"
  index [r]
  (nsfw/render-html
   [:default-head]
   [:page-body
    {:class "page-intro"
     :active-tab :home}
    [:div.bleed-box
     [:div.full-bleed {:style "background-image: url('/img/dog3.jpg');"}]
     [:div.bleed-box-content
      [:div.hero-content
       [:h2 "Get web stuff done with Clojure"]
       [:p
        "NSFW is a framework that optimizes for introspection and "
        "understanding, that helps you build modern webapps using "
        "HTML5, CSS3, and Clojure."]
       [:p "Super-extra alpha."]]]]
    index-body]))

(nsfw/defcomp component-preview [opts body]
  [:pre (util/pp-str (->> body
                          nsfw/apply-comps
                          first))])

(nsfw/defcomp component-demo [opts body]
  [:div
   [:pre
    (-> body first util/pp-str escape)
    [:div.tag "tag"]]
   [:div.libcont
    body
    [:div.tag "preview"]]
   [:pre
    (-> body first nsfw/apply-comps util/pp-str escape)
    [:div.tag "html"]]])

(nsfw/defcomp sub-nav [{:keys [data-offset-top]} body]
  [:ul.nav.sub-nav
   {:data-offset-top (or data-offset-top 200)
    :data-spy "affix"}
   body])

(nsfw/defcomp nav-item [{:keys [active href]} body]
  [:li {:class (when active "active")}
   [:a {:href href}
    body]])

(nsfw/defroute "/styleguide" styleguide [r]
  (nsfw/render-html
   [:default-head
    {:title "NSFW Component Styleguide"}]
   [:page-body
    {:class "library"
     :active-tab :styleguide}
    [:div.container
     [:div.row
      [:div.col-lg-12
       [:div.page-lead
        [:h1 "Component Styleguide"]
        [:p.lead
         "Componentization gets you a styleguide for free. Your design friends will love this."]]]]
     [:div.row
      #_[:div.col-lg-2
         [:sub-nav
          [:nav-item {:href "#clj"} "clj"]
          [:nav-item {:href "#cljs"} "cljs"]]]
      [:div.col-lg-12
       [:h2 "Masthead"]
       [:component-demo
        [:masthead {:active-tab :home}]]
       [:br]
       [:h2 "Footer"]
       [:component-demo
        [:default-footer]]
       [:div.demo-nav-container
        [:h2 "Demo Nav"]
        [:div.libcont
         [:demo-nav]
         [:demo-nav {:tab :the-list}]
         [:demo-nav {:tab :the-list-redux}]]]
       [:div.demo-page-container
        [:h2 "Demo Page"]
        [:component-demo
         [:demo
          {:tab :the-list
           :js-entry "/* ex: nsfw_site.demos.entry(); */"}
          [:h2 "Content Goes Here"]]]]
       [:div
        [:h2 "Markdown"]
        [:p "Easily render a markdown document."]
        [:component-demo
         [:markdown {:src "src/md/styleguide-demo.md"}]]]]]]]))