package com.wenyi.userHandle.utils.jdbcutils;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

/**
 * @project: tomcat-wenyi;
 * @package: com.wenyi.userHandle.utils.jdbcutils;
 * @author: Administrator;
 * @date: 2021/4/14 17:46;
 * @Description: 用于将结果处理成一个该对象的集合 因为需要一个list集合 所以实现接口返回值应该为List<T>
 *     多数据list 处理器
 */
public class MyBeanListHandler<T> implements MyResultSetHandler<List<T>> {
    //因为我们需要创建T对象来返回list<T> 因此需要路径反射来创建对象
    private Class<T> clazz;

    /**
     * 获取我们需要的对象反射路径 来创建对象
     * @param clazz
     */
    public MyBeanListHandler(Class<T> clazz){
        this.clazz = clazz;
    }

    /**
     * 将查询到的 多条数据分别 循环执行 找到元数据 根据元数据 找到属性 与值
     * 再将值赋值到对象 最后将对象加入list集合中返回
     * @param resultSet 获取的查询结果值
     * @return
     * @throws Exception
     */
    @Override
    public List<T> handler(ResultSet resultSet) throws Exception {
        List<T> list = new ArrayList<>();
        //判断结果集是否存在
        if (resultSet.next()){
            //之前是循环实体对象T  这里使用元数据 因为实体与元数据一一对应所以使用起来无区别
            //获取元数据 元数据数量
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            T obj = clazz.newInstance();
            //循环找值 并填充到obj对象
            for (int i = 0; i < columnCount; i++) {
                //取出列名 并使用rs找到值 与找到属性对象好赋值
                String columnName = metaData.getColumnName(i);
                Object value = resultSet.getObject(columnName);
                Field field = clazz.getField(columnName);
                //开启权限 并 赋值给obj对象
                field.setAccessible(true);
                field.set(obj,value);
            }
            list.add(obj);
        }
        return list;
    }
}
