package tech.getarrays.inventorymanager.repo;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import tech.getarrays.inventorymanager.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.getarrays.inventorymanager.wrapper.UserWrapper;

import java.util.List;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    User findFirstByEmail(@Param("email") String email);
    List<UserWrapper> getAllUser();

    @Transactional
    @Modifying
    Integer updateStatus(@Param("status") String status, @Param("id") Long id);
}
