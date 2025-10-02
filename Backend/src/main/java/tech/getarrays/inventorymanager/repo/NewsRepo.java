package tech.getarrays.inventorymanager.repo;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tech.getarrays.inventorymanager.models.POJO.News;
import tech.getarrays.inventorymanager.wrapper.NewsWrapper;

public interface NewsRepo extends JpaRepository<News, Long> {

    News findFirstById(@Param("id") Long id);

    @Query("select new tech.getarrays.inventorymanager.wrapper.NewsWrapper(u.title , u.description , u.content , u.plan_code , u.status , u.createdTime) from News u where u.id=:id")
    NewsWrapper getNewsById(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query("update News n set n.status =:status where n.id=:id")
    Integer updateNewsStatus(@Param("status") String status, @Param("id") Long id);
}
