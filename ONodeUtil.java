package webapp.utils;

import noear.snacks.OMapper;
import noear.snacks.ONode;
import noear.snacks.exts.Fun1;


import javax.jws.Oneway;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;

/**
 * Date: 2018/11/1
 * Time: 17:22
 * Author: Yukai
 * Description: 对snake中的ONode的功能进行扩展
 **/
public class ONodeUtil {

    //接收ONode数组类型，并且返回对应的基本数据类型的包装类的集合
    public static List<Long> toList_long(ONode option) {
        return toList(option, n -> n.getLong());
    }

    public static <T> List<T> toList(ONode node, Fun1<T, ONode> fun) {
        List<T> list = new ArrayList<>();
        for (ONode n : node) {
            list.add(fun.run(n));
        }

        return list;
    }

    public static ONode tryLoadList(List list) throws IllegalAccessException {

        ONode arr = new ONode().asArray();

        for (Object o : list) {
            // ONode node = OMapper.map(o);
            ONode node = tryLoadObj(o);
            arr.add(node);
        }

        return arr;
    }

    public static ONode tryLoadObj(Object o) throws IllegalAccessException {

        ONode node = new ONode().asObject();
        Field[] fields = o.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            Object value = field.get(o);
            if (value instanceof Integer) {
                node.set(field.getName(), (int) field.get(o));
            } else if (value instanceof String) {
                node.set(field.getName(), (String) field.get(o));
            } else if (value instanceof Long) {
                node.set(field.getName(), (long) field.get(o));
            }
           /* if(value instanceof Short){
                node.set(field.getName(),(short)field.get(o));
            }*/
            else if (value instanceof Double) {
                node.set(field.getName(), (double) field.get(o));
            } else if (value instanceof Boolean) {
                node.set(field.getName(), (boolean) field.get(o));
            } else if (value instanceof Date) {
                node.set(field.getName(), (Date) field.get(o));
            } else {
                //not the primary type
                Object oo = field.get(o);
                String ooName = field.getName();
                ONode ooNode = tryLoadObj(oo);
                node.set(ooName, ooNode);
            }
        }
        return node;
    }

    public static ONode buildSubONodeList(ONode arr, String... attrNames) {

        ONode rst = new ONode().asArray();

        if (!arr.isArray()) {
            return rst;
        }

        for (ONode from : arr) {
            ONode to = new ONode();
            for (String attrName : attrNames) {
                if (from.contains(attrName)) {
                    to.set(attrName, from.get(attrName));
                }
            }
            rst.add(to);
        }

        return rst;
    }

    // it would be much much better if user could manipulate the inner list of the ONode target
    // best way : ONode.class has the method with the silimar function of `removeIf`
    // second way : use the method below
    // third way : ....reflection
   /* public static ONode removeIf(ONode target, Predicate criteria) {

    }
    */

}
