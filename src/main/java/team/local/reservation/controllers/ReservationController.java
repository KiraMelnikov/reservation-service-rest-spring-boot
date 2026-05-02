package team.local.reservation.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team.local.reservation.Reservation;
import team.local.reservation.dto.ReservationDto;
import team.local.reservation.services.ReservationService;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;


@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/")
public class ReservationController {

    private final ReservationService reservationService;

    @GetMapping("/reservations/{id}")
    public ResponseEntity<HashMap<String, Object>> getReservationByID(@PathVariable UUID id) {
        log.info("USE [GET] /reservations/{}", id.toString());

        HashMap<String, Object> response = new HashMap<>();

        Reservation reservation = reservationService.getReservationById(id);

        response.put("status", "success");
        response.put("reservation", reservation);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/reservations")
    public ResponseEntity<HashMap<String, Object>> getAllReservations() {
        log.info("USE [GET] /reservations");

        HashMap<String, Object> response = new HashMap<>();

        List<Reservation> reservations = reservationService.getAllReservations();
        response.put("status", "success");
        response.put("reservation", reservations);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/reservations")
    public ResponseEntity<HashMap<String, Object>> createReservation(@Valid @RequestBody ReservationDto body) {
        log.info("USE [POST] /reservations");

        HashMap<String, Object> response = new HashMap<>();

        Reservation reservation = reservationService.createReservation(body);
        response.put("status", "created");
        response.put("reservation", reservation);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        log.info("Use [GET] /api/v1/health");
        return ResponseEntity.ok("{\"status\": \"ok\"}");
    }
}
