package fight.utils;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author XE-CZJ
 * @Date 2022/7/5 14:53
 */
public class JavaBeanTookit {
    private static final Logger log = LoggerFactory.getLogger(JavaBeanTookit.class);



    /**
     * 检查object是否为java的基本数据类型/包装类/java.util.Date/java.sql.Date
     *
     * @param propertyType
     * @return
     */
    public static boolean checkObjectIsSysType(Class<?> propertyType) {
        String objType = propertyType.toString();
        if ("byte".equals(objType) || "short".equals(objType) || "int".equals(objType) || "long".equals(objType)
                || "double".equals(objType) || "float".equals(objType) || "boolean".equals(objType)) {
            return true;
        } else if ("class java.lang.Byte".equals(objType) || "class java.lang.Short".equals(objType)
                || "class java.lang.Integer".equals(objType) || "class java.lang.Long".equals(objType)
                || "class java.lang.Double".equals(objType) || "class java.lang.Float".equals(objType)
                || "class java.lang.Boolean".equals(objType) || "class java.lang.String".equals(objType)) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * 返回结果转换为字符串 根据配置规避List
     *
     * @param bean
     * @return
     * @throws Exception
     */
    public static String ignoreListBeanToStr(Object bean, int count) {
        try {
            //递归不能太深
            if (count > 3) {
                return JacksonUtils.toJson(bean);
            }
            Class type = bean.getClass();
            if (checkObjectIsSysType(type) || Map.class.isAssignableFrom(type)
                    || type.isArray() || Collection.class.isAssignableFrom(type)) {
                return JacksonUtils.toJson(bean);
            }
            Map<String, String> propertyMap = new HashMap<>();
            BeanInfo beanInfo = Introspector.getBeanInfo(type);
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (int i = 0; i < propertyDescriptors.length; i++) {
                PropertyDescriptor descriptor = propertyDescriptors[i];
                String propertyName = descriptor.getName();
                if (!propertyName.equals("class")) {
                    //字段处理，list 判断是否需要规避，java自带类型不管，其他自定义类继续递归最多三次
                    Method readMethod = descriptor.getReadMethod();
                    if (readMethod == null && ("java.lang.Boolean".equals(descriptor.getPropertyType().getName())
                            || "boolean".equals(descriptor.getPropertyType().getName()))) {
                        readMethod = type.getMethod("is" + descriptor.getName().substring(0, 1).toUpperCase() + descriptor.getName().substring(1));
                    }
                    if (readMethod == null) {
                        continue;
                    }
                    Object invoke = readMethod.invoke(bean);
                    if (invoke == null) {
                        continue;
                    }
                    Class<?> propertyType = invoke.getClass();
                    //java基础类含时间布尔包装类等
                    if (checkObjectIsSysType(propertyType)) {
                        propertyMap.put(propertyName, String.valueOf(invoke));
                        continue;
                    }
                    if (Map.class.isAssignableFrom(propertyType)) {
                        //map特殊处理
                        propertyMap.put(propertyName, JacksonUtils.toJson(invoke));
                        continue;
                    }
                    //数组
                    if (propertyType.isArray()) {
                        //写一个替换副本
                        Object[] array = (Object[]) invoke;
                        propertyMap.put(propertyName + "Size", String.valueOf(array.length));
                        continue;
                    }
                    //列表
                    if (Collection.class.isAssignableFrom(propertyType)) {
                        //写一个替换副本
                        Collection collection = (Collection) invoke;
                        propertyMap.put(propertyName + "Size", String.valueOf(collection.size()));
                        continue;
                    }
                    //自定义其他类--递归
                    String beanStr = ignoreListBeanToStr(invoke, ++count);
                    propertyMap.put(propertyName, beanStr);
                }
            }
            return JacksonUtils.toJson(propertyMap);
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }
        return null;
    }

    public static String ignoreListBeanToStr(Object bean) {
        String beanStr = ignoreListBeanToStr(bean, 0);
        if(StringUtils.isNotBlank(beanStr)){
            //去除转义
            return beanStr.replace("\\","");
        }
       return "";
    }

    public static void main(String[] args) {
        System.out.println(ignoreListBeanToStr(Lists.newArrayList("123","12431fsd","31bb")));
    }

}
