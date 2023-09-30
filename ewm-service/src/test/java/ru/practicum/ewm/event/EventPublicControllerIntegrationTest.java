package ru.practicum.ewm.event;

import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.event.controller.EventPublicController;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.location.dto.LocationDto;
import ru.practicum.ewm.event.dto.NewEventDto;
import ru.practicum.ewm.event.mapper.EventMapper;
import ru.practicum.ewm.location.mapper.LocationMapper;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.location.model.Location;
import ru.practicum.ewm.event.model.SortSearch;
import ru.practicum.ewm.event.service.EventPublicService;
import ru.practicum.ewm.user.model.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EventPublicController.class)
@AutoConfigureMockMvc
class EventPublicControllerIntegrationTest {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private EventPublicService publicService;

    private EventShortDto eventShortDto1;
    private EventShortDto eventShortDto2;
    private EventFullDto eventFullDto1;


    @BeforeEach
    public void addEvents() {
        User user = new User();
        user.setId(5L);
        user.setEmail("email@mail.ru");
        user.setName("user name");

        Category category = new Category();
        category.setId(8L);
        category.setName("name category");

        LocationDto locationDto = new LocationDto();
        locationDto.setId(2L);
        locationDto.setLat(23.45f);
        locationDto.setLon(76.21f);
        Location location = LocationMapper.INSTANCE.toLocation(locationDto);
        location.setId(locationDto.getId());

        NewEventDto newEventDto1 = new NewEventDto();
        newEventDto1.setAnnotation("annotation 1");
        newEventDto1.setCategory(category.getId());
        newEventDto1.setDescription("description 1");
        newEventDto1.setEventDate(LocalDateTime.now());
        newEventDto1.setLocation(locationDto);
        newEventDto1.setPaid(true);
        newEventDto1.setParticipantLimit(20);
        newEventDto1.setRequestModeration(false);
        newEventDto1.setTitle("title 1");

        Event event1 = EventMapper.INSTANCE.toEventFromNewDto(newEventDto1, user, category, location);
        event1.setId(1L);
        eventShortDto1 = EventMapper.INSTANCE.toEventShortDto(event1);
        eventFullDto1 = EventMapper.INSTANCE.toEventFullDto(event1);

        NewEventDto newEventDto2 = new NewEventDto();
        newEventDto2.setAnnotation("annotation 2");
        newEventDto2.setCategory(category.getId());
        newEventDto2.setDescription("description 2");
        newEventDto2.setEventDate(LocalDateTime.now());
        newEventDto2.setLocation(locationDto);
        newEventDto2.setPaid(true);
        newEventDto2.setParticipantLimit(30);
        newEventDto2.setRequestModeration(true);
        newEventDto2.setTitle("title 2");

        Event event2 = EventMapper.INSTANCE.toEventFromNewDto(newEventDto2, user, category, location);
        event2.setId(2L);
        eventShortDto2 = EventMapper.INSTANCE.toEventShortDto(event2);
    }

    @SneakyThrows
    @Test
    @DisplayName("получены все события с возможностью фильтрации, когда вызваны, " +
            "то ответ статус ок и список событий")
    void getAllEvents_whenInvoked_thenResponseStatusOkWithEventsCollectionInBody() {
        List<EventShortDto> events = List.of(eventShortDto1, eventShortDto2);
        when(publicService.getAllEvents(anyString(), anyList(), anyBoolean(), any(), any(), anyBoolean(),
                any(SortSearch.class), anyInt(), anyInt(), any(HttpServletRequest.class)))
                .thenReturn(events);

        String result = mockMvc.perform(get("/events")
                        .param("text", "any")
                        .param("categories", "1", "2")
                        .param("paid", "true")
                        .param("onlyAvailable", "true")
                        .param("sort", "EVENT_DATE")
                        .param("from", "0")
                        .param("size", "5")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        assertThat(objectMapper.writeValueAsString(events), equalTo(result));
        verify(publicService, times(1))
                .getAllEvents(anyString(), anyList(), anyBoolean(), any(), any(), anyBoolean(), any(SortSearch.class),
                        anyInt(), anyInt(), any(HttpServletRequest.class));
        verifyNoMoreInteractions(publicService);
    }

    @SneakyThrows
    @Test
    @DisplayName("получено событие по ид, когда событие найдено, " +
            "то ответ статус ок, и оно возвращается")
    void getEventById_whenEventFound_thenReturnedEvent() {
        long eventId = 0L;
        when(publicService.getPublicEventById(anyLong(), any(HttpServletRequest.class)))
                .thenReturn(eventFullDto1);

        String result = mockMvc.perform(get("/events/{id}", eventId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        assertThat(objectMapper.writeValueAsString(eventFullDto1), equalTo(result));
        verify(publicService, times(1))
                .getPublicEventById(anyLong(), any(HttpServletRequest.class));
        verifyNoMoreInteractions(publicService);
    }

}