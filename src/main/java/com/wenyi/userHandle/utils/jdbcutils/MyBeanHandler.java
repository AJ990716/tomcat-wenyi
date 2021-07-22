package com.wenyi.userHandle.utils.jdbcutils;

import java.lang.reflect.Field;
import java.sql.ResultSet;

/**
 * @project: tomcat-wenyi;
 * @package: com.wenyi.userHandle.utils.jdbcutils;
 * @author: Administrator;
 * @date: 2021/4/14 17:33;
 * @Description: 具体的处理对象 处理单个结果为 T
 * 单数据 处理器
 */
public class MyBeanHandler<T> implements MyResultSetHandler<T>{
    //因为我们需要创建T对象来返回 因此需要路径反射来创建对象
    private Class<T> clazz;

    /**
     * 获取我们需要的对象反射路径 来创建对象
     * @param clazz
     */
    public MyBeanHandler(Class<T> clazz){
        this.clazz = clazz;
    }
    /**
     * 处理单条数据
     * @param resultSet 获取的查询结果值
     * @return
     * @throws Exception
     */
    @Override
    public T handler(ResultSet resultSet) throws Exception {
        T obj = null;
        //结果集是否存在
        if (resultSet.next()){
            //创建实例获取类属性对象
            obj = clazz.newInstance();
            Field[] fields = clazz.getDeclaredFields();
            //遍历属性对象
            int length = fields.length;
            for (int i = 0; i < length; i++) {
                Field field = fields[i];
                //给属性获取权限 并给对象赋值
                field.setAccessible(true);
                Object value = resultSet.getObject(field.getName());
                field.set(obj,value);
            }
        }
        return obj;
    }
}
