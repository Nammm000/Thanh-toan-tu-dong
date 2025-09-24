package tech.getarrays.inventorymanager.repo;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tech.getarrays.inventorymanager.models.POJO.News;

public interface NewsRepo extends JpaRepository<News, Long> {

    @Modifying
    @Transactional
    @Query("update News n set n.status =:status where n.id=:id")
    Integer updateNewsStatus(@Param("status") String status, @Param("id") Long id);
}
