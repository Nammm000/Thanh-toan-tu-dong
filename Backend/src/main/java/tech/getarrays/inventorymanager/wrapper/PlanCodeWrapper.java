package tech.getarrays.inventorymanager.wrapper;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class PlanCodeWrapper {
    private String name;
    private String code;
//    private LocalDateTime createdTime;

    public PlanCodeWrapper(String name, String code) {
        this.name = name;
        this.code = code;
    }

}