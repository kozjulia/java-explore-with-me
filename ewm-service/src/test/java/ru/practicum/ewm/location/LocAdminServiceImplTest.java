package ru.practicum.ewm.location;

import ru.practicum.ewm.location.dto.LocationDto;
import ru.practicum.ewm.location.mapper.LocationMapper;
import ru.practicum.ewm.location.model.Location;
import ru.practicum.ewm.location.repository.LocationRepository;
import ru.practicum.ewm.location.service.LocAdminServiceImpl;
import ru.practicum.ewm.util.UtilService;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LocAdminServiceImplTest {

    @Mock
    private LocationRepository locationRepository;
    @Mock
    private UtilService utilService;

    @InjectMocks
    private LocAdminServiceImpl adminService;

    @Test
    @DisplayName("обновлена локация, когда локация валидна, тогда она обновляется")
    void updateLocation_whenLocationFound_thenUpdatedLocation() {
        Long locId = 0L;
        Location oldLocation = new Location();
        oldLocation.setLat(-10.23f);
        oldLocation.setLon(15.78f);
        oldLocation.setRadius(30f);
        when(utilService.returnLocationById(anyLong())).thenReturn(oldLocation);

        Location newLocation = new Location();
        newLocation.setLat(40.23f);
        newLocation.setLon(35.78f);
        newLocation.setRadius(15f);
        when(locationRepository.saveAndFlush(any(Location.class))).thenReturn(newLocation);

        LocationDto actualLocation = adminService
                .updateLocation(locId, LocationMapper.INSTANCE.toLocationDto(newLocation));

        assertThat(newLocation.getLat(), equalTo(actualLocation.getLat()));
        assertThat(newLocation.getLon(), equalTo(actualLocation.getLon()));
        assertThat(newLocation.getRadius(), equalTo(actualLocation.getRadius()));
        InOrder inOrder = inOrder(utilService, locationRepository);
        inOrder.verify(utilService, times(1)).returnLocationById(anyLong());
        inOrder.verify(locationRepository, times(1)).saveAndFlush(any(Location.class));
    }

    @Test
    @DisplayName("получены все локации, когда вызваны, то получен непустой список")
    void getAllLocations_whenInvoked_thenReturnedLocationsCollectionInList() {
        List<Location> expectedLocations = List.of(new Location(), new Location());
        when(locationRepository.findByRadiusIsGreaterThan(anyFloat(), any(Pageable.class)))
                .thenReturn(expectedLocations);

        List<LocationDto> actualLocations = adminService.getAllLocations(0, 5);

        assertThat(LocationMapper.INSTANCE.convertLocationListToLocationDTOList(expectedLocations),
                equalTo(actualLocations));
        verify(locationRepository, times(1))
                .findByRadiusIsGreaterThan(anyFloat(), any(Pageable.class));
        verifyNoMoreInteractions(locationRepository);
    }

    @Test
    @DisplayName("получена локация по ид, когда локация найдена, тогда она возвращается")
    void getLocationById_whenLocationFound_thenReturnedLocation() {
        long locId = 0L;
        Location expectedLocation = new Location();
        when(utilService.returnLocationById(anyLong())).thenReturn(expectedLocation);

        LocationDto actualLocation = adminService.getLocationById(locId);

        assertThat(LocationMapper.INSTANCE.toLocationDto(expectedLocation), equalTo(actualLocation));
        verify(utilService, times(1)).returnLocationById(anyLong());
        verifyNoMoreInteractions(utilService);
    }

}