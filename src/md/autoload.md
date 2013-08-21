## Code Reloading <a id="intro"></a>

A great development experience is a first-class concern for NSFW, we
want you to see changes to your app as soon as you hit save.

Many of the constructs provided by NSFW work off of var metadata. For
example, a routes are picked up by the framework by attaching a piece
of metadata to a defn. These two are roughly equivalent:

    (nsfw/defroute "/foo/bar"
      foo-bar [r]
      (nsfw/render-html
        [:h1 "hello world"]))

    (defn ^{:route "/foo/bar"}
      foo-bar [r]
      (nsfw/render-html
        [:h1 "hello world"]))

We try to work with what Clojure provides, when possible.


<div class="callout">
  You have to specify an <code>:autoload</code>
  path when starting an app via <code>nsfw/app</code> function for
  routes, components, etc to work correctly.
</div>

## Under The Hood <a id="background"></a>

There's a bit of configuration that needs to happen for this to work
smoothly, namely, **route-defining vars need to be loaded, and NSFW
has to know which namespaces contain route (and other) defining
vars**.

NSFW will do this all for you by specifying the `:autoload` parameter
when you start your app:

    (nsfw/app :repl-port 7888
              :server-port 5000
              :autoload "src/clj/nsfw_site")

Autoload will (on each request, in development), look through the
provided path for clj files, parse namespaces from those files, and
look in those namespaces for route-defining vars (vars that have
`:route` metadata on them).
