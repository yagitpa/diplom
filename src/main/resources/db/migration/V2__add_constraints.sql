-- 1. Добавление CHECK-ограничения на цену объявления
ALTER TABLE ads
    ADD CONSTRAINT chk_ad_price_range
        CHECK (price >= 0 AND price <= 10000000);

-- 2. Внешние ключи
ALTER TABLE ads
    ADD CONSTRAINT fk_ads_user
        FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE;

ALTER TABLE comments
    ADD CONSTRAINT fk_comments_user
        FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE;

ALTER TABLE comments
    ADD CONSTRAINT fk_comments_ad
        FOREIGN KEY (ad_id) REFERENCES ads (pk) ON DELETE CASCADE;

-- 3. Индексы для оптимизации запросов
CREATE INDEX idx_ads_user_id ON ads (user_id);
CREATE INDEX idx_comments_user_id ON comments (user_id);
CREATE INDEX idx_comments_ad_id ON comments (ad_id);