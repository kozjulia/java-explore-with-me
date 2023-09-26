package ru.practicum.ewm;

import ru.practicum.ewm.controller.StatsServerController;
import ru.practicum.ewm.dto.EndpointHit;
import ru.practicum.ewm.dto.ViewStats;
import ru.practicum.ewm.service.StatsServerServiceImpl;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import lombok.SneakyThrows;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StatsServerController.class)
@AutoConfigureMockMvc
class StatsServerControllerIntegrationTest {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private StatsServerServiceImpl statsServerService;

    @SneakyThrows
    @Test
    @DisplayName("сохранена информация о запросе, когда информация валидна, " +
            "то ответ статус ок, и она сохраняется")
    void saveHit_whenHitValid_thenSavedHit() {
        EndpointHit endpointHit = new EndpointHit();
        endpointHit.setId(1L);
        endpointHit.setApp("app");
        endpointHit.setUri("/uri");
        endpointHit.setIp("23.23.23.23");
        endpointHit.setTimestamp(LocalDateTime.now());

        when(statsServerService.saveEndpointHit(any(EndpointHit.class))).thenReturn(endpointHit);

        String result = mockMvc.perform(post("/hit")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(endpointHit)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        assertThat(objectMapper.writeValueAsString(endpointHit), equalTo(result));
        verify(statsServerService, times(1)).saveEndpointHit(any(EndpointHit.class));
    }

    @SneakyThrows
    @Test
    @DisplayName("получены все вещи пользователя, когда вызваны, " +
            "то ответ статус ок и список вещей")
    void getAllStats_whenInvoked_thenResponseStatusOkWithStatsCollectionInBody() {
        ViewStats viewStats1 = new ViewStats("app1", "uri1", 5L);
        ViewStats viewStats2 = new ViewStats("app2", "uri2", 1L);
        List<ViewStats> viewStats = List.of(viewStats1, viewStats2);
        when(statsServerService.getAllStats(any(LocalDateTime.class), any(LocalDateTime.class),
                anyList(), anyBoolean())).thenReturn(viewStats);

        String result = mockMvc.perform(get("/stats")
                        .param("start", "2023-08-01 01:00:00")
                        .param("end", "2024-10-01 01:00:00")
                        .param("uris", "null")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        assertThat(objectMapper.writeValueAsString(viewStats), equalTo(result));
        verify(statsServerService, times(1))
                .getAllStats(any(LocalDateTime.class), any(LocalDateTime.class),
                        anyList(), anyBoolean());
    }

}