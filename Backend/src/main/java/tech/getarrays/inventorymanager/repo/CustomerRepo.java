package tech.getarrays.inventorymanager.repo;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import tech.getarrays.inventorymanager.models.POJO.Customer;
import tech.getarrays.inventorymanager.wrapper.CustomerWrapper;

import java.util.List;

public interface CustomerRepo extends JpaRepository<Customer, Long> {
    List<CustomerWrapper> findAllContactNumbers();
    CustomerWrapper getCustomerById(@Param("id") Long id);
    List<Customer> getAllCustomer();
    Customer findFirstByContactNumber(@Param("contactNumber") String ContactNumber);
//    List<CustomerWrapper> getAllCustomer();
}
