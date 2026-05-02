package team.local.reservation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ReservationService {
    public String getReservationById(Integer id) {
        log.info("Getting reservation...");
        return "reservation-%s".formatted(id);
    }
}
