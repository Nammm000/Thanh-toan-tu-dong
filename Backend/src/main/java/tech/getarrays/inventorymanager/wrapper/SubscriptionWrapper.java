package tech.getarrays.inventorymanager.wrapper;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class SubscriptionWrapper {
    private String subscriber;
    private String name;
    private String plan_code;
    private String plan_name;
    private Float price;
    private LocalDateTime startDate;
    private LocalDateTime expirationDate;
    private String status;

    public SubscriptionWrapper(String subscriber, String name, String plan_code, String plan_name, Float price, LocalDateTime startDate, LocalDateTime expirationDate, String status) {
        this.subscriber = subscriber;
        this.name = name;
        this.plan_code = plan_code;
        this.plan_name = plan_name;
        this.price = price;
        this.startDate = startDate;
        this.expirationDate = expirationDate;
        this.status = status;
    }

    public SubscriptionWrapper(Float price, LocalDateTime expirationDate) {
        this.price = price;
        this.expirationDate = expirationDate;
    }
}
