package tech.getarrays.inventorymanager.models.POJO;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.io.Serializable;

@NamedQuery(name = "Customer.getAllCustomer" , query = "select c from Customer c")

@NamedQuery(name = "Customer.getCustomerById", query = "select new tech.getarrays.inventorymanager.wrapper.CustomerWrapper(c.id, c.username, c.address) from Customer c where c.id=:id")

@NamedQuery(name = "Customer.findAllContactNumbers", query = "select new tech.getarrays.inventorymanager.wrapper.CustomerWrapper(c.id, c.contactNumber) from Customer c")

@Data
@Entity
@DynamicUpdate
@DynamicInsert
@Table(name = "customer1")
public class Customer implements Serializable {

    private static final long serialVersionUID = 91L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(unique=true)
    private String contactNumber;

    private String address;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
