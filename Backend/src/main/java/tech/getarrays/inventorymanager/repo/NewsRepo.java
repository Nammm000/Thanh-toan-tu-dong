package tech.getarrays.inventorymanager.repo;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tech.getarrays.inventorymanager.models.POJO.News;
import tech.getarrays.inventorymanager.wrapper.NewsWrapper;

import java.util.List;

public interface NewsRepo extends JpaRepository<News, Long> {

    News findFirstById(@Param("id") Long id);

    @Query("select n from News n where n.price=0 order by n.updatedTime desc")
    List<News> getPublicNews();

    @Query("select n from News n where n.price > 0 order by n.updatedTime desc")
    List<News> getNewsAdmin();

    @Query("select n from News n where (n.price <= :price and n.price > 0) order by n.updatedTime desc")
    List<News> getUserSubNews(@Param("price") Float price);

    @Query("select new tech.getarrays.inventorymanager.wrapper.NewsWrapper(u.title , u.description , u.content , u.plan_code , u.status , u.createdTime) from News u where u.id=:id")
    NewsWrapper getNewsById(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query("update News n set n.status =:status where n.id=:id")
    Integer updateNewsStatus(@Param("status") String status, @Param("id") Long id);
}
