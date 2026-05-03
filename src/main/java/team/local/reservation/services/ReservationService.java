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
        var res = new Reservation(
                currentReservation.uuid(),
                newReservation.userId(),
                newReservation.roomId(),
                newReservation.startDate(),
                newReservation.endDate(),
                ReservationStatus.PENDING
        );
        reservationsMap.put(currentReservation.uuid(), res);

        return res;
    }

    public void deleteReservation(UUID uuid) {
        log.info("Deleting reservation: {}", uuid);

        if (!reservationsMap.containsKey(uuid)) {
            throw new NoSuchElementException("Not found object: " + uuid);
        }
        var reservation = reservationsMap.get(uuid);
        var approverReservation = new Reservation(
                reservation.uuid(),
                reservation.userId(),
                reservation.roomId(),
                reservation.startDate(),
                reservation.endDate(),
                ReservationStatus.CANCELLED
        );
        reservationsMap.put(reservation.uuid(), approverReservation);
//        reservationsMap.remove(uuid);
    }

    public Reservation approveReservation(UUID uuid) {
        log.info("Approving reservation: {}", uuid);

        if (!reservationsMap.containsKey(uuid)) {
            throw new NoSuchElementException("Not found object: " + uuid);
        }
        var reservation = reservationsMap.get(uuid);
        var isConflict = isReservationConflict(reservation);
        if (isConflict) {
            throw new IllegalStateException("Cannot approve reservation cause has a conflict.");
        }
        var approverReservation = new Reservation(
                reservation.uuid(),
                reservation.userId(),
                reservation.roomId(),
                reservation.startDate(),
                reservation.endDate(),
                ReservationStatus.APPROVED
        );
        reservationsMap.put(reservation.uuid(), approverReservation);
        return approverReservation;
    }

    private boolean isReservationConflict(Reservation reservation) {

        for (Reservation existingReservation : reservationsMap.values()) {
                if (reservation.uuid().equals(existingReservation.uuid())) {
                    continue;
                }
                if (!reservation. roomId() .equals(existingReservation.roomId())) {
                    continue;
                }
                if (existingReservation.status().equals(ReservationStatus.APPROVED)) {
                    continue;
                }
                if (reservation.startDate().isBefore(existingReservation.endDate())
                && (existingReservation.startDate().isBefore(reservation. endDate()))) {
                    return true;
                }
        }
        return false;
    }
}
