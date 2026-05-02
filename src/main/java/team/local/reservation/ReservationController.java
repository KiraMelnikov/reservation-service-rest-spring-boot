package team.local.reservation;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;


@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/")
public class ReservationController {

    private final ReservationService reservationService;

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        log.info("Use [GET] /api/v1/health");
        return ResponseEntity.ok("{\"status\": \"ok\"}");
    }

    @GetMapping("/reservation/{id}")
    public ResponseEntity<HashMap<String, Object>> getReservationByID(@PathVariable Integer id) {

        log.info("USE [GET] /reservation/{}", id.toString());

        HashMap<String, Object> response = new HashMap<>();
        String reservation = reservationService.getReservationById(id);
        response.put("reservationId", reservation);

        return ResponseEntity.ok(response);
    }
}
