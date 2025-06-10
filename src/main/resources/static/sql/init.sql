CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(15) UNIQUE NOT NULL,
    pass_hash VARCHAR NOT NULL,
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    last_login TIMESTAMPTZ NOT NULL
);
CREATE TABLE workouts (
    id BIGSERIAL PRIMARY KEY,
    author_id BIGSERIAL REFERENCES users(id) NOT NULL,
    date TIMESTAMPTZ NOT NULL,
    is_done BOOLEAN NOT NULL,
    type VARCHAR,
    duration INT,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);
CREATE TABLE goals (
    id BIGSERIAL PRIMARY KEY,
    author_id BIGSERIAL REFERENCES users(id) NOT NULL,
    text TEXT NOT NULL,
    is_done BOOLEAN NOT NULL,
    deadline TIMESTAMPTZ,
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);
CREATE TABLE user_roles(
    id BIGSERIAL PRIMARY KEY,
    author_id BIGSERIAL REFERENCES users(id) NOT NULL,
    role VARCHAR(10) NOT NULL,
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);
INSERT INTO users (username, pass_hash, last_login)
VALUES ('testuser', 'hashed_password_123', CURRENT_TIMESTAMP);

INSERT INTO workouts (author_id, date, is_done, type, duration)
VALUES (1, '2025-06-10 10:00:00+00', FALSE, 'cardio', 45);

INSERT INTO goals (author_id, text, is_done, deadline)
VALUES (1, 'Run 5km in under 30 mins', FALSE, '2025-06-30 23:59:59+00');

INSERT INTO user_roles (author_id, role)
VALUES (1, 'USER');


SELECT * FROM workouts WHERE author_id = 1 ORDER BY date DESC;

SELECT * FROM goals WHERE author_id = 1 AND is_done = TRUE;

SELECT * FROM workouts
WHERE author_id = 1 AND is_done = FALSE AND type = 'cardio';

SELECT
    COUNT(*) AS total_workouts,
    SUM(duration) AS total_minutes,
    AVG(duration) AS avg_duration
FROM workouts
WHERE author_id = 1;


CREATE OR REPLACE FUNCTION count_pending_goals(user_id BIGINT)
    RETURNS INT AS $$
DECLARE
    pending_count INT;
BEGIN
    SELECT COUNT(*) INTO pending_count
    FROM goals
    WHERE author_id = user_id AND is_done = FALSE;
    RETURN pending_count;
END;
$$ LANGUAGE plpgsql;


CREATE OR REPLACE PROCEDURE mark_goal_done(goal_id BIGINT)
    LANGUAGE plpgsql
AS $$
BEGIN
    UPDATE goals SET is_done = TRUE WHERE id = goal_id;
END;
$$;


CREATE OR REPLACE FUNCTION update_last_login()
    RETURNS TRIGGER AS $$
BEGIN
    NEW.last_login := CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_update_last_login
    BEFORE UPDATE ON users
    FOR EACH ROW
    WHEN (OLD.last_login <> NEW.last_login)
EXECUTE FUNCTION update_last_login();


CREATE OR REPLACE FUNCTION get_active_users_with_roles()
    RETURNS TABLE (
                      user_id BIGINT,
                      username VARCHAR,
                      role VARCHAR,
                      last_login TIMESTAMPTZ
                  ) AS $$
BEGIN
    RETURN QUERY
        SELECT u.id, u.username, r.role, u.last_login
        FROM users u
                 JOIN user_roles r ON u.id = r.author_id
        WHERE u.last_login > CURRENT_TIMESTAMP - INTERVAL '30 days';
END;
$$ LANGUAGE plpgsql;


CREATE INDEX idx_workouts_author_id ON workouts(author_id);
CREATE INDEX idx_goals_author_id ON goals(author_id);
CREATE INDEX idx_user_roles_author_id ON user_roles(author_id);
