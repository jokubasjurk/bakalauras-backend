package lt.vu.bakalauras.model;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class AuthenticationResponse {
    private Map<String, Boolean> classifierResultMapping;
    private boolean success;
}
