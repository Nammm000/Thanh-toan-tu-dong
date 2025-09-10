package tech.getarrays.inventorymanager.models.POJO;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.io.Serializable;

@NamedQuery(name = "Provider.getAllProvider" , query = "select p from Provider p")

@NamedQuery(name = "Provider.updateProviderStatus" , query = "update Provider u set u.status =:status where u.id =:id")

@Data
@Entity
@DynamicUpdate
@DynamicInsert
@Table(name = "provider1")
public class Provider implements Serializable {

    private static final long serialVersionUID = 71L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long id;

    private String name;

    private String address;

    private Boolean status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
