ALTER TABLE mall_product ADD UNIQUE INDEX uk_category_unit_brand_hash (category_id, unit_id,brand_id,hash);
