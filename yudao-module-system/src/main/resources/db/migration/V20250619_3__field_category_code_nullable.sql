-- Make code nullable and drop unique constraint
ALTER TABLE system_field_category
  DROP INDEX uk_parent_code,
  MODIFY COLUMN code VARCHAR(32) NULL;
