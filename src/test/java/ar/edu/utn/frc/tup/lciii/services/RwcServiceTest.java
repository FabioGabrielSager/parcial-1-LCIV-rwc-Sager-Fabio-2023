package ar.edu.utn.frc.tup.lciii.services;


import ar.edu.utn.frc.tup.lciii.clients.Rwc2023Client;
import ar.edu.utn.frc.tup.lciii.clients.TeamDto;
import ar.edu.utn.frc.tup.lciii.dtos.response.TeamResponse;
import ar.edu.utn.frc.tup.lciii.services.imps.RwcServiceImp;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class RwcServiceTest {
    @Autowired
    @Mock
    Rwc2023Client rwcClient;
    @Mock
    ModelMapper modelMapper;
    @InjectMocks
    RwcServiceImp rwcServiceImp;

    @Test
    public void getTeamsFromAPoolTest(){
        ResponseEntity<TeamDto[]> teams = new ResponseEntity<>(new TeamDto[]{
                        new TeamDto(1L, "someName", "Argentina"),
                }, HttpStatusCode.valueOf(200));

        when(rwcClient.getTeamsByPool('A')).thenReturn(teams);

        assertEquals(1, this.rwcServiceImp.getTeamsFromAPool('A').size());
        assertEquals("someName", this.rwcServiceImp.getTeamsFromAPool('A').get(0).getTeamName());
    }
}
