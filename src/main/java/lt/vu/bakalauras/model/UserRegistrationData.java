package lt.vu.bakalauras.model;

import lombok.Data;

import java.util.List;

@Data
public class UserRegistrationData {
    private String username;
    private String password;
    private String email;
    private String inputType;
    private List<List<FlightTime>> flightTimes;
}
