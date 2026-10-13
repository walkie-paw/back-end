package com.WalkiePaw.domain.board.repository;

import org.hibernate.resource.jdbc.spi.StatementInspector;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/** 테스트에서 Hibernate 가 실제로 보낸 SQL 을 확인하려고 기록한다 (hibernate.session_factory.statement_inspector) */
public class SqlRecorder implements StatementInspector {

    private static final List<String> SQLS = new CopyOnWriteArrayList<>();

    @Override
    public String inspect(final String sql) {
        SQLS.add(sql);
        return sql;
    }

    static void clear() {
        SQLS.clear();
    }

    static List<String> recorded() {
        return List.copyOf(SQLS);
    }
}
