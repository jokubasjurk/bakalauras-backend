package lt.vu.bakalauras.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FlightTime {
    private String keyPair;
    private String type;
    private Double flightTime;
}
