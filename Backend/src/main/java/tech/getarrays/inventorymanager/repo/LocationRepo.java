package tech.getarrays.inventorymanager.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import tech.getarrays.inventorymanager.models.POJO.Category;
import tech.getarrays.inventorymanager.models.POJO.Location;

import java.util.List;

public interface LocationRepo extends JpaRepository<Location, Long> {
    List<Category> getAllLocation();
}