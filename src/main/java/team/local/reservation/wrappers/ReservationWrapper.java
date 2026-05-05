package team.local.reservation.wrappers;

import org.springframework.stereotype.Component;
import team.local.reservation.Reservation;
import team.local.reservation.ReservationStatus;
import team.local.reservation.dto.ReservationDto;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class ReservationWrapper {

    public Reservation wrap (ReservationDto dto) {
        return new Reservation(
                null,
                dto.getUserId(),
                dto.getRoomId(),
                LocalDate.parse(dto.getStartDate(), DateTimeFormatter.ISO_DATE),
                LocalDate.parse(dto.getEndDate(), DateTimeFormatter.ISO_DATE),
                ReservationStatus.PENDING
        );
    }
}
