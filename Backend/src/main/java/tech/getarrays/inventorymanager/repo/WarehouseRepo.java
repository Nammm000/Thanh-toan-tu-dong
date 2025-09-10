package tech.getarrays.inventorymanager.repo;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import tech.getarrays.inventorymanager.models.POJO.Warehouse;
import tech.getarrays.inventorymanager.wrapper.WarehouseWrapper;

import java.util.List;

public interface WarehouseRepo extends JpaRepository<Warehouse, Long> {

    List<WarehouseWrapper> getAllWarehouse();

    List<WarehouseWrapper> getByLocation(@Param("id") Long id);

    WarehouseWrapper getWarehouseById(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query("update Warehouse w set w.status =:status where w.id=:id")
    Integer updateWarehouseStatus(@Param("status") Boolean status, @Param("id") Long id);

}
