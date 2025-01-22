-- 清空现有数据
DELETE FROM post_comment;
DELETE FROM post;
DELETE FROM user;

-- 插入测试帖子
INSERT INTO post (id, user_id, title, content, view_count, like_count, comment_count, deleted) VALUES 
(1, 1, '保护海洋生态系统的重要性', '海洋生态系统对地球生命至关重要...', 100, 50, 2, 0),
(2, 1, '如何减少海洋塑料污染', '塑料污染已经成为全球性问题...', 80, 30, 1, 0),
(3, 1, '鲸鱼搁浅救助经验分享', '最近参与了一次鲸鱼救助行动...', 150, 70, 3, 0);

-- 插入测试评论
INSERT INTO post_comment (id, post_id, user_id, content, deleted) VALUES 
(1, 1, 1, '完全同意，我们必须采取行动保护海洋！', 0),
(2, 1, 1, '建议可以从减少使用一次性塑料制品开始。', 0),
(3, 2, 1, '可以从源头减少塑料使用开始。', 0),
(4, 3, 1, '感谢分享这么宝贵的经验！', 0),
(5, 3, 1, '希望能有更多人参与到海洋保护中来。', 0),
(6, 3, 1, '建议可以组织更多类似的救助培训。', 0); 