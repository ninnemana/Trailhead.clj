(ns trailhead.core
  (:gen-class :extends javax.servlet.http.HttpServlet)
  (:use compojure.core
        [ring.util.servlet   :only [defservice]]
        [ring.util.response  :only [redirect]]
        [hiccup.core         :only [h html]]
        [hiccup.page-helpers :only [doctype include-css link-to xhtml-tag]]
        [hiccup.form-helpers :only [form-to text-area text-field]])
  (:import (com.google.appengine.api.datastore Query))
  (:require [compojure.route          :as route]
            [appengine.datastore.core :as ds]
            [appengine.users          :as users]))

(defn mail [& m]
  (let [mail (apply hash-map m)
        props (java.util.Properties.)]

    (doto props
      (.put "mail.smtp.host" (:host mail))
      (.put "mail.smtp.port" (:port mail))
      (.put "mail.smtp.user" (:user mail))
      (.put "mail.smtp.socketFactory.port"  (:port mail))
      (.put "mail.smtp.auth" "true"))

    (if (= (:ssl mail) true)
      (doto props
        (.put "mail.smtp.starttls.enable" "true")
        (.put "mail.smtp.socketFactory.class" 
              "javax.net.ssl.SSLSocketFactory")
        (.put "mail.smtp.socketFactory.fallback" "false")))

    (let [authenticator (proxy [javax.mail.Authenticator] [] 
                          (getPasswordAuthentication 
                            []
                            (javax.mail.PasswordAuthentication. 
                             (:user mail) (:password mail))))
          recipients (reduce #(str % "," %2) (:to mail))
          session (javax.mail.Session/getInstance props authenticator)
          msg     (javax.mail.internet.MimeMessage. session)]
      
      (.setFrom msg (javax.mail.internet.InternetAddress. (:user mail)))
      
      (.setRecipients msg 
                      (javax.mail.Message$RecipientType/TO)
                      (javax.mail.internet.InternetAddress/parse recipients))
      
      (.setSubject msg (:subject mail))
      (.setText msg (:text mail))
      (javax.mail.Transport/send msg))))


(def home-page
  (html
    (doctype :html5)
      [:head
	[:meta {:charset "utf-8"}]
	[:meta {:http-equiv "X-UA-Compatible" :content "IE=edge,chrome=1"}]
	[:meta {:name "viewport" :content "width=device-width, initial-scale=1,maximum-scale=1"}]
	[:title "Trailhead Pottery"]
	(include-css "http://fonts.googleapis.com/css?family=Rock+Salt")
	(include-css "/css/landing.css")]
      [:body
	[:img {:src "/img/throwing_newsite.png" :id "new_site" :alt "We're throwing our new site!"}]]
	[:header
	  [:img {:src "/img/logo.png" :class "logo" :alt "Welcome to Trailhead Pottery"}]
	  [:span {:class "sub_title"} "Home of Eau Claire's Biggest Muffin!"]]
	[:section
	  [:img {:src "/img/foot.png" :class "bar" :alt ""}]
	  [:form {:method "post" :action "/idea"}
	    [:p
	      [:span "Have an idea?"]
	      "We'd love to hear it."]
	    [:input {:type "email" :name "email" :id "email" :placeholder "Enter your e-mail"}]
	    [:textarea {:rows "10" :cols "20" :name "idea" :id "idea"}]
	    [:input {:type "submit" :name "btnSubmit" :id "btnSubmit" :value "Submit Your Idea"}]
]]
	[:footer]
	[:script {:type "text/javascript" :src "/js/analytics.js"}]
))

(def idea
	(mail :user "ninnemana@gmail.com" :password "D3fj@m84"
	      :host "smtp.gmail.com"
	      :port 465
      	      :ssl true
      :to ["alex@ninneman.org" ]
      :subject "I Have Rebooted." 
      :text "I Have Rebooted.")
)

(defroutes example
  (GET "/" [] home-page)
  (POST "/idea" [] idea)
  (route/not-found "Page not found"))

(defservice example)

