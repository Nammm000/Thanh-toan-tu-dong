package tech.getarrays.inventorymanager.wrapper;

import lombok.Data;
import lombok.NoArgsConstructor;
import tech.getarrays.inventorymanager.models.User;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class UserWrapper {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String status;
    private LocalDateTime createdTime;
    private User.Role role;

    public UserWrapper(Long id, String name, String email, String phone, String status, LocalDateTime createdTime, User.Role role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.status = status;
        this.createdTime = createdTime;
        this.role = role;
    }
}