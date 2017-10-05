ALTER TABLE LESSON
  ADD COLUMN index_number INTEGER;

WITH LESSON_INDEX AS (
    SELECT
      l1.id,
      (SELECT count(*)
       FROM LESSON l2
       WHERE l1.id >= l2.id AND l1.stream_id = l2.stream_id AND l1.type_id = l2.type_id AND
             (l1.group_id = l2.group_id OR (l1.group_id IS NULL AND l2.group_id IS NULL))) AS index_number
    FROM LESSON l1
)
UPDATE LESSON
SET INDEX_NUMBER = (SELECT li.INDEX_NUMBER
                    FROM LESSON_INDEX li
                    WHERE li.id = LESSON.id);

ALTER TABLE STREAM
  ADD COLUMN lecture_count INTEGER;
ALTER TABLE STREAM
  ADD COLUMN practical_count INTEGER;
ALTER TABLE STREAM
  ADD COLUMN lab_count INTEGER;
