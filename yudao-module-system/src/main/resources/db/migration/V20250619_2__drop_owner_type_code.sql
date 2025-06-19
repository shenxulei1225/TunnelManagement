-- Remove redundant owner_type_code column after refactor
ALTER TABLE system_field_category
  DROP COLUMN owner_type_code;
