package ar.edu.utn.frc.tup.lciii.clients;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeamMatchResultDto {
    private Long id;
    private int points;
    private int tries;
    private int yellow_cards;
    private int red_cards;
}
