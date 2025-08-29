INSERT INTO users (userId, authorName, authorUserName) VALUES
    ('11111111-1111-1111-1111-111111111111', 'John Millar', 'johnMillar'),
    ('11111111-1111-1111-1111-111111111112', 'Alice Johnson', 'aliceJohnson'),
    ('11111111-1111-1111-1111-111111111113', 'Bob Smith', 'bobSmith'),
    ('11111111-1111-1111-1111-111111111114', 'Charlie Davis', 'charlieDavis'),
    ('11111111-1111-1111-1111-111111111115', 'Diana Clark', 'dianaClark'),
    ('11111111-1111-1111-1111-111111111116', 'Ethan Brown', 'ethanBrown'),
    ('11111111-1111-1111-1111-111111111117', 'Fiona Lewis', 'fionaLewis'),
    ('11111111-1111-1111-1111-111111111118', 'George Wilson', 'georgeWilson'),
    ('11111111-1111-1111-1111-111111111119', 'Hannah Moore', 'hannahMoore'),
    ('11111111-1111-1111-1111-11111111111a', 'Ian Thomas', 'ianThomas'),
    ('11111111-1111-1111-1111-11111111111b', 'Julia White', 'juliaWhite');

INSERT INTO posts (content, subject, tags, modificationDate, userId) VALUES
    ('This is a real post from a real database', 'Super DB!', 'db,blog,lifestyle', '2018-01-01', '11111111-1111-1111-1111-111111111111');

INSERT INTO subscriptions (authorId, subscriberId) VALUES
    ('11111111-1111-1111-1111-111111111111', '11111111-1111-1111-1111-111111111112'),
    ('11111111-1111-1111-1111-111111111111', '11111111-1111-1111-1111-111111111113'),
    ('11111111-1111-1111-1111-111111111112', '11111111-1111-1111-1111-111111111113');
