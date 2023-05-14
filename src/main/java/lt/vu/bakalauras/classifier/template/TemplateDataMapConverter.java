package lt.vu.bakalauras.classifier.template;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lt.vu.bakalauras.model.TemplateData;

import java.io.IOException;
import java.util.Map;

@Converter(autoApply = true)
public class TemplateDataMapConverter implements AttributeConverter<Map<String, TemplateData>, String> {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(Map<String, TemplateData> map) {
        try {
            return objectMapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting map to JSON", e);
        }
    }

    @Override
    public Map<String, TemplateData> convertToEntityAttribute(String json) {
        try {
            TypeReference<Map<String, TemplateData>> typeRef = new TypeReference<Map<String, TemplateData>>() {};
            return objectMapper.readValue(json, typeRef);
        } catch (IOException e) {
            throw new RuntimeException("Error converting JSON to map", e);
        }
    }
}
