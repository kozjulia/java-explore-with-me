package ru.practicum.ewm.location.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.location.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {

    Location findByLatAndLonAndRadius(Float lat, Float lon, Float radius);

    @Modifying
    @Query("delete from Category c where c.id = ?1")
    Integer deleteByIdWithReturnedLines(Long catId);

    List<Location> findByRadiusIsGreaterThan(Float radius, Pageable page);

}