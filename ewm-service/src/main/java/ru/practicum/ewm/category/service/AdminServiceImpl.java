package ru.practicum.ewm.category.service;

import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.dto.NewCategoryDto;
import ru.practicum.ewm.exception.ConflictException;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.exception.NotSaveException;
import ru.practicum.ewm.category.mapper.CategoryMapper;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.category.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final CategoryRepository categoryRepository;

    @Override
    public CategoryDto saveCategory(NewCategoryDto newCategoryDto) {
        try {
            Category category = categoryRepository.save(
                    CategoryMapper.INSTANCE.toCategoryFromNewDto(newCategoryDto));
            return CategoryMapper.INSTANCE.toCategoryDto(category);
        } catch (DataIntegrityViolationException e) {
            throw new NotSaveException("Категория не была создана: " + newCategoryDto);
        }
    }

    @Override
    public void deleteCategoryById(Long catId) {
        //if (categoryRepository.findById(catId).isEmpty()) {
        if (!categoryRepository.existsById(catId)) {
            throw new NotFoundException("Категория с ид = " + catId + " не была найдена.");
        }

        if (categoryRepository.deleteByIdWithReturnedLines(catId) == 0) {
            throw new ConflictException("Категория с ид = " + catId + " не может быть удалена, " +
                    "существуют события, связанные с категорией.");
        }
    }

    @Override
    public CategoryDto updateCategory(Long catId, CategoryDto categoryDto) {
        Category category = categoryRepository.findById(catId).orElseThrow(() ->
                new NotFoundException("Категория с id = " + catId + " не найдена."));
        category.setName(categoryDto.getName());

        try {
            return CategoryMapper.INSTANCE.toCategoryDto(categoryRepository.saveAndFlush(category));
        } catch (DataIntegrityViolationException e) {
            throw new NotSaveException("Категория с id = " + catId + " не была обновлена: " + categoryDto);
        }
    }

}