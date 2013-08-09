(defproject nsfw-site "0.1.0"
  :description "NSFW overview / docs / demo"
  :min-lein-version "2"
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [nsfw "0.6.8"]
                 [lein-cljsbuild "0.3.2"]]
  :source-paths ["src/clj"]
  :test-paths ["test/clj"]
  :cljsbuild {:builds
              {:dev  {:source-paths ["src/cljs"]
                      :compiler {:output-to "resources/public/js/app.js"
                                 :optimizations :whitespace
                                 :pretty-print true}
                      :jar true}

               :prod {:source-paths ["src/cljs"]
                      :compiler {:output-to "resources/public/js/app.js"
                                 :optimizations :advanced
                                 :pretty-print false}
                      :jar true}}})