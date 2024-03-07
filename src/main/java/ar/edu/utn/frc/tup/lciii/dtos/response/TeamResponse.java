package ar.edu.utn.frc.tup.lciii.dtos.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeamResponse {
    @JsonProperty("id")
    private Long teamId;
    @JsonProperty("team_name")
    private String teamName;
    private String country;
    @JsonProperty("matches_played")
    private int MatchesPlayed;
    private int wins;
    private int draws;
    private int losses;
    @JsonProperty("points_for")
    private int pointsFor;
    @JsonProperty("points_againts")
    private int pointsAgaints;
    @JsonProperty("points_differential")
    private int pointsDifferential;
    @JsonProperty("tries_made")
    private int triesMade;
    @JsonProperty("bonus points")
    private int BonusPoints;
    private int points;
    @JsonProperty("total_yellow_cards")
    private int TotalYellowCards;
    @JsonProperty("total_red_cards")
    private int TotalRedCards;
}
