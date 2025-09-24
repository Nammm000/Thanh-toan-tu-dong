package tech.getarrays.inventorymanager.repo;

import jakarta.persistence.NamedQuery;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tech.getarrays.inventorymanager.models.POJO.Plan;
import tech.getarrays.inventorymanager.wrapper.PlanCodeWrapper;
import tech.getarrays.inventorymanager.wrapper.PlanWrapper;

import java.util.List;

public interface SubscriptionRepo extends JpaRepository<Plan, Long> {

    Plan findFirstById(@Param("id") Long id);

}
