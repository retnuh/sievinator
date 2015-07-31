-- name: max-index
-- returns the max index 
SELECT max(ind) AS max_index
FROM odd_numbers_seen

-- name: odd-numbers-seen
-- returns all the odd numbers seen
SELECT ind, factor
FROM odd_numbers_seen
ORDER BY ind ASC

-- name: get-config
SELECT value
FROM config
WHERE key = :key LIMIT 1

-- name: insert-odd-number!
-- Inserts an odd number and it's factor
INSERT OR REPLACE INTO odd_numbers_seen (ind, factor)
VALUES (:ind, :factor)

-- name: set-config!
-- Inserts an odd number and it's factor
INSERT OR REPLACE INTO config (key, value)
VALUES (:key, :value)


