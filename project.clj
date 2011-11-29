(defproject trailhead "0.1.0"
  :description "Clojure Deployment of Trailhead Pottery"
  :aot [trailhead.core]
  :dependencies [[compojure "0.4.0-RC3"]
                 [ring/ring-servlet "0.2.1"]
                 [hiccup "0.2.4"]
                 [appengine "0.2"]
                 [com.google.appengine/appengine-api-1.0-sdk "1.3.4"]
                 [com.google.appengine/appengine-api-labs "1.3.4"]
                 [fleet "0.9.5"]]
  :dev-dependencies [[swank-clojure "1.2.0"]
                     [ring/ring-jetty-adapter "0.2.0"]
                     [com.google.appengine/appengine-local-runtime "1.3.4"]
                     [com.google.appengine/appengine-api-stubs "1.3.4"]]
  :repositories {"maven-gae-plugin" "http://maven-gae-plugin.googlecode.com/svn/repository"}
  :compile-path "war/WEB-INF/classes"
  :library-path "war/WEB-INF/lib")
