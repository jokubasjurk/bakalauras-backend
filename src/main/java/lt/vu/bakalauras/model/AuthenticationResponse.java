package lt.vu.bakalauras.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthenticationResponse {
    private boolean success;
}
