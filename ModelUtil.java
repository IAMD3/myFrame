package com.d3.simplegenerator.utils;


import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Date: 2018/11/27
 * Time: 11:31
 * Author: Yukai
 * Description :  List<Person> -> List<Integer> 将包装类集合的成员数据提取并且组建成新的List
 * 理论上支撑任何基本数据类型的成员属性
 * <p>
 * e.g.    TestPo po1 = new TestPo(111,"name1");
 * TestPo po2 = new TestPo(222, "name2");
 * List<TestPo> list = new ArrayList<>();
 * list.add(po1);
 * list.add(po2);
 * List<Integer> keyList= ModelUtil.toKeyListInteger(list,"keyId");
 **/

public class ModelUtil {

    public static List<Integer> toKeyListInteger(List objList, String keyName) throws Exception {
        return getKeyList((b, c) -> b.getInt(c), objList, keyName);
    }

    public static List<Long> toKeyListLong(List objList, String keyName) throws Exception {
        return getKeyList((b, c) -> b.getLong(c), objList, keyName);
    }

    public static List<String> toKeyListString(List objList, String keyName) throws Exception {
        return getKeyList((b, c) -> String.valueOf(b.get(c)), objList, keyName);
    }

    public static List toKeyListObj(List objList, String keyName) throws Exception {
        return getKeyList((b, c) -> b.get(c), objList, keyName);
    }

    public static <T> List<T> getKeyList(FunX<T, Field, Object> fun, List objList, String keyName) throws Exception {
        List<T> list = new ArrayList<>();
        Class clazz = null;
        Field field = null;
        for (Object o : objList) {
            if (clazz == null) {
                clazz = o.getClass();
                field = clazz.getDeclaredField(keyName);
                field.setAccessible(true);
            }
            list.add(fun.run(field, o));
        }
        return list;
    }


    //ChainedList -- powerfull structural list, optimised by Yukai
    public static <T> ChainedList<T> toChainList(List<T> list) {
        ChainedList<T> chainedList = new ChainedList<>();
        chainedList.addAll(list);
        return chainedList;
    }

    //*************************************************************************************************

 /*   public static Map<Integer, String> IntStringModelListToMap(List target, String keyName, String valueName) throws Exception {
        return modelListToMap(target, keyName, valueName, (kField, vField, o) -> {
            Map<Integer, String> map = new HashMap<>();
            Integer key = kField.getInt(o);
            String value = String.valueOf(vField.get(o));
            map.put(key, value);
            return map;
        });
    }


    public static Map<String, String> pureStringModelListToMap(List target, String keyName, String valueName) throws Exception {
        return modelListToMap(target, keyName, valueName, (kField, vField, o) -> {
            Map<String, String> map = new HashMap<>();
            String key = String.valueOf(kField.get(o));
            String value = String.valueOf(vField.get(o));
            map.put(key, value);
            return map;
        });
    }
*/

    public static Map objListToMap(List target, String keyName, String valueName) throws NoSuchFieldException, IllegalAccessException {

        HashMap map = new HashMap();

        for (Object o : target) {
            Class<?> clazz = o.getClass();
            Field keyField = clazz.getDeclaredField(keyName);
            Field valueField = clazz.getDeclaredField(valueName);
            keyField.setAccessible(true);
            valueField.setAccessible(true);
            map.put(keyField.get(o), valueField.get(o));
        }
        return map;
    }


   /* public static <K, V> Map<K, V> modelListToMap(List target, String keyName, String valueName, FunXX<Map<K, V>, Field, Field, Object> typeFunx) throws Exception {

        Map<K, V> map = new HashMap<>();

        for (Object o : target) {
            Class<?> clazz = o.getClass();
            Field keyField = clazz.getDeclaredField(keyName);
            Field valueField = clazz.getDeclaredField(valueName);
            keyField.setAccessible(true);
            valueField.setAccessible(true);
            map.putAll(typeFunx.run(keyField, valueField, o));
        }
        return map;
    }*/


    @FunctionalInterface
    interface FunX<A, B, C> {
        public A run(B b, C c) throws Exception;
    }

    @FunctionalInterface
    interface FunXX<A, B, C, D> {
        public A run(B b, C c, D d) throws Exception;
    }

    @FunctionalInterface
    interface FunS<A, B> {
        A run(B b);
    }


 /*   public static List<Integer> toKeyListInteger(String keyName, List objList) throws Exception {

        List<Integer> list = new ArrayList<>();
        Class clazz = null;

        for (Object obj : objList) {

            if (clazz == null) {
                clazz = obj.getClass();
            }

            Field keyField = clazz.getDeclaredField(keyName);
            keyField.setAccessible(true);

            list.add(getKey((b, c) -> b.getInt(c), keyField, obj));
        }

        return list;

    }*/

/*    // T : keyType
    private static <T> T getKey(FunX<T, Field, Object> fun, Field field, Object obj) throws Exception {
        return fun.run(field, obj);
    }*/

}
