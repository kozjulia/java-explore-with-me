package ru.practicum.ewm.compilation.service;

import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.NewCompilationDto;
import ru.practicum.ewm.compilation.dto.UpdateCompilationRequest;
import ru.practicum.ewm.compilation.mapper.CompilationMapper;
import ru.practicum.ewm.compilation.model.Compilation;
import ru.practicum.ewm.compilation.repository.CompilationRepository;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.exception.ConflictException;
import ru.practicum.ewm.exception.NotSaveException;
import ru.practicum.ewm.util.UtilService;

import java.util.HashSet;
import java.util.Set;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CompAdminServiceImpl implements CompAdminService {

    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;
    private final UtilService utilService;

    @Override
    public CompilationDto saveCompilation(NewCompilationDto newCompilationDto) {
        Set<Event> events = new HashSet<>();
        if (newCompilationDto.getEvents() != null && !newCompilationDto.getEvents().isEmpty()) {
            events.addAll(eventRepository.findAllById(newCompilationDto.getEvents()));
        }

        try {
            Compilation compilation = compilationRepository.save(
                    CompilationMapper.INSTANCE.toCompilationFromNewDto(newCompilationDto, events));
            return CompilationMapper.INSTANCE.toCompilationDto(compilation);
        } catch (DataIntegrityViolationException e) {
            throw new NotSaveException("Подборка событий не была создана: " + newCompilationDto);
        }
    }

    @Override
    public void deleteCompilationById(Long compId) {
        utilService.returnCompilation(compId);
        try {
            compilationRepository.deleteById(compId);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException("Подборка с ид = " + compId + " не может быть удалена.");
        }
    }

    @Override
    public CompilationDto updateCompilation(Long compId, UpdateCompilationRequest updateCompilationRequest) {
        Compilation compilation = utilService.returnCompilation(compId);

        if (updateCompilationRequest.getEvents() != null) {
            Set<Event> events = new HashSet<>(eventRepository.findAllById(updateCompilationRequest.getEvents()));
            compilation.setEvents(events);
        }
        if (updateCompilationRequest.getPinned() != null) {
            compilation.setPinned(updateCompilationRequest.getPinned());
        }
        if (updateCompilationRequest.getTitle() != null) {
            compilation.setTitle(updateCompilationRequest.getTitle());
        }

        try {
            return CompilationMapper.INSTANCE.toCompilationDto(compilationRepository.saveAndFlush(compilation));
        } catch (DataIntegrityViolationException e) {
            throw new NotSaveException("Подборка событий с id = " + compId +
                    " не была обновлена: " + updateCompilationRequest);
        }
    }

}