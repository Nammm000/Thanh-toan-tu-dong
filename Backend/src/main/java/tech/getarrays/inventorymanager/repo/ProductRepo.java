package tech.getarrays.inventorymanager.repo;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tech.getarrays.inventorymanager.models.POJO.Product;
import tech.getarrays.inventorymanager.wrapper.ProductWrapper;
import org.springframework.data.jpa.repository.Modifying;


import java.util.List;

public interface ProductRepo extends JpaRepository<Product, Long> {

    List<ProductWrapper> getAllProduct();

    List<ProductWrapper> getByCategory(@Param("id") Long id);

    ProductWrapper getProductById(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query("update Product p set p.status =:status where p.id=:id")
    Integer updateProductStatus(@Param("status") String status, @Param("id") Long id);

}
