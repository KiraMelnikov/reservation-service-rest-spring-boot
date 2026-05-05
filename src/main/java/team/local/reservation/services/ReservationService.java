package team.local.reservation.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import team.local.reservation.Reservation;
import team.local.reservation.ReservationStatus;
import team.local.reservation.models.ReservationEntity;
import team.local.reservation.repositories.ReservationRepository;
import team.local.reservation.wrappers.ReservationWrapper;
import team.local.reservation.dto.ReservationDto;

import java.util.*;

@Slf4j
@Service
@AllArgsConstructor
public class ReservationService {

    private final Map<UUID, Reservation> reservationsMap;

    private final ReservationWrapper reservationWrapper;

    private final ReservationRepository repository;

    public Reservation getReservationById(UUID id) {
        log.info("Getting reservation...");

        ReservationEntity reservationEntity = repository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Not found reservation by uuid: " + id)
        );
        return toDomainReservation(reservationEntity);
    }

    public List<Reservation> getAllReservations() {
        log.info("Searching all reservations...");

        List<ReservationEntity> allEntities = repository.findAll();
        return allEntities.stream().map(this::toDomainReservation).toList();
    }

    public Reservation createReservation(ReservationDto data) {
        log.info("Creating new reservation...");

        var reservation = reservationWrapper.wrap(data);
        if (reservation.uuid() != null) {
            throw new IllegalArgumentException("UUID should not be present.");
        }

        ReservationEntity reservationEntity = new ReservationEntity(
                null,
                reservation.userId(),
                reservation.roomId(),
                reservation.startDate(),
                reservation.endDate(),
                reservation.status(),
                null,
                null
        );
        var savedEntity = repository.save(reservationEntity);

        return toDomainReservation(savedEntity);
    }

    public Reservation updateReservation(UUID uuid, ReservationDto data) {
        log.info("Updating reservation: {}", uuid);

        var newReservation = reservationWrapper.wrap(data);
        var currentReservationEntity = repository.findById(uuid).orElseThrow(
                () -> new EntityNotFoundException("Not found reservation by id: " + uuid)
        );

        if (currentReservationEntity.getStatus() != ReservationStatus.PENDING) {
            throw new IllegalStateException(
                    "Cannot modify reservation status=" + currentReservationEntity.getStatus()
            );
        }
        var updatedReservationEntity = new ReservationEntity(
                currentReservationEntity.getUuid(),
                newReservation.userId(),
                newReservation.roomId(),
                newReservation.startDate(),
                newReservation.endDate(),
                ReservationStatus.PENDING,
                currentReservationEntity.getCreatedAt(),
                currentReservationEntity.getLastUpdatedAt()
        );

        repository.save(updatedReservationEntity);

        return toDomainReservation(updatedReservationEntity);
    }

    public void deleteReservation(UUID uuid) {
        log.info("Deleting reservation: {}", uuid);

        var currentReservationEntity = repository.findById(uuid).orElseThrow(
                () -> new EntityNotFoundException("Not found reservation by id: " + uuid)
        );
        var deletedReservation = new ReservationEntity(
                currentReservationEntity.getUuid(),
                currentReservationEntity.getUserId(),
                currentReservationEntity.getRoomId(),
                currentReservationEntity.getStartDate(),
                currentReservationEntity.getEndDate(),
                ReservationStatus.CANCELLED,
                currentReservationEntity.getCreatedAt(),
                currentReservationEntity.getLastUpdatedAt()
        );
        repository.save(deletedReservation);
    }

    public Reservation approveReservation(UUID uuid) {
        log.info("Approving reservation: {}", uuid);

        var reservationEntity = repository.findById(uuid).orElseThrow(
                () -> new EntityNotFoundException("Not found reservation by: " + uuid)
        );
        var isConflict = isReservationConflict(reservationEntity);
        if (isConflict) {
            throw new IllegalStateException("Cannot approve reservation cause has a conflict.");
        }
        reservationEntity.setStatus(ReservationStatus.APPROVED);
        repository.save(reservationEntity);

        return toDomainReservation(reservationEntity);
    }

    private boolean isReservationConflict(ReservationEntity reservationEntity) {
        List<ReservationEntity> allEntities = repository.findAll();

        for (ReservationEntity existingReservationEntity : allEntities) {
            if (reservationEntity.getUuid().equals(existingReservationEntity.getUuid())) {
                continue;
            }
            if (!reservationEntity.getRoomId().equals(existingReservationEntity.getRoomId())) {
                continue;
            }
            if (!existingReservationEntity.getStatus().equals(ReservationStatus.APPROVED)) {
                continue;
            }
            if (
                    reservationEntity.getStartDate().isBefore(existingReservationEntity.getEndDate())
                    && existingReservationEntity.getStartDate().isBefore(reservationEntity.getEndDate())
            ) {
                return true;
            }
        }
        return false;
    }

    private Reservation toDomainReservation(ReservationEntity reservationEntity) {
        return new Reservation(
                reservationEntity.getUuid(),
                reservationEntity.getUserId(),
                reservationEntity.getRoomId(),
                reservationEntity.getStartDate(),
                reservationEntity.getEndDate(),
                reservationEntity.getStatus()
        );
    }
}
