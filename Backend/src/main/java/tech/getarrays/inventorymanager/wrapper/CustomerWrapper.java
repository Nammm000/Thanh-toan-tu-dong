package tech.getarrays.inventorymanager.wrapper;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
//@AllArgsConstructor
public class CustomerWrapper {
    Long id;
    String username;
    String contactNumber;
    String address;

    public CustomerWrapper(Long id, String username, String address) {
        this.id = id;
        this.username = username;
        this.address = address;
    }

    public CustomerWrapper(Long id, String username, String contactNumber, String address) {
        this.id = id;
        this.username = username;
        this.contactNumber = contactNumber;
        this.address = address;
    }

    public CustomerWrapper(Long id, String contactNumber) {
        this.id = id;
        this.contactNumber = contactNumber;
    }
}
