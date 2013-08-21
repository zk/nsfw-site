## Web-Dev, Clojure Style <a id="intro"></a>

Web development in Clojure is a bit of a PITA right now. While there
are a ton of great libraries for facilitating web development; from
routing libraries, server-side templating, and database querying to
front-end DOM manipulation and Event binding.

NSFW provides an opinionated layer on top of all these libraries, so you can
get rolling on whatever you need to get done.

### The NSFW Namespace <a id="the-nsfw-namespace"></a>

Ok, so most of NSFW is a library itself, the 'framework' part is wholy
contained in the `nsfw` namespace. All state, such as the route table,
is contained in vars here. This allows you to dive in and swap out
parts of the functionality as needed. Also, you can get your head
around the frameworky parts of NSFW just by looking at this namespace.

### Starting an App <a id="starting-an-app"></a>

The first step to serving up requests is to start up the backend web
process. This is what's responsible for serving up requests. NSFW
provides a very easy way to start and configure this server.

First, a very simple example:

    (require 'nsfw)

    (nsfw/defroute "/" index [r] {:body "hello world"})

    (nsfw/app :server-port 5000
              :autoload "src/clj")


Open up `localhost:5000` in your browser and you'll see:

![starting screen](http://f.cl.ly/items/3Z061f1O0k0p1r2W3j0G/Screen%20Shot%202013-08-20%20at%201.30.03%20AM.png)

### Rendering HTML <a id="rendering-html"></a>

'hello world' is a good start, but let's see how easy it is to render some HTML:

    (require 'nsfw)

    (nsfw/defroute "/" index [r]
      (nsfw/render-html
       [:body
        [:h1 "Ermahgerd!"]
        [:ul
         [:li "Berks"]
         [:li "Snerks"]
         [:li "Gersbermps"]]]))

    (nsfw/app :server-port 5000
              :autoload "src/clj")

![rendering html](http://f.cl.ly/items/2h1c3B0B2m3t103C1U2L/Screen%20Shot%202013-08-20%20at%201.26.35%20AM.png)


In addition to `render-html`, you've also got `render-edn`, and
`render-json` to work with, which will craft a response with the
`Content-Type` header set to `application/edn` and `application/json`,
respectively.


    (nsfw/render-edn {:hello "world"})
    ;;> Content-Type: application/edn, Body: "{:hello \"world\"}"

    (nsfw/render-json {:hello "world"})
    ;;> Content-Type: application/json, Body: "{\"hello\":\"world\"}"


### App Config <a id="app-config"></a>

Configuration values should be passed in through the environment. NSFW
provides several functions to get this done:


    (require 'nsfw)

    (nsfw/env-str :user) ;;> "zk"

    ;; $PORT=8080
    (nsfw/env-int :port) ;;> 8080

    ;; $DEBUG=true
    (nsfw/env-bool :debug) ;;> true


See this in action in the
[NSFW Starter Kit](https://github.com/zk/nsfw-starter), where the
server's port is set from an environment variable.


    (nsfw/app :repl-port 7888
              :server-port (env/int :port 8080)
              :autoload "src/clj")


### Components

Components are custom html snippets you define that expand out into
full HTML blocks on rendering. Here's an example from this site, the
`markdown` tag, which renders this page.

NSFW Components are very much in the spirit of Google's Web
Components, but much more rudamentary.

    ;; Component definition
    (nsfw/defcomp markdown [{:keys [src]} _]
      [:div.row
       [:div.col-lg-12
        (when src
          (-> src
              slurp
              html/markdown))]])

    ;; Here's the usage
    (nsfw/render-html
      [:markdown {:src "src/md/styleguide-demo.md"}])


And the rendered output:
<div class="example">
<markdown src="src/md/styleguide-demo.md"></markdown>
</div>



### Static Pages <a id="static-pages"></a>

You'll find default 404 & 500 pages at `resources/public`. Content
found at this path will be served for these error conditions.
