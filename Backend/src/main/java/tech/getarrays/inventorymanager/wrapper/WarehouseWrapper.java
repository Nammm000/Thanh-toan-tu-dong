package tech.getarrays.inventorymanager.wrapper;

//import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
//@AllArgsConstructor
public class WarehouseWrapper {

    Long id;
    String name;
    Boolean IsRefrigerated;
    Boolean status;
    Long locationId;
    String locationName;

    public WarehouseWrapper(Long id, String name, Boolean IsRefrigerated, Boolean status, Long locationId, String locationName) {
        this.id = id;
        this.name = name;
        this.IsRefrigerated = IsRefrigerated;
        this.status = status;
        this.locationId = locationId;
        this.locationName = locationName;
    }

    public WarehouseWrapper(Long id, String name, Boolean IsRefrigerated) {
        this.id = id;
        this.name = name;
        this.IsRefrigerated = IsRefrigerated;
//        this.status = status;
    }
}
