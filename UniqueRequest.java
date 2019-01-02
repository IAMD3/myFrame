package webapp.utils.Ext.annotations;

import java.lang.annotation.*;

/**
 * Date: 2018/12/20
 * Time: 10:00
 * Author: Yukai
 **/
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueRequest {
    /**
     * indicate that if the duplicate requests are not
     * allowed in a  short period of the time
     */
}
