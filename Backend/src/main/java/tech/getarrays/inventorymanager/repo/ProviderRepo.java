package tech.getarrays.inventorymanager.repo;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

import tech.getarrays.inventorymanager.models.POJO.Provider;
import tech.getarrays.inventorymanager.wrapper.ProviderWrapper;

import java.util.List;

public interface ProviderRepo extends JpaRepository<Provider, Long> {
//    Provider findFirstByEmail(@Param("email") String email);
    List<ProviderWrapper> getAllProvider();

    @Transactional
    @Modifying
    Integer updateProviderStatus(@Param("status") Boolean status, @Param("id") Long id);
}
