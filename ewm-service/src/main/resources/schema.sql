DROP TABLE IF EXISTS categories;

CREATE TABLE IF NOT EXISTS categories (
  categories_id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  categories_name VARCHAR(64) NOT NULL,
  CONSTRAINT pk_hit PRIMARY KEY (categories_id),
  CONSTRAINT uq_category_name UNIQUE (categories_name)
);