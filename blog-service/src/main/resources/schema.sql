DROP TABLE IF EXISTS posts;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS subscriptions;

CREATE TABLE users (
    userId UUID PRIMARY KEY,
    authorName VARCHAR(64) NOT NULL,
    authorUserName VARCHAR(64) NOT NULL
);

CREATE TABLE posts (
    postId INTEGER PRIMARY KEY AUTO_INCREMENT,
    content VARCHAR(256),
    subject VARCHAR(50),
    tags VARCHAR(1024),
    modificationDate DATE,
    userId UUID,
    FOREIGN KEY (userId) REFERENCES users(userId)
);

CREATE TABLE subscriptions (
    authorId UUID NOT NULL,
    subscriberId UUID NOT NULL,
    PRIMARY KEY (authorId, subscriberId),
    FOREIGN KEY (authorId) REFERENCES users(userId),
    FOREIGN KEY (subscriberId) REFERENCES users(userId)
);
