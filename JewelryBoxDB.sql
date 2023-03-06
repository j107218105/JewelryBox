/**
 * 手風琴
 */
CREATE SEQUENCE"Accordion_id_seq"MAXVALUE 32767 CYCLE;
CREATE TABLE"Accordion"(
	"id"int2 DEFAULT"nextval"('"Accordion_id_seq"'::"regclass")PRIMARY KEY,
	"name"varchar NOT NULL UNIQUE,
	"sort"int2 NOT NULL UNIQUE
);
ALTER SEQUENCE"Accordion_id_seq"OWNED BY"Accordion"."id";
COMMENT ON COLUMN"Accordion"."id"IS'主鍵(長度16位元)';
COMMENT ON COLUMN"Accordion"."name"IS'手風琴名稱';
COMMENT ON COLUMN"Accordion"."sort"IS'排序';
COMMENT ON TABLE"Accordion"IS'手風琴';
-- 
INSERT INTO"Accordion"("name","sort")VALUES(E'會員','1'),(E'活動','2'),(E'權限','3'),(E'其它','4');

/**
 * 方式
 */
CREATE SEQUENCE"Method_id_seq"MAXVALUE 32767 CYCLE;
CREATE TABLE"Method"(
	"id"int2 DEFAULT"nextval"('"Method_id_seq"'::"regclass")PRIMARY KEY,
	"name"varchar NOT NULL UNIQUE
);
ALTER SEQUENCE"Method_id_seq"OWNED BY"Method"."id";
COMMENT ON COLUMN"Method"."id"IS'主鍵(長度16位元)';
COMMENT ON COLUMN"Method"."name"IS'方式名稱';
COMMENT ON TABLE"Method"IS'方式';
-- 
INSERT INTO"Method"("name")VALUES(E'DELETE'),(E'GET'),(E'POST'),(E'PUT');

/**
 * 途徑
 */
CREATE SEQUENCE"Mapping_id_seq"MAXVALUE 32767 CYCLE;
CREATE TABLE"Mapping"(
	"id"int2 DEFAULT"nextval"('"Mapping_id_seq"'::"regclass")PRIMARY KEY,
	"accordion"int2 REFERENCES"Accordion"("id")ON DELETE RESTRICT ON UPDATE CASCADE,
	"sort"int2,
	"title"varchar,
	"uri"varchar,
	"method"int2 NOT NULL REFERENCES"Method"("id")ON DELETE RESTRICT ON UPDATE CASCADE,
	"pattern"varchar NOT NULL,
	"description"text NOT NULL,
	UNIQUE("accordion","sort"),
	UNIQUE("uri","method")
);
ALTER SEQUENCE"Mapping_id_seq"OWNED BY"Mapping"."id";
COMMENT ON COLUMN"Mapping"."id"IS'主鍵(長度16位元)';
COMMENT ON COLUMN"Mapping"."accordion"IS'外鍵:手風琴';
COMMENT ON COLUMN"Mapping"."sort"IS'排序';
COMMENT ON COLUMN"Mapping"."title"IS'標題';
COMMENT ON COLUMN"Mapping"."uri"IS'路徑';
COMMENT ON COLUMN"Mapping"."method"IS'外鍵:方式';
COMMENT ON COLUMN"Mapping"."pattern"IS'規則';
COMMENT ON COLUMN"Mapping"."description"IS'描述';
COMMENT ON TABLE"Mapping"IS'途徑';
-- 
/*
INSERT INTO"Mapping"("accordion","title","uri","method","sort","description")VALUES
(NULL,E'後臺',E'^\/$','2','1',E'後臺的首頁'),
-- 會員
('1',E'會員',E'\/everyone\/','2','1',E'會員列表'),
('1',E'待審核',E'\/unproven\/','2','2',E'待審核'),
(NULL,E'待審核的活動',E'\/unproven\/(\d)+\.asp','2','3',E'待審核的活動'),
(NULL,E'待審核的活動',E'\/unproven\/(\d)+\.asp','3','4',E'待審核的活動'),
('1',E'待撥款',E'\/transfer\/','2','5',E'待撥款'),
(NULL,E'撥款',E'\/transfer\/(\d)+\.asp','2','6',E'撥款'),
(NULL,E'撥款',E'\/transfer\/(\d)+\.asp','3','7',E'撥款'),
('1',E'黑名單',E'\/blacklist\/','2','8',E'黑名單'),
('1',E'被封鎖',E'\/blockade\/','2','9',E'被封鎖'),
('1',NULL,E'\/blockade\/(\d)+.json','3','10',E'解除封鎖'),
('1',E'反饋',E'\/feedback\.asp','2','11',E'反饋'),
('1',E'反饋 &#187; 回覆',E'\/feedback\/(d)+\.asp','2','12',E'反饋 &#187; 回覆'),
('1',E'反饋 &#187; 回覆',E'\/feedback\/(d)+\.asp','3','13',E'反饋 &#187; 回覆'),
-- 活動
('2',E'活動',E'\/activity\/','2','1',E'活動列表'),
('2',E'未開始',E'\/activity\/available\.asp','2','2',E'未開始'),
('2',E'進行中',E'\/activity\/underway\.asp','2','3',E'進行中'),
('2',E'已結束',E'\/activity\/finished\.asp','2','4',E'已結束'),
('2',E'新增',E'\/activity\/add\.asp','2','5',E'新增'),
(NULL,E'修改',E'\/activity\/(\d)\.asp','2','6',E'修改'),
(NULL,E'修改',E'\/activity\/(\d)\.asp','3','7',E'修改'),
-- 其它
('4',E'其它 &#187; 行政區劃',E'\/district\/','2','1',E'其它 &#187; 行政區劃'),
('4',E'其它 &#187; 金融機構',E'\/financialInstitution\/','2','2',E'其它 &#187; 金融機構');
*/
INSERT INTO"Mapping"("accordion","sort","title","uri","method","pattern","description")VALUES
(NULL,NULL,E'後臺',NULL,'2',E'^\\/$',E'後臺的首頁'),
-- 會員
('1','1',E'會員一覽',E'/someone/','2',E'^\\/someone\\/$',E'會員列表'),
('1',NULL,NULL,NULL,'3',E'^\\/someone\\/(\\d)+\\.json$',E'開放或封鎖某會員'),
('1',NULL,E'活動列表',NULL,'2',E'^\\/someone\\/(\\d)+\\/activity\\/$',E'某會員的活動列表'),
('1',NULL,NULL,NULL,'3',E'^\\/someone\\/(\\d)+\\/agent\\.json$',E'授權或停權管理者'),
-- 活動
('2','1',E'活動一覽',E'/activity/','2',E'^\\/activity\\/$',E'活動列表'),
('2',NULL,E'{活動}',NULL,'2',E'^\\/activity\\/(\\d)+\\.asp$',E'修改活動頁面'),
('2',NULL,E'{活動}',NULL,'3',E'^\\/activity\\/(\\d)+\\.asp$',E'修改活動作業'),
('2','2',E'活動 &#187; 新增',E'/activity/add.asp','2',E'^\\/activity\\/add\\.asp$',E'新增活動頁面'),
('2',NULL,E'活動 &#187; 新增',NULL,'3',E'^\\/activity\\/add\\.asp$',E'新增活動作業'),
('2',NULL,NULL,NULL,'3',E'^\\/activity\\/guesstimate\\.json$',E'估算符合活動需求的人數'),
('2',NULL,NULL,NULL,'3',E'^\\/activity\\/(\\d)+\\/notify\\.json$',E'發送簡訊給候選者'),
-- 權限
('3','1',E'管理者一覽',E'/agent/','2',E'^\\/agent\\/$',E'管理者列表'),
('3',NULL,E'{管理者}',NULL,'2',E'^\\/agent\\/(\\d)+\\.asp$',E'某管理者的權限列表'),
('3',NULL,NULL,NULL,'3',E'^\\/agent\\/(\\d)+\\/mapping\\/(\\d)+\\.json$',E'賦予或撤除管理者的某權限'),
('3','2',E'手風琴一覽',E'/accordion/','2',E'^\\/accordion\\/$',E'手風琴列表'),
('3','3',E'手風琴 &#187; 新增',E'/accordion/add.asp','2',E'^\\/accordion\\/add\\.asp$',E'新增手風琴頁面'),
('3',NUll,E'手風琴 &#187; 新增',NULL,'3',E'^\\/accordion\\/add\\.asp$',E'新增手風琴作業'),
('3',NULL,E'{手風琴}',NULL,'2',E'^\\/accordion\\/(\\d)+\\.asp$',E'修改手風琴頁面'),
('3',NULL,E'{手風琴}',NULL,'3',E'^\\/accordion\\/(\\d)+\\.asp$',E'修改手風琴作業'),
('3',NULL,E'{手風琴} &#187; 途徑一覽',NULL,'2',E'^\\/accordion\\/(\\d)+\\/mapping\\/$',E'某手風琴下的途徑列表'),
('3','4',E'途徑 &#187; 新增',E'/mapping/add.asp','2',E'^\\/mapping\\/add\\.asp$',E'新增途徑頁面'),
('3',NULL,E'途徑 &#187; 新增',NULL,'3',E'^\\/mapping\\/add\\.asp$',E'新增途徑作業'),
('3',NULL,E'{途徑}',NULL,'2',E'^\\/mapping\\/(\\d)+\\.asp$',E'修改途徑頁面'),
('3',NULL,E'{途徑}',NULL,'3',E'^\\/mapping\\/(\\d)+\\.asp$',E'修改途徑作業'),
('3',NULL,E'途徑一覽',NULL,'2',E'^\\/mapping\\/$',E'途徑列表');
--('accordion','sort',E'title',E'uri','method',E'pattern',E'description')

/**
 * 職業
 */
CREATE SEQUENCE"Occupation_id_seq"MAXVALUE 32767 CYCLE;
CREATE TABLE"Occupation"(
	"id"int2 DEFAULT"nextval"('"Occupation_id_seq"'::"regclass")PRIMARY KEY,
	"name"varchar NOT NULL UNIQUE
);
ALTER SEQUENCE"Occupation_id_seq"OWNED BY"Occupation"."id";
COMMENT ON COLUMN"Occupation"."id"IS'主鍵(長度16位元)';
COMMENT ON COLUMN"Occupation"."name"IS'職業名稱';
COMMENT ON TABLE"Occupation"IS'職業';
-- 
INSERT INTO"Occupation"("name")VALUES(E'工業'),(E'自由業'),(E'服務業'),(E'軍人'),(E'公務員'),(E'教育業'),(E'家管'),(E'經商'),(E'學生');

/**
 * 金融機構
 */
CREATE SEQUENCE"FinancialInstitution_id_seq"MAXVALUE 32767 CYCLE;
CREATE TABLE"FinancialInstitution"(
	"id"int2 DEFAULT"nextval"('"FinancialInstitution_id_seq"'::"regclass")PRIMARY KEY,
	"code"varchar NOT NULL,
	"name"varchar NOT NULL UNIQUE,
	"shown"bool NOT NULL DEFAULT'0'
);
ALTER SEQUENCE"FinancialInstitution_id_seq"OWNED BY"FinancialInstitution"."id";
COMMENT ON COLUMN"FinancialInstitution"."id"IS'主鍵(長度16位元)';
COMMENT ON COLUMN"FinancialInstitution"."code"IS'代碼';
COMMENT ON COLUMN"FinancialInstitution"."name"IS'金融機構名稱';
COMMENT ON COLUMN"FinancialInstitution"."shown"IS'顯示與否';
COMMENT ON TABLE"FinancialInstitution"IS'金融機構';
-- 
INSERT INTO"FinancialInstitution"("code","name")VALUES(E'004',E'臺灣銀行'),(E'005',E'土地銀行'),(E'006',E'合庫商銀'),(E'007',E'第一銀行'),(E'008',E'華南銀行'),(E'009',E'彰化銀行'),(E'011',E'上海銀行'),(E'012',E'台北富邦'),(E'013',E'國泰世華'),(E'016',E'高雄銀行'),(E'017',E'兆豐商銀'),(E'018',E'農業金庫'),(E'021',E'花旗(台灣)銀行'),(E'022',E'美國銀行'),(E'025',E'首都銀行'),(E'039',E'澳盛(台灣)銀行'),(E'040',E'中華開發'),(E'050',E'臺灣企銀'),(E'052',E'渣打商銀'),(E'053',E'台中銀行'),(E'054',E'京城商銀'),(E'072',E'德意志銀行'),(E'075',E'東亞銀行'),(E'081',E'匯豐(台灣)銀行'),(E'101',E'瑞興銀行'),(E'102',E'華泰銀行'),(E'103',E'臺灣新光商銀'),(E'104',E'台北五信'),(E'108',E'陽信銀行'),(E'114',E'基隆一信'),(E'115',E'基隆二信'),(E'118',E'板信銀行'),(E'119',E'淡水一信'),(E'120',E'淡水信合社'),(E'124',E'宜蘭信合社'),(E'127',E'桃園信合社'),(E'130',E'新竹一信'),(E'132',E'新竹三信'),(E'146',E'台中二信'),(E'147',E'三信銀行'),(E'158',E'彰化一信'),(E'161',E'彰化五信'),(E'162',E'彰化六信'),(E'163',E'彰化十信'),(E'165',E'鹿港信合社'),(E'178',E'嘉義三信'),(E'188',E'台南三信'),(E'204',E'高雄三信'),(E'215',E'花蓮一信'),(E'216',E'花蓮二信'),(E'222',E'澎湖一信'),(E'223',E'澎湖二信'),(E'224',E'金門信合社');
INSERT INTO"FinancialInstitution"("code","name","shown")VALUES(E'700',E'中華郵政','1');
INSERT INTO"FinancialInstitution"("code","name")VALUES(E'803',E'聯邦銀行'),(E'805',E'遠東銀行'),(E'806',E'元大銀行'),(E'807',E'永豐銀行');
INSERT INTO"FinancialInstitution"("code","name","shown")VALUES(E'808',E'玉山銀行','1');
INSERT INTO"FinancialInstitution"("code","name")VALUES(E'809',E'凱基銀行'),(E'810',E'星展(台灣)銀行'),(E'812',E'台新銀行'),(E'814',E'大眾銀行'),(E'815',E'日盛銀行'),(E'816',E'安泰銀行'),(E'822',E'中國信託');

/**
 * 行政區
 */
CREATE SEQUENCE"District_id_seq"MAXVALUE 32767 CYCLE;
CREATE TABLE"District"(
	"id"int2 DEFAULT"nextval"('"District_id_seq"'::"regclass")PRIMARY KEY,
	"name"varchar NOT NULL UNIQUE
);
ALTER SEQUENCE"District_id_seq"OWNED BY"District"."id";
COMMENT ON COLUMN"District"."id"IS'主鍵(長度16位元)';
COMMENT ON COLUMN"District"."name"IS'行政區名稱';
COMMENT ON TABLE"District"IS'行政區';
-- 
INSERT INTO"District"("name")VALUES(E'基隆市'),(E'臺北市'),(E'新北市'),(E'桃園市'),(E'新竹市'),(E'新竹縣'),(E'苗栗縣'),(E'臺中市'),(E'彰化縣'),(E'南投縣'),(E'雲林縣'),(E'嘉義市'),(E'嘉義縣'),(E'臺南市'),(E'高雄市'),(E'屏東市'),(E'臺東縣'),(E'花蓮縣'),(E'宜蘭縣'),(E'澎湖縣'),(E'金門縣'),(E'連江縣');

/**
 * 會員
 */
CREATE SEQUENCE"Someone_id_seq"MAXVALUE 2147483647 CYCLE;
CREATE TABLE"Someone"(
	"id"int4 DEFAULT"nextval"('"Someone_id_seq"'::"regclass")PRIMARY KEY,
	"facebookId"varchar UNIQUE,
	"googleID"varchar UNIQUE,
	"birthday"date NOT NULL,
	"gender"bool NOT NULL,
	"name"varchar NOT NULL,
	"email"varchar NOT NULL UNIQUE,
	"denied"bool NOT NULL DEFAULT'0',
	"cellular"varchar UNIQUE,
	"occupation"int2 REFERENCES"Occupation"("id")ON DELETE RESTRICT ON UPDATE CASCADE,
	"financialInstitution"int2 REFERENCES"FinancialInstitution"("id")ON DELETE RESTRICT ON UPDATE CASCADE,
	"financialAccountHolder"varchar,
	"financialAccountNumber"varchar,
	"district"int2 REFERENCES"District"("id")ON DELETE RESTRICT ON UPDATE CASCADE,
	UNIQUE("financialInstitution","financialAccountNumber")
);
ALTER SEQUENCE"Someone_id_seq"OWNED BY"Someone"."id";
COMMENT ON COLUMN"Someone"."id"IS'主鍵(長度32位元)';
COMMENT ON COLUMN"Someone"."facebookId"IS'Facebook帳號';
COMMENT ON COLUMN"Someone"."googleID"IS'Google帳號';
COMMENT ON COLUMN"Someone"."birthday"IS'生日';
COMMENT ON COLUMN"Someone"."gender"IS'性別';
COMMENT ON COLUMN"Someone"."name"IS'全名';
COMMENT ON COLUMN"Someone"."email"IS'電子郵件';
COMMENT ON COLUMN"Someone"."denied"IS'封鎖';
COMMENT ON COLUMN"Someone"."cellular"IS'手機';
COMMENT ON COLUMN"Someone"."occupation"IS'外鍵：職業';
COMMENT ON COLUMN"Someone"."financialInstitution"IS'外鍵：金融機構';
COMMENT ON COLUMN"Someone"."financialAccountHolder"IS'金融帳戶戶名';
COMMENT ON COLUMN"Someone"."financialAccountNumber"IS'金融帳戶號碼';
COMMENT ON COLUMN"Someone"."district"IS'外鍵：行政區';
COMMENT ON TABLE"Someone"IS'會員';
-- 
INSERT INTO"Someone"("facebookId","googleID","birthday","gender","name","email")VALUES
(E'495028320637268',E'108242987103370290694','1976-01-13','1',E'高科技黑手',E'jewelrybox.tw@gmail.com'),
(E'100000419207522',NULL,'1984-03-08','0',E'鄭惠文',E'amycheng0308@gmail.com'),
(E'100000273190144',NULL,'1992-09-08','1',E'許伯維',E'xu_bao_he_azlzypm_xu_bao_he@gmail.com'),
(E'1375315626112981',NULL,'1992-09-08','1',E'許寶盒',E'xu_bao_he_azlzypm_xu_bao_he@tfbnw.net'),
(E'1375249659452939',NULL,'1984-03-08','0',E'鄭寶盒',E'zheng_bao_he_mjgupdf_zheng_bao_he@tfbnw.net'),
(E'1385033031806509',NULL,'1976-01-13','1',E'林寶盒',E'lin_bao_he_knwzsva_lin_bao_he@tfbnw.net'),
(E'1377630472550422',NULL,'2000-02-07','0',E'李利貞',E'li_li_zhen_elpdnga_li_li_zhen@tfbnw.net'),
(E'1385146031797749',NULL,'2000-11-28','1',E'鬼谷子',E'wang_xu_aovzxuv_wang_xu@tfbnw.net'),
(E'1380825598897996',NULL,'1927-06-18','1',E'張作霖',E'zhang_zuo_lin_sdwjjfi_zhang_zuo_lin@tfbnw.net'),
(E'1377780149202432',NULL,'1959-04-27','1',E'劉少奇',E'liu_shao_qi_uvyfait_liu_shao_qi@tfbnw.net');

/**
 * 權限
 *
 * crontab	re-order sequence by agent,mapping
 */
CREATE SEQUENCE"Privilege_id_seq"MAXVALUE 32767 CYCLE;
CREATE TABLE"Privilege"(
	"id"int2 DEFAULT"nextval"('"Privilege_id_seq"'::"regclass")PRIMARY KEY,
	"mapping"int2 NOT NULL REFERENCES"Mapping"("id")ON DELETE RESTRICT ON UPDATE CASCADE,
	"agent"int4 NOT NULL REFERENCES"Someone"("id")ON DELETE RESTRICT ON UPDATE CASCADE,
	UNIQUE("mapping","agent")
);
ALTER SEQUENCE"Privilege_id_seq"OWNED BY"Privilege"."id";
COMMENT ON COLUMN"Privilege"."id"IS'主鍵(長度16位元)';
COMMENT ON COLUMN"Privilege"."mapping"IS'外鍵:途徑';
COMMENT ON COLUMN"Privilege"."agent"IS'外鍵:管理者';
COMMENT ON TABLE"Privilege"IS'權限';
-- 
INSERT INTO"Privilege"("mapping","agent")VALUES
('1','1'),('2','1'),('3','1'),('4','1'),('5','1'),('6','1'),('7','1'),('8','1'),('9','1'),('10','1'),('11','1'),('12','1'),('13','1'),('14','1'),('15','1'),('16','1'),('17','1'),('18','1'),('19','1'),('20','1'),('21','1'),('22','1'),('23','1'),('24','1'),('25','1'),('26','1'),
('1','2'),
('1','3');

/**
 * 活動
 */
CREATE SEQUENCE"Activity_id_seq"MAXVALUE 2147483647 CYCLE;
CREATE TABLE"Activity"(
	"id"int4 DEFAULT"nextval"('"Activity_id_seq"'::"regclass")PRIMARY KEY,
	"name"varchar NOT NULL,
	"headcount"int4 NOT NULL,
	"score"int2 NOT NULL,
	"since"timestamp NOT NULL,
	"until"timestamp NOT NULL,
	"notified"bool NOT NULL DEFAULT'0',
	"age"int2,
	"gender"bool,
	"occupation"int2 REFERENCES"Occupation"("id")ON DELETE RESTRICT ON UPDATE CASCADE,
	"district"int2 REFERENCES"District"("id")ON DELETE RESTRICT ON UPDATE CASCADE
);
ALTER SEQUENCE"Activity_id_seq"OWNED BY"Activity"."id";
COMMENT ON COLUMN"Activity"."id"IS'主鍵(長度32位元)';
COMMENT ON COLUMN"Activity"."name"IS'活動名稱';
COMMENT ON COLUMN"Activity"."headcount"IS'人數';
COMMENT ON COLUMN"Activity"."score"IS'點數';
COMMENT ON COLUMN"Activity"."since"IS'開始時戳';
COMMENT ON COLUMN"Activity"."until"IS'結束時戳';
COMMENT ON COLUMN"Activity"."notified"IS'通知';
COMMENT ON COLUMN"Activity"."age"IS'年齡需求';
COMMENT ON COLUMN"Activity"."gender"IS'性別需求';
COMMENT ON COLUMN"Activity"."occupation"IS'外鍵:職業需求';
COMMENT ON COLUMN"Activity"."district"IS'外鍵:行政區需求';
COMMENT ON TABLE"Activity"IS'活動';
-- 
INSERT INTO"Activity"("name","headcount","score","since","until","notified")VALUES
(E'已結束','6','6',"now"()+'-2w',"now"()+'-1w',FALSE),
(E'進行中還沒回報','7','7',"now"()+'-1w',"now"()+'1w',FALSE),
(E'進行中且已回報','8','8',"now"()+'-3d',"now"()+'3d',TRUE),
(E'未開始已確認','14','14',"now"()+'3d',"now"()+'1w',FALSE),
(E'未開始未確認','12','12',"now"()+'1w',"now"()+'2w',FALSE);

/**
 * 反饋
 */
CREATE SEQUENCE"Feedback_id_seq"MAXVALUE 9223372036854775807 CYCLE;
CREATE TABLE"Feedback"(
	"id"int8 DEFAULT"nextval"('"Feedback_id_seq"'::"regclass")PRIMARY KEY,
	"someone"int4 NOT NULL REFERENCES"Someone"("id")ON DELETE RESTRICT ON UPDATE CASCADE,
	"context"text NOT NULL,
	"posted"timestamp NOT NULL DEFAULT"now"()
);
ALTER SEQUENCE"Feedback_id_seq"OWNED BY"Feedback"."id";
COMMENT ON COLUMN"Feedback"."id"IS'主鍵(長度64位元)';
COMMENT ON COLUMN"Feedback"."someone"IS'外鍵:會員';
COMMENT ON COLUMN"Feedback"."context"IS'內文';
COMMENT ON COLUMN"Feedback"."posted"IS'發文時戳';
COMMENT ON TABLE"Feedback"IS'反饋';

/**
 * 候選者
 */
CREATE SEQUENCE"Candidate_id_seq"MAXVALUE 9223372036854775807 CYCLE;
CREATE TABLE"Candidate"(
	"id"int8 DEFAULT"nextval"('"Candidate_id_seq"'::"regclass")PRIMARY KEY,
	"activity"int4 NOT NULL REFERENCES"Activity"("id")ON DELETE RESTRICT ON UPDATE CASCADE,
	"someone"int4 NOT NULL REFERENCES"Someone"("id")ON DELETE RESTRICT ON UPDATE CASCADE,
	UNIQUE("activity","someone"),
	"replied"bool,
	"repliedBy"timestamp,
	"notified"bool,
	"proof"bytea,
	"provedBy"timestamp,
	"agent"int4 REFERENCES"Someone"("id")ON DELETE RESTRICT ON UPDATE CASCADE
);
ALTER SEQUENCE"Candidate_id_seq"OWNED BY"Candidate"."id";
COMMENT ON COLUMN"Candidate"."id"IS'主鍵(長度64位元)';
COMMENT ON COLUMN"Candidate"."activity"IS'外鍵:活動';
COMMENT ON COLUMN"Candidate"."someone"IS'外鍵:會員';
COMMENT ON COLUMN"Candidate"."replied"IS'回覆';
COMMENT ON COLUMN"Candidate"."repliedBy"IS'回覆時戳';
COMMENT ON COLUMN"Candidate"."notified"IS'通知';
COMMENT ON COLUMN"Candidate"."proof"IS'證明(擷圖)';
COMMENT ON COLUMN"Candidate"."provedBy"IS'證明時戳';
COMMENT ON COLUMN"Candidate"."agent"IS'外鍵:審核者';
COMMENT ON TABLE"Candidate"IS'候選者';
-- 
INSERT INTO"Candidate"("activity","someone")VALUES
('1','3'),
('2','2'),
('3','1'),
('4','3'),
('5','2');

/**
 * 歷程
 */
CREATE SEQUENCE"History_id_seq"MAXVALUE 9223372036854775807 CYCLE;
CREATE TABLE"History"(
	"id"int8 DEFAULT"nextval"('"History_id_seq"'::"regclass")PRIMARY KEY,
	"activity"int4 REFERENCES"Activity"("id")ON DELETE RESTRICT ON UPDATE CASCADE,
	"someone"int4 NOT NULL REFERENCES"Someone"("id")ON DELETE RESTRICT ON UPDATE CASCADE,
	UNIQUE("activity","someone"),
	"amount"int4,
	"score"int2 NOT NULL DEFAULT'0',
	"financialInstitution"int2 REFERENCES"FinancialInstitution"("id")ON DELETE RESTRICT ON UPDATE CASCADE,
	"financialAccountHolder"varchar,
	"financialAccountNumber"varchar,
	"occurred"timestamp DEFAULT"now"(),
	"agent"int4 REFERENCES"Someone"("id")ON DELETE RESTRICT ON UPDATE CASCADE,
	UNIQUE("financialInstitution","financialAccountNumber")
);
ALTER SEQUENCE"History_id_seq"OWNED BY"History"."id";
COMMENT ON COLUMN"History"."id"IS'主鍵(長度64位元)';
COMMENT ON COLUMN"History"."activity"IS'外鍵:活動';
COMMENT ON COLUMN"History"."someone"IS'外鍵:會員';
COMMENT ON COLUMN"History"."amount"IS'金額';
COMMENT ON COLUMN"History"."score"IS'點數異動';
COMMENT ON COLUMN"History"."financialInstitution"IS'外鍵：金融機構';
COMMENT ON COLUMN"History"."financialAccountHolder"IS'金融帳戶戶名';
COMMENT ON COLUMN"History"."financialAccountNumber"IS'金融帳戶號碼';
COMMENT ON COLUMN"History"."occurred"IS'發生時戳';
COMMENT ON COLUMN"History"."agent"IS'外鍵:匯款人';
COMMENT ON TABLE"History"IS'歷程';
