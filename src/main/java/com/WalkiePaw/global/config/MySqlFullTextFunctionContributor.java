package com.WalkiePaw.global.config;

import org.hibernate.boot.model.FunctionContributions;
import org.hibernate.boot.model.FunctionContributor;
import org.hibernate.type.StandardBasicTypes;

/**
 * MySQL Full-Text 검색 {@code MATCH(column) AGAINST(keyword IN BOOLEAN MODE)} 를 JPQL·Querydsl 에서 쓰도록 등록한다.
 *
 * <p>JPQL 에는 MATCH ... AGAINST 문법이 없어 함수로 감싼다. 사용: 조건절에 {@code match_against(컬럼, 검색어)}.
 * 반환 타입을 boolean 으로 등록해 {@code > 0} 같은 비교 없이 조건으로 쓴다. 비교식이 붙으면 MySQL 이 관련도 점수를 계산해
 * 흔한 검색어에서 약 3배 느렸다. {@code function('match_against', ...)} 형태로 부르면 Hibernate 가 타입을 몰라 조건으로 받지 않는다.
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
