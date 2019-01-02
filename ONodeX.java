package webapp.utils.Ext;

import noear.snacks.ONode;
import noear.snacks.ONodeType;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Date: 2018/12/7
 * Time: 13:25
 * Author: Yukai
 * Description: extends the functions of the ONode, makes everything clearer and simpler
 * 大幅度增加ONode的功能
 **/
public class ONodeX extends ONode {

    public boolean removeIf(Predicate<? super ONode> p) {

        if (this._type != ONodeType.Array) {
            return false;
        }
        return this._array.elements.removeIf(p);
    }


    public ONodeX toSubONodeXList(String...attrNames){

        if(!this.isArray()){
            return this;
        }

        ONodeX rst = new ONodeX().asArray();

        for (ONode from : this._array.elements) {
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


    public static ONodeX tryLoadObj(Object o) throws IllegalAccessException {
        // define the type of the ONode
        ONodeX node = new ONodeX().asObject();

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


  public static ONodeX tryLoadList(List list) throws IllegalAccessException {

        ONodeX arr = new ONodeX().asArray();

        for (Object o : list) {
            // ONode node = OMapper.map(o);
            ONodeX node = tryLoadObj(o);
            arr.add(node);
        }

        return arr;
    }

    @Override
    public ONodeX asObject(){
        this.tryInitObject();
        return this;
    }

    @Override
    public ONodeX asArray(){
        this.tryInitArray();
        return this;
    }


}
