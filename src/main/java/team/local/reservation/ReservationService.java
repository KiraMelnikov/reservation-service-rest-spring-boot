package team.local.reservation;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class ReservationService {

    private final Map<UUID, Reservation> reservationsMap;

    private final ReservationWrapper reservationWrapper;

    public Reservation getReservationById(UUID id) {
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

    public Reservation createReservation(ReservationDto data) {
        log.info("Creating new reservation...");

        var reservation = reservationWrapper.wrap(data);
        reservationsMap.put(reservation.uuid(), reservation);

        return reservation;
    }
}
