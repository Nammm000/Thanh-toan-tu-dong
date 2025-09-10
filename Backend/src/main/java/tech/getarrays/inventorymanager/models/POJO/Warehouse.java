package tech.getarrays.inventorymanager.models.POJO;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.io.Serializable;

@NamedQuery(name = "Warehouse.getAllWarehouse", query = "select new tech.getarrays.inventorymanager.wrapper.WarehouseWrapper(w.id, w.name, w.IsRefrigerated, w.status, w.location.id, w.location.name) from Warehouse w")

@NamedQuery(name = "Warehouse.updateWarehouseStatus" , query = "update Warehouse w set w.status =:status where w.id =:id")

@NamedQuery(name = "Warehouse.getByLocation", query = "select new tech.getarrays.inventorymanager.wrapper.WarehouseWrapper(w.id, w.name, w.IsRefrigerated, w.location.id, w.location.name, w.status) from Warehouse w where w.location.id=:id and w.status=true")

@NamedQuery(name = "Warehouse.getWarehouseById", query = "select new tech.getarrays.inventorymanager.wrapper.WarehouseWrapper(w.id, w.name, w.IsRefrigerated, w.status) from Warehouse w where w.id=:id")

@Data
@Entity
@DynamicUpdate
@DynamicInsert
@Table(name = "warehouse1")
public class Warehouse implements Serializable {

    private static final long serialVersionUID = 331L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long id;

    private String name;

    @Column(name = "IsRefrigerated")
    private Boolean IsRefrigerated;

    private Boolean status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_fk", nullable = false)
    private Location location;

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

    public Boolean getRefrigerated() {
        return IsRefrigerated;
    }

    public void setRefrigerated(Boolean refrigerated) {
        IsRefrigerated = refrigerated;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
