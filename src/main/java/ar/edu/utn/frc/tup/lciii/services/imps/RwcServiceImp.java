package ar.edu.utn.frc.tup.lciii.services.imps;

import ar.edu.utn.frc.tup.lciii.clients.MatchDto;
import ar.edu.utn.frc.tup.lciii.clients.Rwc2023Client;
import ar.edu.utn.frc.tup.lciii.clients.TeamMatchResultDto;
import ar.edu.utn.frc.tup.lciii.dtos.response.PoolStandingResponse;
import ar.edu.utn.frc.tup.lciii.dtos.response.TeamResponse;
import ar.edu.utn.frc.tup.lciii.services.RwcService;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class RwcServiceImp implements RwcService {
    @Autowired
    Rwc2023Client rwcClient;
    @Autowired
    ModelMapper modelMapper;

    private final char[] POOLS = {'A', 'B', 'C', 'D'};

    @Override
    public List<PoolStandingResponse> getAllStandingPools() {
        List<PoolStandingResponse> response = new ArrayList<>();

        for (char p: POOLS) {
            response.add(PoolStandingResponse.builder()
                            .poolId(p)
                            .teams(getTeamsFromAPool(p))
                    .build());
        }

         for (PoolStandingResponse pS: response) {
            MatchDto[] matches = rwcClient.getMatchesByPool(pS.getPoolId()).getBody();
            assert matches != null;
             createAPoolStandingResponse(pS, matches);
        }

        return response;
    }

    public void createAPoolStandingResponse(PoolStandingResponse pS, MatchDto[] matches){
        for (MatchDto m : matches) {
            TeamResponse team1OnResponse = pS.getTeams().get(
                    findTeamFromAList(pS.getTeams(), m.getTeams()[0].getId()));
            TeamResponse team2OnResponse = pS.getTeams().get(
                    findTeamFromAList(pS.getTeams(), m.getTeams()[1].getId()));
            TeamMatchResultDto resultTeam1 = m.getTeams()[0];
            TeamMatchResultDto resultTeam2 = m.getTeams()[1];

            if(resultTeam1.getPoints() > resultTeam2.getPoints()) {
                calculateNoDrawMatchPoints(team1OnResponse, team2OnResponse, resultTeam1, resultTeam2,
                        team1OnResponse.getLosses(), resultTeam2.getPoints());
            }
            if(resultTeam1.getPoints() < resultTeam2.getPoints()){
                calculateNoDrawMatchPoints(team2OnResponse, team1OnResponse, resultTeam2, resultTeam1,
                        team1OnResponse.getLosses(), resultTeam2.getPoints());
            }
            else{
                team1OnResponse.setDraws(team1OnResponse.getDraws() + 1);
                team1OnResponse.setTriesMade(team1OnResponse.getTriesMade() + resultTeam1.getTries());
                team1OnResponse.setPointsFor(team1OnResponse.getPointsFor() + resultTeam1.getPoints());
                team1OnResponse.setPointsAgaints(team1OnResponse.getPointsAgaints() + resultTeam2.getPoints());

                team2OnResponse.setDraws(team2OnResponse.getDraws() + 1);
                team2OnResponse.setTriesMade(team2OnResponse.getTriesMade() + resultTeam2.getTries());
                team2OnResponse.setPointsFor(team2OnResponse.getPointsFor() + resultTeam2.getPoints());
                team2OnResponse.setPointsAgaints(team2OnResponse.getPointsAgaints() + resultTeam1.getPoints());
                calculatePoolBonusPointsForTriesInAmatch(team1OnResponse, team2OnResponse,
                        resultTeam1, resultTeam2);
                setYellowAndRedCards(team1OnResponse, team2OnResponse, resultTeam1, resultTeam2);
            }
            team1OnResponse.setMatchesPlayed(team1OnResponse.getMatchesPlayed() + 1);
            team2OnResponse.setMatchesPlayed(team2OnResponse.getMatchesPlayed() + 1);
            calculatePointsForATeam(team1OnResponse);
            calculatePointsForATeam(team2OnResponse);
            calculatePointsDifferentialForATeam(team1OnResponse);
            calculatePointsDifferentialForATeam(team2OnResponse);
        }
    }

    public void calculateNoDrawMatchPoints(TeamResponse winnerTeam, TeamResponse losserTeam,
                                            TeamMatchResultDto winnerResult, TeamMatchResultDto losserResult,
                                            int losses, int points) {
        winnerTeam.setWins(winnerTeam.getWins() + 1);
        winnerTeam.setTriesMade(winnerTeam.getTriesMade() + winnerResult.getTries());
        winnerTeam.setPointsFor(winnerTeam.getPointsFor() + winnerResult.getPoints());
        winnerTeam.setPointsAgaints(winnerTeam.getPointsAgaints() + losserResult.getPoints());


        losserTeam.setLosses(losses + 1);
        losserTeam.setPointsFor(winnerTeam.getPointsFor() + winnerResult.getPoints());
        losserTeam.setPointsAgaints(winnerTeam.getPointsAgaints() + points);
        if(winnerResult.getPoints() - losserResult.getPoints() <= 7) {
            losserTeam.setBonusPoints(losserTeam.getBonusPoints() + 1);
        }
        calculatePoolBonusPointsForTriesInAmatch(winnerTeam, losserTeam, winnerResult, losserResult);
        setYellowAndRedCards(winnerTeam, losserTeam, winnerResult, losserResult);
    }

    public void calculatePoolBonusPointsForTriesInAmatch(TeamResponse team1, TeamResponse team2,
                                                          TeamMatchResultDto team1Result,
                                                          TeamMatchResultDto team2Result){
        if(team1Result.getTries() > 4) {
            team1.setBonusPoints(team1.getBonusPoints() + 1);
        }
        if(team2Result.getTries() > 4) {
            team2.setBonusPoints(team2.getBonusPoints() + 1);
        }
    }

    public void setYellowAndRedCards(TeamResponse team1, TeamResponse team2,
                                                  TeamMatchResultDto team1Result,
                                                      TeamMatchResultDto team2Result){
        team1.setTotalRedCards(team1.getTotalRedCards() + team1Result.getYellow_cards());
        team1.setTotalYellowCards(team1.getTotalYellowCards() + team1Result.getYellow_cards());
        team2.setTotalRedCards(team2.getTotalRedCards() + team2Result.getYellow_cards());
        team2.setTotalYellowCards(team2.getTotalYellowCards() + team2Result.getYellow_cards());
    }

    public void calculatePointsForATeam(TeamResponse team){
        team.setPoints(team.getPoints() + team.getWins()*4 + team.getDraws()*2 + team.getBonusPoints());
    }
    public void calculatePointsDifferentialForATeam(TeamResponse team){
        team.setPointsDifferential(team.getPointsFor() - team.getPointsAgaints());
    }


    @Override
    public PoolStandingResponse getStantigPoolsByPoolId(char poolId) {
        MatchDto[] matches = rwcClient.getMatchesByPool(poolId).getBody();
        PoolStandingResponse response = PoolStandingResponse.builder()
                .poolId(poolId)
                .teams(getTeamsFromAPool(poolId))
                .build();

        createAPoolStandingResponse(response, matches);

        return response;
    }

    public List<TeamResponse> getTeamsFromAPool(char poolId){
        TeamResponse[] response = modelMapper.map(rwcClient.getTeamsByPool(poolId).getBody(),
                TeamResponse[].class);
        List<TeamResponse> result =
                new ArrayList<>(Arrays.asList(response));

        return result;
    }

    public int findTeamFromAList(List<TeamResponse> t, Long teamId){
        int result = -1;
        for(int i = 0; i < t.size(); i++){
            if(t.get(i).getTeamId() == teamId) {
                result = i;
                break;
            }
        }
        if(result == -1) throw new EntityNotFoundException();

        return result;
    }
}
