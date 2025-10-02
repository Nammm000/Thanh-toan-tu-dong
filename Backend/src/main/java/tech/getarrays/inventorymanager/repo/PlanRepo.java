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

public interface PlanRepo extends JpaRepository<Plan, Long> {

    Plan findFirstById(@Param("id") Long id);

//    @Query("select new Plan() from Plan u where u.code=:code")
    Plan findFirstByCode(@Param("code") String code);

    @Modifying
    @Transactional
    @Query("update Plan pl set pl.status =:status where pl.id=:id")
    Integer updatePlanStatus(@Param("status") String status, @Param("id") Long id);

    @Query("select new tech.getarrays.inventorymanager.wrapper.PlanWrapper(u.name , u.description , u.code , u.price , u.duration , u.status , u.createdTime) from Plan u where u.id=:id")
    PlanWrapper getPlanById(@Param("id") Long id);

    @Query("select new tech.getarrays.inventorymanager.wrapper.PlanCodeWrapper(u.name , u.code) from Plan u")
    List<PlanCodeWrapper> getAllPlanCode();
}
