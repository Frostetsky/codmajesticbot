CREATE TABLE IF NOT EXISTS cod_majestic_rp_bot.inactive_member
(
    id              uuid PRIMARY KEY,
    discord_user_id BIGINT       NOT NULL,
    description     VARCHAR(256) NOT NULL,
    action_time     TIMESTAMP    NOT NULL
);