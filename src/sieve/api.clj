(ns sieve.api
  (:require [org.zalando.stups.friboo.system.http :refer [def-http-component]]
            [ring.util.response :refer :all]
            [org.zalando.stups.friboo.ring :refer :all]
            [org.zalando.stups.friboo.log :as log]
            [io.sarnowski.swagger1st.util.api :as api]))

; define the API component and its dependencies
(def-http-component API "api/sieve-api.yaml" [svc])

(def default-http-configuration
  {:http-port 8080})

;; primes

(defn primes-up-to [{:keys [n] :as params} request {:keys [primes-up-to]}]
  (log/debug "params: '%s'" params)
  (log/debug (str n))
  (-> n
      long
      primes-up-to
      vec
      response
      content-type-json))

(defn primes-from-to [{:keys [from to] :as params} request {:keys [primes-up-to]}]
  (log/debug "params: '%s'" params)
  (-> (drop-while #(< % from) (primes-up-to to))
      vec
      response
      content-type-json))

;; applications

;; (defn read-applications [{:keys [search]} request db]
;;   (u/require-internal-user request)
;;   (if (nil? search)
;;     (do
;;       (log/debug "Read all applications.")
;;       (-> (sql/cmd-read-applications {} {:connection db})
;;           (sql/strip-prefixes)
;;           (response)
;;           (content-type-json)))
;;     (do
;;       (log/debug "Search in applications with term %s." search)
;;       (-> (sql/cmd-search-applications {:searchquery search} {:connection db})
;;           (sql/strip-prefixes)
;;           (response)
;;           (content-type-json)))))

;; (defn load-application
;;   "Loads a single application by ID, used for team checks."
;;   [application-id db]
;;   (-> (sql/cmd-read-application
;;         {:id application-id}
;;         {:connection db})
;;       (sql/strip-prefixes)
;;       (first)))

;; (defn read-application [{:keys [application_id]} request db]
;;   (u/require-internal-user request)
;;   (log/debug "Read application %s." application_id)
;;   (-> (sql/cmd-read-application
;;         {:id application_id}
;;         {:connection db})
;;       (sql/strip-prefixes)
;;       (single-response)
;;       (content-type-json)))

;; (defn create-or-update-application! [{:keys [application application_id]} request db]
;;   (let [old-application (load-application application_id db)
;;         defaults {:specification_url nil
;;                   :documentation_url nil
;;                   :subtitle          nil
;;                   :scm_url           nil
;;                   :service_url       nil
;;                   :description       nil
;;                   :required_approvers 2}]
;;     (u/require-internal-team (or (:team_id old-application) (:team_id application)) request)
;;     (sql/cmd-create-or-update-application!
;;       (merge defaults application {:id application_id})
;;       {:connection db})
;;     (log/audit "Created/updated application %s using data %s." application_id application)
;;     (response nil)))

;; (defn read-application-approvals [{:keys [application_id]} request db]
;;   (u/require-internal-user request)
;;   (log/debug "Read all approvals for application %s." application_id)
;;   (->> (sql/cmd-read-application-approvals
;;          {:application_id application_id}
;;          {:connection db})
;;        (sql/strip-prefixes)
;;        (map #(:approval_type %))
;;        (response)
;;        (content-type-json)))

;; ;; versions

;; (defn read-versions-by-application [{:keys [application_id]} request db]
;;   (u/require-internal-user request)
;;   (log/debug "Read all versions for application %s." application_id)
;;   (-> (sql/cmd-read-versions-by-application
;;         {:application_id application_id}
;;         {:connection db})
;;       (sql/strip-prefixes)
;;       (response)
;;       (content-type-json)))

;; (defn read-version-by-application [{:keys [application_id version_id]} request db]
;;   (u/require-internal-user request)
;;   (log/debug "Read version %s of application %s." version_id application_id)
;;   (-> (sql/cmd-read-version-by-application
;;         {:id             version_id
;;          :application_id application_id}
;;         {:connection db})
;;       (sql/strip-prefixes)
;;       (single-response)
;;       (content-type-json)))

;; (defn create-or-update-version! [{:keys [application_id version_id version]} request db]
;;   (if-let [application (load-application application_id db)]
;;     (do
;;       (u/require-internal-team (:team_id application) request)
;;       (with-db-transaction
;;         [connection db]
;;         (let [defaults {:notes nil}]
;;           (sql/cmd-create-or-update-version!
;;             (merge defaults version {:id             version_id
;;                                      :application_id application_id})
;;             {:connection connection}))
;;         (sql/cmd-delete-approvals! {:application_id application_id :version_id version_id} {:connection connection}))
;;       (log/audit "Created/updated version %s for application %s using data %s." version_id application_id version)
;;       (response nil))
;;     (api/error 404 "application not found")))

;; ;; approvals

;; (defn read-approvals-by-version [{:keys [application_id version_id]} request db]
;;   (u/require-internal-user request)
;;   (log/debug "Read approvals for version %s of application %s." version_id application_id)
;;   (-> (sql/cmd-read-approvals-by-version
;;         {:version_id     version_id
;;          :application_id application_id}
;;         {:connection db})
;;       (sql/strip-prefixes)
;;       (response)
;;       (content-type-json)))

;; (defn approve-version! [{:keys [application_id version_id approval]} request db]
;;   (if-let [application (load-application application_id db)]
;;     (do
;;       (u/require-internal-team (:team_id application) request)
;;       (let [defaults {:notes nil}]
;;         (sql/cmd-approve-version!
;;           (merge defaults approval {:version_id     version_id
;;                                     :application_id application_id
;;                                     :user_id        (get-in request [:tokeninfo "uid"])})
;;           {:connection db}))
;;       (log/audit "Approved version %s for application %s." version_id application_id)
;;       (response nil))
;;     (api/error 404 "application not found")))
