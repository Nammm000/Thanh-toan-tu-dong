package tech.getarrays.inventorymanager.wrapper;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
//@AllArgsConstructor
public class ProviderWrapper {

    private Long id;
    private String name;
    private String email;
    private String phone;
    private String status;

    public ProviderWrapper(Long id, String name, String email, String phone, String status) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.status = status;
    }
}
