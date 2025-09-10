package tech.getarrays.inventorymanager.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import tech.getarrays.inventorymanager.models.POJO.Bill;

import java.util.List;

public interface BillRepo extends JpaRepository<Bill, Long> {
    List<Bill> getAllBills();
    List<Bill> getBillByUserName(@Param("username") String username);
}
