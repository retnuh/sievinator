(ns sieve.storage)

(defprotocol SievinatorStorage
  "Methods for persisting sievinator state"
  (store-state [ss state] "Store the state given in map state")
  (load-state [ss] "Return the last-stored state, or nil"))
