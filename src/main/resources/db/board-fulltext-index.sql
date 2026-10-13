-- 게시글 제목·내용 부분 검색용 n-gram Full-Text 인덱스 (BoardRepositoryOverrideImpl#titleCond·contentCond)
-- 스키마를 JPA 가 만들지 않으므로(ddl-auto 꺼짐) 운영 DB 에 한 번 직접 실행한다. JPA @Index 로는 FULLTEXT 를 만들 수 없다.
--
-- 프론트는 제목 또는 내용 중 하나로만 검색해, MATCH 의 컬럼 목록과 같은 인덱스가 컬럼마다 따로 있어야 한다.
-- InnoDB 는 FULLTEXT 인덱스를 한 문장에 하나씩만 만든다.
--
-- 불용어를 끈다: n-gram 파서는 불용어가 들어간 토큰을 통째로 버리는데, 기본 불용어에 'a'·'i' 같은 영어 한 글자가 있어
-- 'walk' 의 'wa'·'al' 이 색인되지 않는다. 불용어 설정은 인덱스를 만들 때 고정된다.
-- 토큰 길이는 서버 기본값 ngram_token_size=2 를 전제로 한다(앱은 2글자 미만 검색어를 LIKE 로 처리).
SET SESSION innodb_ft_enable_stopword = OFF;
ALTER TABLE board ADD FULLTEXT INDEX ft_board_title (title) WITH PARSER ngram;
ALTER TABLE board ADD FULLTEXT INDEX ft_board_content (content) WITH PARSER ngram;
