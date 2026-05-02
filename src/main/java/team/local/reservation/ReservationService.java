package team.local.reservation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.management.RuntimeErrorException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;

@Slf4j
@Service
public class ReservationService {

    private final Map<Long, Reservation> reservationsMap = Map.of(
            1L, new Reservation(
                    UUID.randomUUID(),
                        1L,
                        12312L,
                        123213L,
                        LocalDate.now(),
                        LocalDate.now().plusDays(7),
                        ReservationStatus.PENDING
                ),
            2L, new Reservation(
                    UUID.randomUUID(),
                    2L,
                    12312L,
                    123213L,
                    LocalDate.now(),
                    LocalDate.now().plusDays(7),
                    ReservationStatus.PENDING
                )
    );

    public Reservation getReservationById(Long id) {
        log.info("Getting reservation...");

        if (!reservationsMap.containsKey(id)) {
            throw new NoSuchElementException("Not found reservation.");
        }
        return reservationsMap.get(id);
    }

    public List<Reservation> getAllReservations() {
        log.info("Searching all reservations...");

        return reservationsMap.values().stream().toList();
    }
}
