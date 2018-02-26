package com.myapp.service.aop;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DefaultFooService implements FooService {

    private JdbcTemplate jdbcTemplate;

    //使用SimpleJdbcInsert必须设置nullNamePatternMatchesAll=true（默认是false）
    private SimpleJdbcInsert simpleJdbcInsert;

    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("foo")
                .usingGeneratedKeyColumns("id")
//                .usingColumns("name")
        ;
    }

    @Override
    public Foo getFoo(String name, int age) {
        return new Foo(name, age);
    }

    @Override
    public String getById(Long id) {
        Object[] objects = new Object[1];
        objects[0] = id;
        return jdbcTemplate.queryForObject("SELECT name FROM foo WHERE id = ?", objects, String.class);
    }

    @Override
//    @Transactional(readOnly = true)
    public void insertFoo(String name) throws Exception {
//        jdbcTemplate.update("INSERT INTO foo VALUES(NULL, ?)", name);

        Map<String, Object> parameters = new HashMap<>();
//        parameters.put("id", null);
        parameters.put("name", name);
//        simpleJdbcInsert.execute(parameters);
        Number newId = simpleJdbcInsert.executeAndReturnKey(parameters);

        //Spring事务管理回滚机制默认只回滚unchecked异常，不会回滚checked异常，但可通过配置来回滚checked异常。
//        throw new UnsupportedOperationException();
//        throw new IOException();
    }
}
