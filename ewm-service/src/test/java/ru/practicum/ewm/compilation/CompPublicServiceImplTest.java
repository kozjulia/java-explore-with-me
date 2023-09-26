package ru.practicum.ewm.compilation;

import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.mapper.CompilationMapper;
import ru.practicum.ewm.compilation.model.Compilation;
import ru.practicum.ewm.compilation.repository.CompilationRepository;
import ru.practicum.ewm.compilation.service.CompPublicServiceImpl;
import ru.practicum.ewm.util.UtilService;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompPublicServiceImplTest {

    @Mock
    private CompilationRepository compilationRepository;
    @Mock
    private UtilService utilService;

    @InjectMocks
    private CompPublicServiceImpl compPublicService;

    @Test
    @DisplayName("получены все подборки, когда вызваны, то получен непустой список")
    void getAllCompilations_whenInvoked_thenReturnedCompilationsCollectionInList() {
        List<Compilation> expectedCompilations = List.of(new Compilation(), new Compilation());
        Page<Compilation> expectedCompilationsPage = new PageImpl<>(
                expectedCompilations, PageRequest.of(0, 1), 2);
        when(compilationRepository.findAll(any(Pageable.class))).thenReturn(expectedCompilationsPage);

        List<CompilationDto> actualCompilations = compPublicService
                .getAllCompilations(null, 0, 1);

        assertThat(CompilationMapper.INSTANCE.convertCompilationListToCompilationDTOList(expectedCompilations),
                equalTo(actualCompilations));
        verify(compilationRepository, times(1))
                .findAll(any(Pageable.class));
    }

    @Test
    @DisplayName("получена подборка по ид, когда подборка найдена, тогда она возвращается")
    void getCompilationById_whenCompilationFound_thenReturnedCompilation() {
        long compId = 0L;
        Compilation expectedCompilation = new Compilation();
        when(utilService.returnCompilation(anyLong())).thenReturn(expectedCompilation);

        CompilationDto actualCompilation = compPublicService.getCompilationById(compId);

        assertThat(CompilationMapper.INSTANCE.toCompilationDto(expectedCompilation), equalTo(actualCompilation));
        verify(utilService, times(1)).returnCompilation(anyLong());
    }

}