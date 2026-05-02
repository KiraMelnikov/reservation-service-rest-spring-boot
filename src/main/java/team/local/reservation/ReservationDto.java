package team.local.reservation;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationDto {

    @JsonProperty("userId")
    @NotNull(message = "userId is required")
    private Long userId;

    @NotNull(message = "roomId is required")
    private Long roomId;

    @NotNull(message = "startDate is required")
    private String startDate;

    @NotNull(message = "endDate is required")
    private String endDate;
}
