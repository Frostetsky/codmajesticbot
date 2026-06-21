CREATE TABLE IF NOT EXISTS cod_majestic_rp_bot.personal_branch
(
    id                   uuid PRIMARY KEY,
    discord_user_id      BIGINT       NOT NULL,
    branch_name          VARCHAR(128) NOT NULL,
    discord_channel_id   BIGINT       NOT NULL
);