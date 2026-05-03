package team.local.reservation.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import team.local.reservation.Reservation;
import team.local.reservation.ReservationStatus;
import team.local.reservation.wrappers.ReservationWrapper;
import team.local.reservation.dto.ReservationDto;

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

    public Reservation updateReservation(UUID uuid, ReservationDto data) {
        log.info("Updating reservation: {}", uuid);

        var newReservation = reservationWrapper.wrap(data);
        if (!reservationsMap.containsKey(uuid)) {
            throw new NoSuchElementException("Not found object: " + uuid);
        }
        var currentReservation = reservationsMap.get(uuid);
        if (currentReservation.status() != ReservationStatus.PENDING) {
            throw new IllegalStateException("Cannot modify reservation status=" + currentReservation.status());
        }
        reservationsMap.put(currentReservation.uuid(), newReservation);

        return newReservation;
    }

    public void deleteReservation(UUID uuid) {
        log.info("Deleting reservation: {}", uuid);

        if (!reservationsMap.containsKey(uuid)) {
            throw new NoSuchElementException("Not found object: " + uuid);
        }
        reservationsMap.remove(uuid);
    }
}
