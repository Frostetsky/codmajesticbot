CREATE TABLE IF NOT EXISTS cod_majestic_rp_bot.main_button
(
    id                 uuid PRIMARY KEY,
    discord_message_id BIGINT       NOT NULL,
    name_button        VARCHAR(256) NOT NULL,
    discord_channel_id BIGINT       NOT NULL
);

CREATE TABLE IF NOT EXISTS cod_majestic_rp_bot.family_member
(
    id                   uuid PRIMARY KEY,
    discord_profile_name VARCHAR(256) NOT NULL,
    discord_username     VARCHAR(256) NOT NULL,
    discord_user_id      BIGINT       NOT NULL,
    points               BIGINT       NOT NULL DEFAULT 0
);

CREATE TABLE IF NOT EXISTS cod_majestic_rp_bot.event_crime
(
    id                 uuid PRIMARY KEY,
    event_crime_type   VARCHAR(256)                                           NOT NULL,
    action_time        TIMESTAMP                                              NOT NULL,
    event_crime_server VARCHAR(256)                                           NOT NULL,
    discord_message_id BIGINT                                                 NOT NULL,
    family_member_id   uuid REFERENCES cod_majestic_rp_bot.family_member (id) NOT NULL
);

CREATE TABLE IF NOT EXISTS cod_majestic_rp_bot.event_crime_pair_point
(
    id               uuid PRIMARY KEY,
    event_crime_type VARCHAR(32) NOT NULL,
    point            INT         NOT NULL
);