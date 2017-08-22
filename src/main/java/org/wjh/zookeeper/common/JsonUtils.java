package org.wjh.zookeeper.common;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.codehaus.jackson.map.ObjectMapper;

public class JsonUtils {
    private static ObjectMapper mapper = null;

    @SuppressWarnings("deprecation")
    private synchronized static ObjectMapper getObjectMapper() {
        if (mapper == null) {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS");
            mapper = new ObjectMapper();
            // JSON串和对象字段不对应时候,只解析有对应的映射
            mapper.getDeserializationConfig().set(Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            mapper.getSerializationConfig().setDateFormat(df);
            mapper.getDeserializationConfig().setDateFormat(df);
        }
        return mapper;
    }

    /**
     * From JSON string to bean.
     * 
     * @param <T>
     * @param json
     * @param clazz
     * @return
     */
    public static <T> T toBean(String json, Class<T> clazz) {
        if (!StringUtils.isEmpty(json)) {
            ObjectMapper mapper = getObjectMapper();
            try {
                return mapper.readValue(json, clazz);
            } catch (Exception e) {
            }
        }
        return null;
    }

    /**
     * To JSON string.
     * 
     * @param object
     * @return
     */
    public static String toString(Object object) {
        ObjectMapper mapper = getObjectMapper();
        return toString(object, mapper);
    }

    /**
     * To JSON string base on given mapper.
     * 
     * @param object
     * @param mapper
     * @return
     */
    public static String toString(Object object, ObjectMapper mapper) {
        try {
            return mapper.writeValueAsString(object);
        } catch (Exception e) {
            return "";
        }
    }
}
