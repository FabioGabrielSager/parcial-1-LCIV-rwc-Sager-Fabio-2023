package ar.edu.utn.frc.tup.lciii.dtos.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PoolStandingResponse {
    @JsonProperty("pool_id")
    private char poolId;
    private List<TeamResponse> teams;
}
