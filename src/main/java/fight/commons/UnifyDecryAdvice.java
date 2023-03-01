package fight.commons;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author XE-CZJ
 * @Date 2022/12/9 14:45
 */
@ControllerAdvice
public class UnifyDecryAdvice extends RequestBodyAdviceAdapter {
    private static final Logger log = LoggerFactory.getLogger(UnifyDecryAdvice.class);



    @Override
    public boolean supports(MethodParameter methodParameter
            , Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return methodParameter.hasMethodAnnotation(NeedDecry.class);
    }


    /**
     * 对含有加密注解的字段进行解密，写入解密字段
     *
     * @param body
     * @param inputMessage
     * @param parameter
     * @param targetType
     * @param converterType
     * @return
     */
    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        //正常入参才操作
        try {
            if (body != null && body instanceof BaseBean) {
                BaseBean param = (BaseBean) body;
                String aid = param.getAid();
                if (StringUtils.isNotBlank(aid)) {
                    Serializable data = param.getData();
                    Field[] declaredFields = data.getClass().getDeclaredFields();
                    //key为目标字段名，value为加密字段值
                    Map<String, String> decryMap = new HashMap<>();
                    Map<String, Field> fieldMap = new HashMap<>();
                    for (Field f : declaredFields) {
                        //解除private保护
                        f.setAccessible(true);
                        fieldMap.put(f.getName(), f);
                        NeedDecry needDecry = f.getAnnotation(NeedDecry.class);
                        //有注解且有目标字段就行，目标字段可以为自身
                        if (f.getType().equals(String.class) && needDecry != null
                                && StringUtils.isNotBlank(needDecry.decryFieldName())) {
                            Object ciphertext = f.get(data);
                            if (ciphertext != null) {
                                decryMap.put(needDecry.decryFieldName(), ciphertext.toString());
                            }
                        }
                    }
                    //如果data里面有aid，则优先取这里的
                    if(fieldMap.containsKey("aid")){
                        Field field = fieldMap.get("aid");
                        Object innerAid = field.get(data);
                        if (field.getType().equals(String.class) && innerAid != null &&
                                StringUtils.isNotBlank(innerAid.toString())) {
                            aid=innerAid.toString();
                        }
                    }
                }

            }
        }catch (RuntimeException e){
            throw e;
        }catch (Exception e) {
            log.error("统一解密异常", e);
            throw new RuntimeException("统一解密异常");
        }
        return body;
    }


}
