package tech.getarrays.inventorymanager.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.getarrays.inventorymanager.models.POJO.Category;

import java.util.List;

public interface CategoryRepo extends JpaRepository<Category, Long> {
    List<Category> getAllCategory();
}
