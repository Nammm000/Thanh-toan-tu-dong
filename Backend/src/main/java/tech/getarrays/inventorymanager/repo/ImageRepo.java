package tech.getarrays.inventorymanager.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import tech.getarrays.inventorymanager.models.POJO.Image;

import java.util.List;

public interface ImageRepo extends JpaRepository<Image, Long> {
    Image findFirstById(@Param("id") Long id);
}