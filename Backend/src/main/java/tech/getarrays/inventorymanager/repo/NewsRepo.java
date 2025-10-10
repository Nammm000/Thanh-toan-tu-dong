package tech.getarrays.inventorymanager.repo;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tech.getarrays.inventorymanager.models.POJO.News;
import tech.getarrays.inventorymanager.wrapper.NewsImgWrapper;
import tech.getarrays.inventorymanager.wrapper.NewsWrapper;

import java.util.List;

public interface NewsRepo extends JpaRepository<News, Long> {

    News findFirstById(@Param("id") Long id);

    @Query("select new tech.getarrays.inventorymanager.wrapper.NewsWrapper(u.id , u.title , " +
            "u.description , u.content , " +
            "u.plan_code , u.status , u.createdTime , u.updatedTime , u.view) " +
            "from News u where u.status='true' order by u.updatedTime desc")
    List<NewsWrapper> getAllNews();

    @Query("select new tech.getarrays.inventorymanager.wrapper.NewsImgWrapper(n.id , n.title , " +
            "n.description , n.status , n.updatedTime , n.view , n.price , " +
            "n.image.name , n.image.type , n.image.picByte) " +
            "from News n order by n.updatedTime desc")
    List<NewsImgWrapper> getAllNewsImg();

    @Query("select new tech.getarrays.inventorymanager.wrapper.NewsImgWrapper(n.id , n.title , " +
            "n.description , n.content , " +
            "n.plan_code , n.status , n.createdTime , n.updatedTime , n.view , n.price , " +
            "n.image.id , n.image.name , n.image.type , n.image.picByte) " +
            "from News n where (n.price=0 and n.status='true') order by n.updatedTime desc")
    List<NewsImgWrapper> getPublicNews();

    @Query("select new tech.getarrays.inventorymanager.wrapper.NewsImgWrapper(n.id , n.title , " +
            "n.description , n.content , " +
            "n.plan_code , n.status , n.createdTime , n.updatedTime , n.view , n.price , " +
            "n.image.id , n.image.name , n.image.type , n.image.picByte) " +
            "from News n where n.price > 0 order by n.updatedTime desc")
    List<NewsImgWrapper> getPaidNews();

    @Query("select new tech.getarrays.inventorymanager.wrapper.NewsImgWrapper(n.id , n.title , " +
            "n.description , n.content , " +
            "n.plan_code , n.status , n.createdTime , n.updatedTime , n.view , n.price , " +
            "n.image.id , n.image.name , n.image.type , n.image.picByte) " +
            "from News n where (n.price <= :price and n.price > 0) order by n.updatedTime desc")
    List<NewsImgWrapper> getUserSubNews(@Param("price") Float price);

    @Query("select new tech.getarrays.inventorymanager.wrapper.NewsImgWrapper(u.id , u.title , " +
            "u.description , u.content , " +
            "u.plan_code , u.status , u.createdTime , u.updatedTime , u.view , u.price , " +
            "u.image.id , u.image.name , u.image.type , u.image.picByte) from News u where u.id=:id")
    NewsImgWrapper getNewsById(@Param("id") Long id);

    @Query("select n.price from News n where n.id=:id")
    Float getPriceByNewsId(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query("update News n set n.status =:status where n.id=:id")
    Integer updateNewsStatus(@Param("status") String status, @Param("id") Long id);

    @Modifying
    @Transactional
    @Query("update News n set n.view =:views where n.id=:id")
    Integer updateNewsViews(@Param("views") Integer views, @Param("id") Long id);
}
