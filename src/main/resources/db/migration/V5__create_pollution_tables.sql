-- 海洋污染物表
CREATE TABLE ocean_pollutant (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL COMMENT '污染物名称',
    category VARCHAR(50) NOT NULL COMMENT '污染物类别',
    percentage DECIMAL(5,2) COMMENT '占比(%)',
    harm_description TEXT COMMENT '危害描述',
    protection_measures TEXT COMMENT '保护措施',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT(1) DEFAULT 0
);

-- 初始化污染物数据
INSERT INTO ocean_pollutant (name, category, percentage, harm_description, protection_measures) VALUES 
('塑料袋', '塑料制品', 35.50, '可能被海洋生物误食导致死亡；分解过程产生微塑料污染；破坏海洋生态系统', '减少使用一次性塑料制品；参与海滩清理活动；使用环保购物袋'),
('农药残留', '化学污染物', 15.20, '破坏海洋生态系统平衡；危害海洋生物健康；通过食物链危害人类健康', '控制农药使用量；发展有机农业；建立农药使用监管体系'),
('工业废水', '工业污染', 28.30, '含有重金属等有害物质；破坏水质；导致海洋生物死亡；影响渔业发展', '加强工业废水处理；提高环保标准；推广清洁生产技术'),
('生活垃圾', '生活废弃物', 12.50, '污染海水；破坏海滩景观；危害海洋生物生存', '加强垃圾分类；减少一次性用品使用；建立海岸清理机制'),
('石油泄漏', '石油污染', 8.50, '破坏海洋生态系统；导致海洋生物大规模死亡；影响沿海旅游业', '加强油轮安全管理；完善应急处理机制；发展清洁能源'),
('微塑料', '塑料制品', 18.80, '被海洋生物误食；进入食物链；长期存在难以分解', '限制微塑料使用；研发可降解材料；加强污水处理'),
('重金属', '工业污染', 16.40, '累积性污染；危害海洋生物健康；通过食物链影响人类', '严控工业排放；加强水质监测；发展环保技术'),
('养殖污染', '水产养殖', 9.80, '导致水体富营养化；影响水质；破坏生态平衡', '发展生态养殖；控制养殖密度；处理养殖废水'),
('船舶废弃物', '航运污染', 6.90, '污染海水；危害海洋生物；影响海域环境', '加强船舶管理；建立废弃物处理系统；推广环保船舶'),
('放射性物质', '特殊污染物', 2.10, '长期危害；影响生物基因；污染范围广', '严控核废料处理；加强监测；建立应急机制'); 
