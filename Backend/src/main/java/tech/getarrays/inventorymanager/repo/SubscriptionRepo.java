package tech.getarrays.inventorymanager.repo;

import jakarta.persistence.NamedQuery;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tech.getarrays.inventorymanager.models.POJO.Subscription;
import tech.getarrays.inventorymanager.wrapper.SubscriptionWrapper;

import java.util.List;

public interface SubscriptionRepo extends JpaRepository<Subscription, Long> {

    Subscription findFirstById(@Param("id") Long id);

    @Query("select su from Subscription su where (su.subscriber=:email and su.price > 0) order by su.expirationDate desc")
    List<Subscription> findAllByEmail(@Param("email") String email);

//    @Query("select new tech.getarrays.inventorymanager.wrapper.SubscriptionWrapper(u.name , u.code) from Subscription u")
//    List<SubscriptionWrapper> getAllSubscriptionWrapper();

}
