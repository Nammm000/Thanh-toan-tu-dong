package tech.getarrays.inventorymanager.wrapper;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class PlanWrapper {
    private String name;
    private String description;
    private String code;
    private Float price;
    private Integer duration;
    private String status;
    private LocalDateTime createdTime;

    public PlanWrapper(String name, String description, String code, Float price, Integer duration, String status, LocalDateTime createdTime) {
        this.name = name;
        this.description = description;
        this.code = code;
        this.price = price;
        this.duration = duration;
        this.status = status;
        this.createdTime = createdTime;
    }

}