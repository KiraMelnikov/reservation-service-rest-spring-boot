package team.local.reservation.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import team.local.reservation.models.ReservationEntity;

import java.util.UUID;

public interface ReservationRepository extends JpaRepository<ReservationEntity, UUID> {
}
