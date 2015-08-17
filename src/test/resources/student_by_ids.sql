SELECT
  id,
  first_name,
  last_name,
  gpa
FROM student
WHERE id in :ids
