SELECT
  id,
  first_name,
  last_name,
  gpa
FROM students
WHERE id in :ids
LIMIT :limit
