package ar.edu.utn.frc.tup.lciii.controllers;

import ar.edu.utn.frc.tup.lciii.dtos.response.PoolStandingResponse;
import ar.edu.utn.frc.tup.lciii.services.RwcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/rwc/2023/")
public class RwcController {
    @Autowired
    RwcService rwcService;

    @GetMapping("/pools")
    public ResponseEntity<List<PoolStandingResponse>> getStandingPools(){
        return ResponseEntity.ok(rwcService.getAllStandingPools());
    }

    @GetMapping("/pools/{id}")
    public ResponseEntity<PoolStandingResponse> getStandingPoolsById(char poolId){
        return ResponseEntity.ok(rwcService.getStantigPoolsByPoolId(poolId));
    }
}
