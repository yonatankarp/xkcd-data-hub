CREATE TABLE web_comics
(
    id               SERIAL PRIMARY KEY,
    year             INT                                                                     NOT NULL,
    month            INT                                                                     NOT NULL,
    day              INT                                                                     NOT NULL,
    title            TEXT                                                                    NOT NULL,
    safe_title       TEXT                                                                    NOT NULL,
    transcript       TEXT                                                                    NOT NULL,
    alternative_text TEXT                                                                    NOT NULL,
    image_url        TEXT                                                                    NOT NULL,
    news             TEXT,
    link             TEXT,
    created_at       TIMESTAMP WITH TIME ZONE DEFAULT (CURRENT_TIMESTAMP AT TIME ZONE 'UTC') NOT NULL
);
