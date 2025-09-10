package tech.getarrays.inventorymanager.wrapper;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductWrapper {
    Long id;
    String name;
    String description;
    Integer price;
    String status;
    Long categoryId;
    String categoryName;

    @Builder
    public ProductWrapper(Long id, String name, String description, Integer price, Long categoryId, String categoryName, String status) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.status = status;
    }

    public ProductWrapper(String categoryName, Long categoryId) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }

    public ProductWrapper(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public ProductWrapper(Long id, String name, String description, Integer price) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
    }
}
