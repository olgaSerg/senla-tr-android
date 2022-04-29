SELECT id, title, body FROM post;

SELECT post.id AS postId, title, body, userId, email FROM user JOIN post ON user.id == post.userId;

SELECT id, firstName || ' ' || lastName AS fullName FROM user;

SELECT count(*) FROM user;

SELECT count(*) FROM post WHERE rate > 3;

SELECT MAX(rate) AS maxRate FROM post;

SELECT AVG(rate) AS avgRate FROM post WHERE rate > 2;

SELECT comment.id, comment.text, post.title AS postTitle, author.email AS authorEmail, commenter.firstName || ' ' || commenter.lastName AS commenterFullName
FROM post
  JOIN user AS author ON author.id == post.userId
  JOIN comment ON post.id == comment.postId
  JOIN user AS commenter ON commenter.id == comment.userId
WHERE rate > 2;

UPDATE user SET firstName = 'Hacked' WHERE email LIKE '%e%';

UPDATE post SET rate = rate + 1 WHERE title LIKE '%you%' AND body NOT LIKE '%for%';

INSERT INTO comment(postId, userId, text) VALUES (3, 2, 'Nihao )');

DELETE FROM user WHERE email NOT LIKE '%e%';
