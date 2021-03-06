(ns seven-guis.core
  (:require
   [reagent.core :as reagent :refer [atom]]
   [reagent.dom :as rdom]
   [reagent.session :as session]
   [reitit.frontend :as reitit]
   [clerk.core :as clerk]
   [accountant.core :as accountant]
   [seven-guis.counter :as counter]
   [seven-guis.temperature-converter :as temperature-converter]
   [seven-guis.flight-booker :as flight-booker]
   [seven-guis.timer :as timer])
  )

;; -------------------------
;; Routes

(def router
  (reitit/router
   [["/" :index]
    ["/counter" :counter]
    ["/temperature-converter" :temperature-converter]
    ["/flight-booker" :flight-booker]
    ["/timer" :timer]
    ["/about" :about]]))

(defn path-for [route & [params]]
  (if params
    (:path (reitit/match-by-name router route params))
    (:path (reitit/match-by-name router route))))

;; -------------------------
;; Page components

(defn home-page []
  (fn []
    [:span.main
     [:h1 "Welcome to seven-guis"]
     ]))

(defn about-page []
  (fn [] [:span.main
          [:h1 "About seven-guis"]]))

;; -------------------------
;; Translate routes -> page components

(defn page-for [route]
  (case route
    :index #'home-page
    :counter #'counter/page
    :temperature-converter #'temperature-converter/page
    :flight-booker #'flight-booker/page
    :timer #'timer/page
    :about #'about-page
    ))


;; -------------------------
;; Page mounting component

(defn current-page []
  (fn []
    (let [page (:current-page (session/get :route))]
      [:div
       [:header
        [:p [:a {:href (path-for :index)} "Home"] " | "
         [:a {:href (path-for :about)} "About seven-guis"] " | "
         [:a {:href (path-for :counter)} "Counter"] " | "
         [:a {:id "temperature-converter" :href (path-for :temperature-converter)} "Temperature Converter"] " | "
         [:a {:id "flight-booker" :href (path-for :flight-booker)} "Flight Booker"] " | "
         ]
         [:a {:id "timer" :href (path-for :timer)} "Timer"]
        ]
       [page]
       ])))

;; -------------------------
;; Initialize app

(defn mount-root []
  (rdom/render [current-page] (.getElementById js/document "app")))

(defn init! []
  (clerk/initialize!)
  (accountant/configure-navigation!
   {:nav-handler
    (fn [path]
      (let [match (reitit/match-by-path router path)
            current-page (:name (:data  match))
            route-params (:path-params match)]
        (reagent/after-render clerk/after-render!)
        (session/put! :route {:current-page (page-for current-page)
                              :route-params route-params})
        (clerk/navigate-page! path)
        ))
    :path-exists?
    (fn [path]
      (boolean (reitit/match-by-path router path)))})
  (accountant/dispatch-current!)
  (mount-root))
