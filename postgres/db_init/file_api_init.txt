DROP TABLE IF EXISTS song_data;
CREATE TABLE song_data (
                           sd_id bigint PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
                           original_name VARCHAR(255) UNIQUE,
                           storage_type int,
                           path VARCHAR(255),
                           bucket_name VARCHAR(100)
);