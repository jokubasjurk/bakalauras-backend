package lt.vu.bakalauras.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import lt.vu.bakalauras.model.TemplateData;

import java.io.IOException;
import java.util.Map;

public class JsonUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String toJson(Map<String, TemplateData> map) {
        try {
            return objectMapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting map to JSON", e);
        }
    }

    public static Map<String, TemplateData> fromJson(String json) {
        try {
            TypeReference<Map<String, TemplateData>> typeRef = new TypeReference<Map<String, TemplateData>>() {};
            return objectMapper.readValue(json, typeRef);
        } catch (IOException e) {
            throw new RuntimeException("Error converting JSON to map", e);
        }
    }
}
