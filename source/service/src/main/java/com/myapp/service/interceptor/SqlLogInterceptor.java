//package com.myapp.service.interceptor;
//
//import org.apache.commons.lang.StringUtils;
//import org.apache.ibatis.cache.CacheKey;
//import org.apache.ibatis.executor.Executor;
//import org.apache.ibatis.mapping.BoundSql;
//import org.apache.ibatis.mapping.MappedStatement;
//import org.apache.ibatis.mapping.ParameterMapping;
//import org.apache.ibatis.plugin.*;
//import org.apache.ibatis.reflection.MetaObject;
//import org.apache.ibatis.session.Configuration;
//import org.apache.ibatis.session.ResultHandler;
//import org.apache.ibatis.session.RowBounds;
//import org.apache.ibatis.type.TypeHandlerRegistry;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.util.CollectionUtils;
//
//import java.text.DateFormat;
//import java.util.Date;
//import java.util.List;
//import java.util.Locale;
//import java.util.Properties;
//import java.util.regex.Matcher;
//
//@Intercepts(value = {
//        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
//        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class,
//                RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}),
//        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class,
//                RowBounds.class, ResultHandler.class})})
//public class SqlLogInterceptor implements Interceptor {
//
//    private static final Logger LOGGER = LoggerFactory.getLogger(com.adas.interceptor.SqlLogInterceptor.class);
//
////    private Properties properties;
//
//    @Value("${profile}")
//    private String profile;
//
//    @Override
//    public Object intercept(Invocation invocation) throws Throwable {
//        Object returnValue;
//        returnValue = invocation.proceed();
//
//        if ("pro".equals(profile)) return returnValue;
//
//        try {
//            final Object[] args = invocation.getArgs();
//            MappedStatement ms = (MappedStatement) args[0]; // 获取原始的ms
//            Object parameter = null;
//            if (args.length > 1) { // 获取参数，if语句成立，表示sql语句有参数，参数格式是map形式
//                parameter = args[1];
//            }
//            String sqlId = ms.getId();// 获取到节点的id，即sql语句的id
//            BoundSql boundSql = ms.getBoundSql(parameter);  // BoundSql就是封装MyBatis最终产生的sql类
//            Configuration configuration = ms.getConfiguration();  // 获取节点的配置
//            String msg = getLogMsg(configuration, boundSql, sqlId); // 获取要打印的信息
//            LOGGER.info(msg);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return returnValue;
//    }
//
//    private String getLogMsg(Configuration configuration, BoundSql boundSql, String sqlId) {
//        String sqlShortId = sqlId.substring(StringUtils.lastOrdinalIndexOf(sqlId, ".", 2) + 1);
//        String sql = resolveSql(configuration, boundSql);
//        return sqlShortId + ": " + sql;
//    }
//
//    // 进行?替换
//    private String resolveSql(Configuration configuration, BoundSql boundSql) {
//        Object parameterObject = boundSql.getParameterObject();  // 获取参数
//        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
//        String sql = boundSql.getSql().replaceAll("[\\s]+", " ");  // sql语句中多个空格都用一个空格代替
//        if (!CollectionUtils.isEmpty(parameterMappings) && parameterObject != null) {
//            TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry(); // 获取类型处理器注册器，类型处理器的功能是进行java类型和数据库类型的转换
//            if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) { // 如果根据parameterObject.getClass()可以找到对应的类型，则替换
//                sql = sql.replaceFirst("\\?", Matcher.quoteReplacement(getParameterValue(parameterObject)));
//            } else {
//                MetaObject metaObject = configuration.newMetaObject(parameterObject); // MetaObject主要是封装了originalObject对象，提供了get和set的方法用于获取和设置originalObject的属性值，主要支持对JavaBean、Collection、Map三种类型对象的操作
//                for (ParameterMapping parameterMapping : parameterMappings) {
//                    Object rawValue;
//                    String propertyName = parameterMapping.getProperty();
//                    if (metaObject.hasGetter(propertyName)) {
//                        rawValue = metaObject.getValue(propertyName);
//                    } else if (boundSql.hasAdditionalParameter(propertyName)) {
//                        rawValue = boundSql.getAdditionalParameter(propertyName);  // 该分支是动态sql
//                    } else {
//                        rawValue = "该参数缺失"; // 打印出该参数缺失
//                    }
//                    sql = sql.replaceFirst("\\?", Matcher.quoteReplacement(getParameterValue(rawValue)));
//                }
//            }
//        }
//        return sql;
//    }
//
//    /*
//     * 如果参数是String，则添加单引号；
//     * 如果是日期，则转换为时间格式器并加单引号；
//     * 对参数是null和不是null的情况作了处理
//     * */
//    private String getParameterValue(Object obj) {
//        String value;
//        if (obj instanceof String) {
//            value = "'" + obj.toString() + "'";
//        } else if (obj instanceof Date) {
//            DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.CHINA);
//            value = "'" + formatter.format(new Date()) + "'";
//        } else {
//            if (obj != null) {
//                value = obj.toString();
//            } else {
//                value = "";
//            }
//        }
//        return value;
//    }
//
//    @Override
//    public Object plugin(Object arg0) {
//        return Plugin.wrap(arg0, this);
//    }
//
//    @Override
//    public void setProperties(Properties arg0) {
////        this.properties = arg0;
//    }
//}
