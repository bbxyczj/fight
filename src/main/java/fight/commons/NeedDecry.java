package fight.commons;

import java.lang.annotation.*;

/**
 * @Author XE-CZJ
 * @Date 2022/12/9 10:58
 */
@Target({ElementType.FIELD,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NeedDecry {
    /**
     * 解密后存入哪个字段
     * @return
     */
    String decryFieldName() default "";
}
