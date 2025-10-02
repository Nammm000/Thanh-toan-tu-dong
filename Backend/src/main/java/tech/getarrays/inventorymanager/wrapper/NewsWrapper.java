package tech.getarrays.inventorymanager.wrapper;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class NewsWrapper {
    private String title;
    private String description;
    private String content;
    private String plan_code;
    private String status;
    private LocalDateTime createdTime;

    public NewsWrapper(String title, String description, String content, String plan_code, String status, LocalDateTime createdTime) {
        this.title = title;
        this.description = description;
        this.content = content;
        this.plan_code = plan_code;
        this.status = status;
        this.createdTime = createdTime;
    }

}