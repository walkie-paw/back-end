package com.WalkiePaw.global.config;

import org.hibernate.boot.model.FunctionContributions;
import org.hibernate.boot.model.FunctionContributor;
import org.hibernate.type.StandardBasicTypes;

/**
 * MySQL Full-Text 검색 {@code MATCH(column) AGAINST(keyword IN BOOLEAN MODE)} 를 JPQL·Querydsl 에서 쓰도록 등록한다.
 *
 * <p>JPQL 에는 MATCH ... AGAINST 문법이 없어 함수로 감싼다. 사용: 조건절에 {@code match_against(컬럼, 검색어)}.
 * 반환 타입을 boolean 으로 등록해 {@code > 0} 같은 비교 없이 조건으로 쓴다(개수만 세는 쿼리에서는 비교식이 붙으면 약 3배 느렸지만,
 * 이 앱의 목록 쿼리(id 정렬 + LIMIT)에서는 차이가 없었다). {@code function('match_against', ...)} 형태로 부르면 Hibernate 가 타입을 몰라 조건으로 받지 않는다.
 * 등록은 META-INF/services/org.hibernate.boot.model.FunctionContributor 로 Hibernate 가 찾는다.
 */
public class MySqlFullTextFunctionContributor implements FunctionContributor {

    public static final String MATCH_AGAINST = "match_against";

    @Override
    public void contributeFunctions(final FunctionContributions functionContributions) {
        functionContributions.getFunctionRegistry().registerPattern(
                MATCH_AGAINST,
                "match(?1) against(?2 in boolean mode)",
                functionContributions.getTypeConfiguration().getBasicTypeRegistry().resolve(StandardBasicTypes.BOOLEAN));
    }
}
