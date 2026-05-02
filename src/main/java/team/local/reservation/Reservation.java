package team.local.reservation;

import java.time.LocalDate;
import java.util.UUID;

public record Reservation(
        UUID uuid,
        Long id,
        Long userId,
        Long roomId,
        LocalDate startDate,
        LocalDate endDate,
        ReservationStatus status
) {
}
