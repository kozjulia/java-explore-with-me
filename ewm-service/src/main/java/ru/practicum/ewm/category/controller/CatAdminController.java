package ru.practicum.ewm.category.controller;

import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.dto.NewCategoryDto;
import ru.practicum.ewm.category.service.CatAdminService;

import javax.validation.Valid;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
@Slf4j
public class CatAdminController {

    private final CatAdminService adminService;

    @PostMapping
    @Validated
    /**
     * Добавление новой категории.
     */
    public ResponseEntity<CategoryDto> saveCategory(@Valid @RequestBody NewCategoryDto newCategoryDto) {
        CategoryDto categoryDto = adminService.saveCategory(newCategoryDto);
        log.info("Добавлена новая категория: {}", categoryDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryDto);
    }

    @DeleteMapping("/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    /**
     * Удаление категории.
     */
    public ResponseEntity<Boolean> deleteCategoryById(@PathVariable Long catId) {
        Boolean result = adminService.deleteCategoryById(catId);
        log.info("Удалена категория с id = {}", catId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(result);
    }

    @PatchMapping("/{catId}")
    @Validated
    /**
     * Изменение категории.
     */
    public ResponseEntity<CategoryDto> updateCategory(
            @PathVariable Long catId, @Valid @RequestBody CategoryDto categoryDto) {
        categoryDto = adminService.updateCategory(catId, categoryDto);
        log.info("Обновлена категория с id = {}: {}.", catId, categoryDto);
        return ResponseEntity.ok(categoryDto);
    }

}