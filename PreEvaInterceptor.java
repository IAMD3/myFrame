package webapp.utils.Ext.interceptors;

import com.sun.org.apache.bcel.internal.generic.NEW;
import noear.snacks.ONode;
import noear.water.utils.LockUtil;
import org.noear.solonboot.protocol.XContext;
import org.noear.solonboot.protocol.XHandler;
import org.noear.solonboot.uapi.rock.RockApiParams;
import org.noear.solonboot.uapi.rock.RockApiResult;
import webapp.Config;
import webapp.controller.apis.UApiBase;
import webapp.utils.Ext.ONodeX;
import webapp.utils.Ext.annotations.UniqueRequest;
import webapp.utils.StringUtil;

import java.lang.annotation.Annotation;

/**
 * Date: 2018/12/20
 * Time: 10:16
 * Author: Yukai
 **/
public class PreEvaInterceptor implements XHandler {
    @Override
    public void handle(XContext context) throws Exception {

        RockApiParams params = context.attr("params", new RockApiParams());
        ONode requestParams = params.data;

        String path_upper = context.pathAsUpper();
        String cmdName = path_upper.split("/")[2];
        String qualifiedName = classNameLocate(cmdName, "webapp.controller.apis");

        try {
            Class clazz = Class.forName(qualifiedName);
            Annotation ann = clazz.getAnnotation(UniqueRequest.class);
            if (ann != null) {
                // has such annotion xxDDDD
                String g_lkey = requestParams.get("g_lkey").getString();

                if (!StringUtil.isNullOrEmpty(g_lkey) && !LockUtil.isUnique(Config.group_name, "SABER_LKEY_" + g_lkey)) {
                    RockApiResult result = new RockApiResult();
                    result.code = 211;
                    result.message = "重复的请求";
                    context.attrSet("result", result);
                    context.setHandled(true);
                }
            }
        } catch (ClassNotFoundException e) {
            return;
        }
    }

    public String classNameLocate(String pathCmdName, String packageName) {
        //webapp.controller.apis in this case
        String[] chars = pathCmdName.split("\\.");

        StringBuilder sb = new StringBuilder();
        sb.append(packageName);
        sb.append(".CMD");

        for (String chr : chars) {
            sb.append("_");
            sb.append(chr);
        }

        return sb.toString();
    }
}
