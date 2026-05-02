package team.local.reservation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Slf4j
@Service
public class ReservationService {
    public Reservation getReservationById(Long id) {
        log.info("Getting reservation...");

        return new Reservation(
                UUID.randomUUID(),
                id,
                12312L,
                123213L,
                LocalDate.now(),
                LocalDate.now().plusDays(7),
                ReservationStatus.PENDING
        );
    }
}
