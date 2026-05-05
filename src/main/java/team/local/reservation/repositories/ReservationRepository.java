package team.local.reservation.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import team.local.reservation.ReservationStatus;
import team.local.reservation.models.ReservationEntity;

import java.util.UUID;

public interface ReservationRepository extends JpaRepository<ReservationEntity, UUID> {

    @Transactional
    @Modifying
    @Query("""
            update ReservationEntity r
            set r.status = :status
            where r.uuid = :uuid
            """)
    void setStatus (
            @Param("uuid") UUID uuid,
            @Param("status") ReservationStatus status
    );
}
