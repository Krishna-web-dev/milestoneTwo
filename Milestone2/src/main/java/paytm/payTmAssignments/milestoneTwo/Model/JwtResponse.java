package paytm.payTmAssignments.milestoneTwo.Model;

import lombok.Data;

@Data
public class JwtResponse {

    private String jwtToken;
    public JwtResponse(String jwtToken) {
        this.jwtToken = jwtToken;
    }
}
