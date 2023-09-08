package ru.practicum.ewm.controller;

import ru.practicum.ewm.dto.EndpointHit;
import ru.practicum.ewm.dto.ViewStats;
import ru.practicum.ewm.service.StatsServerService;

import java.time.LocalDateTime;
import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static util.Constants.PATTERN_FOR_DATETIME;

@RestController
@RequiredArgsConstructor
@Slf4j
public class StatsServerController {

    private final StatsServerService statsServerService;

    @PostMapping("/hit")
    /**
     * Сохранение информации о том, что на uri конкретного сервиса был отправлен запрос пользователем.
     */
    public ResponseEntity<EndpointHit> saveUser(@RequestBody EndpointHit endpointHitDto) {
        endpointHitDto = statsServerService.saveEndpointHit(endpointHitDto);
        log.info("Добавлена новая информация о запросе: {}", endpointHitDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(endpointHitDto);
    }

    @GetMapping("/stats")
    /**
     *
     Получение статистики по посещениям.
     */
    public ResponseEntity<List<ViewStats>> getAllStats(
            @RequestParam @DateTimeFormat(pattern = PATTERN_FOR_DATETIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(pattern = PATTERN_FOR_DATETIME) LocalDateTime end,
            @RequestParam(required = false) List<String> uris,
            @RequestParam(defaultValue = "false") Boolean unique) {
        List<ViewStats> stats = statsServerService.getAllStats(start, end, uris, unique);
        log.info("Получена статистика по посещениям start = {}, end = {}, uris = {}, unique = {}.",
                start, end, uris, unique);
        return ResponseEntity.ok().body(stats);
    }

}