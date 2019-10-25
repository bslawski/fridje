CREATE TABLE IF NOT EXISTS people (
  id VARCHAR(16) PRIMARY KEY,
  title VARCHAR(64),
  password CHAR(60),
  facade BLOB
);

CREATE TABLE IF NOT EXISTS messages (
  id VARCHAR(16) PRIMARY KEY,
  sender VARCHAR(16),
  recipient VARCHAR(16),
  sent_at TIMESTAMP,
  received_at TIMESTAMP,
  payload TEXT
);
