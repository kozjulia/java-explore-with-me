package ru.practicum.ewm;

import ru.practicum.ewm.repository.StatsServerRepository;
import ru.practicum.ewm.dto.EndpointHit;
import ru.practicum.ewm.dto.ViewStats;
import ru.practicum.ewm.exception.HitNotSaveException;
import ru.practicum.ewm.mapper.StatsServerMapper;
import ru.practicum.ewm.model.Hit;
import ru.practicum.ewm.service.StatsServerServiceImpl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StatsServerServiceImplTest {

    @Mock
    private StatsServerRepository statsServerRepository;

    @InjectMocks
    private StatsServerServiceImpl statsServerService;

    @Test
    @DisplayName("сохранена информация о запросе, когда информация валидна, тогда она сохраняется")
    void saveEndpointHit_whenEndpointHitValid_thenSavedEndpointHit() {
        EndpointHit endpointHitToSave = new EndpointHit();
        endpointHitToSave.setIp("1.1.1.1");
        when(statsServerRepository.save(any(Hit.class)))
                .thenReturn(StatsServerMapper.INSTANCE.toHit(endpointHitToSave));

        EndpointHit actualEndpointHit = statsServerService.saveEndpointHit(endpointHitToSave);

        assertThat(endpointHitToSave, equalTo(actualEndpointHit));
        verify(statsServerRepository, times(1)).save(any(Hit.class));
    }

    @Test
    @DisplayName("сохранена информация о запросе, когда информация не валидна, " +
            "тогда выбрасывается исключение")
    void saveEndpointHit_whenEndpointHitNotValid_thenExceptionThrown() {
        EndpointHit endpointHitToSave = new EndpointHit();
        when(statsServerRepository.save(any(Hit.class)))
                .thenThrow(new DataIntegrityViolationException("Информация не была сохранена"));

        final HitNotSaveException exception = assertThrows(HitNotSaveException.class,
                () -> statsServerService.saveEndpointHit(endpointHitToSave));

        assertThat("Информация о запросе не была сохранена: " + endpointHitToSave,
                equalTo(exception.getMessage()));
        verify(statsServerRepository, times(1)).save(any(Hit.class));
    }

    @Test
    @DisplayName("получена вся статистика с уникальными ури, когда вызвана, то получен непустой список")
    void getAllUniqueStats_whenInvoked_thenReturnedStatsCollectionInList() {
        ViewStats viewStats1 = new ViewStats("app1", "uri1", 5L);
        ViewStats viewStats2 = new ViewStats("app2", "uri2", 1L);
        List<ViewStats> expectedViewStats = List.of(viewStats1, viewStats2);

        when(statsServerRepository.getAllUniqueStats(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(expectedViewStats);

        List<ViewStats> actualViewStats = statsServerService.getAllStats(LocalDateTime.now(),
                LocalDateTime.now(), null, true);

        assertThat(expectedViewStats.size(), equalTo(actualViewStats.size()));
        assertThat(expectedViewStats.get(0), equalTo(actualViewStats.get(0)));
        assertThat(expectedViewStats.get(1), equalTo(actualViewStats.get(1)));

        verify(statsServerRepository, times(1))
                .getAllUniqueStats(any(LocalDateTime.class), any(LocalDateTime.class));
    }

    @Test
    @DisplayName("получена вся статистика, когда вызвана, то получен непустой список")
    void getAllStats_whenInvoked_thenReturnedStatsCollectionInList() {
        ViewStats viewStats1 = new ViewStats("app1", "uri1", 5L);
        ViewStats viewStats2 = new ViewStats("app2", "uri2", 1L);
        List<ViewStats> expectedViewStats = List.of(viewStats1, viewStats2);

        when(statsServerRepository.getAllStats(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(expectedViewStats);

        List<ViewStats> actualViewStats = statsServerService.getAllStats(LocalDateTime.now(),
                LocalDateTime.now(), null, false);

        assertThat(expectedViewStats.size(), equalTo(actualViewStats.size()));
        assertThat(expectedViewStats.get(0), equalTo(actualViewStats.get(0)));
        assertThat(expectedViewStats.get(1), equalTo(actualViewStats.get(1)));

        verify(statsServerRepository, times(1))
                .getAllStats(any(LocalDateTime.class), any(LocalDateTime.class));
    }

}