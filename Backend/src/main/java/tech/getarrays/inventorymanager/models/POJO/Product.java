package tech.getarrays.inventorymanager.models.POJO;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.io.Serializable;

@NamedQuery(name = "Product.getAllProduct", query = "select new tech.getarrays.inventorymanager.wrapper.ProductWrapper(u.id, u.name, u.description, u.price, u.category.id, u.category.name, u.status) from Product u")
//@NamedQuery(name = "Product.getAllProduct", query = "select new tech.getarrays.inventorymanager.wrapper.ProductWrapper(u.id, u.name, u.description, u.price, u.category.id, u.category.name) from Product u")

@NamedQuery(name = "Product.updateProductStatus" , query = "update Product u set u.status =:status where u.id =:id")

//@NamedQuery(name = "Product.getProductByCategory", query = "select new tech.getarrays.inventorymanager.wrapper.ProductWrapper(p.id, p.name , p.description , p.price ) from Product p where p.category.id=:id and status='true'")

@NamedQuery(name = "Product.getByCategory", query = "select new tech.getarrays.inventorymanager.wrapper.ProductWrapper(u.id , u.name , u.description , u.price , u.category.id , u.category.name , u.status  ) from Product u where u.category.id=:id and u.status='true'")
//@NamedQuery(name = "Product.getByCategory", query = "select new tech.getarrays.inventorymanager.wrapper.ProductWrapper(u.id , u.name , u.description , u.price , u.category.id , u.category.name) from Product u where u.category.id=:id")

@NamedQuery(name = "Product.getProductById", query = "select new tech.getarrays.inventorymanager.wrapper.ProductWrapper(u.id , u.name , u.description , u.price) from Product u where u.id=:id")

@Data
@Entity
@DynamicUpdate
@DynamicInsert
@Table(name = "product1")
public class Product implements Serializable {
    private static final long serialVersionUID = 123456L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long id;

    @Column(name = "name")
    private String name;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_fk", nullable = false)
    private Category category;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private Integer price;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Column(name = "status")
    private String status;

    public Product() {
    }

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

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }


    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", category=" + category +
                ", description='" + description + '\'' +
                ", price=" + price +
                '}';
    }
}
