package ar.edu.utn.frc.tup.lciii.services;

import ar.edu.utn.frc.tup.lciii.dtos.response.PoolStandingResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RwcService {
    List<PoolStandingResponse> getAllStandingPools();
    PoolStandingResponse getStantigPoolsByPoolId(char poolId);
}
