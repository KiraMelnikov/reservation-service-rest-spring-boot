package team.local.reservation.models;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SourceType;
import org.hibernate.annotations.UpdateTimestamp;
import team.local.reservation.ReservationStatus;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;


@Setter
@Getter
@Entity
@Table(name = "reservations")
public class ReservationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "uuid")
    private UUID uuid;
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "room_id")
    private Long roomId;
    @Column(name = "start_date")
    private LocalDate startDate;
    @Column(name = "end_date")
    private LocalDate endDate;
    @Column(name = "status")
    private ReservationStatus status;
    @CreationTimestamp(source = SourceType.DB)
    @Column(name = "_created_at")
    private Instant createdAt;
    @UpdateTimestamp(source = SourceType.DB)
    @Column(name = "_last_updated_at")
    private Instant lastUpdatedAt;
}
