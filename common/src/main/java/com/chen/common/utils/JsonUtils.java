package com.chen.common.utils;


import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Chen
 * @date 2020/11/18
 */
@Slf4j
public class JsonUtils {

    private JsonUtils() {
    }

    private static ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static ObjectMapper getMapper() {
        return mapper;
    }

    public static String obj2jsonstr(Object obj) {
        String jsonStr = null;
        try {
            jsonStr = mapper.writeValueAsString(obj);
        } catch (IOException e) {
            log.error(String.valueOf(e));
        }
        return jsonStr;
    }

    public static Map<String, Object> jsonStr2Map(String jsonStr) {
        Map<String, Object> result = null;
        try {
            JavaType type = mapper.getTypeFactory().constructParametricType(Map.class, String.class, Object.class);
            result = mapper.readValue(jsonStr, type);

        } catch (IOException e) {
            log.error(jsonStr, e);
        }
        return result;
    }

    public static String map2JsonStr(Map<String, Object> map) {
        String jsonStr = null;
        try {
            jsonStr = mapper.writeValueAsString(map);
        } catch (IOException e) {
            log.error(String.valueOf(e));
        }
        return jsonStr;
    }

    public static <T> T jsonStr2Model(String jsonStr, Class<T> clazz) {
        T model = null;
        try {
            model = mapper.readValue(jsonStr, clazz);
        } catch (IOException e) {
            log.error(jsonStr, e);
        }
        return model;
    }

    public static JsonNode getJsonNode(HttpServletRequest request) {
        JsonNode jsonNode = null;
        try {
            jsonNode = JsonUtils.getMapper().readTree(request.getInputStream());
            if (jsonNode.isEmpty()) {
                jsonNode = JsonNodeFactory.instance.objectNode();
            }
        } catch (IOException ex) {
            log.debug(String.valueOf(ex));
        }
        return jsonNode;
    }


    public static JsonNode readJsonNode(JsonNode jsonNode, String filedName) {
        if (jsonNode.hasNonNull(filedName)) {
            return jsonNode.get(filedName);
        }
        return JsonNodeFactory.instance.missingNode();
    }

    public static <T> T readJsonNode(JsonNode jsonNode, Class<T> clazz) {
        T result = null;
        try {
            result = mapper.treeToValue(jsonNode, clazz);
        } catch (Exception ex) {
            log.error(String.valueOf(ex));
        }
        return result;
    }

    public static <T> T readJsonNode(JsonNode jsonNode, String fieldName, Class<T> clazz) {
        T result = null;
        try {
            if (jsonNode.hasNonNull(fieldName)) {
                result = readJsonNode(readJsonNode(jsonNode, fieldName), clazz);
            }
        } catch (Exception ex) {
            log.error(String.valueOf(ex));
        }

        return result;

    }

    public static <T> List<T> readJsonNodes(JsonNode jsonNode, String fieldName, Class<T> clazz) {
        List<T> result = new ArrayList<>();
        try {
            if (jsonNode.hasNonNull(fieldName)) {
                for (JsonNode node : readJsonNode(jsonNode, fieldName)) {
                    result.add(readJsonNode(node, clazz));
                }
            }
        } catch (Exception ex) {
            log.error(String.valueOf(ex));
        }

        return result;
    }
}
