package tech.getarrays.inventorymanager.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import tech.getarrays.inventorymanager.constents.InventoryConstants;
import tech.getarrays.inventorymanager.filters.JwtRequestFilter;
import tech.getarrays.inventorymanager.models.POJO.Location;
import tech.getarrays.inventorymanager.models.POJO.Warehouse;
import tech.getarrays.inventorymanager.repo.WarehouseRepo;
import tech.getarrays.inventorymanager.util.InventoryUtils;
import tech.getarrays.inventorymanager.wrapper.WarehouseWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/warehouse")
public class WarehouseController {

    @Autowired
    WarehouseRepo warehouseRepo;

    @Autowired
    JwtRequestFilter jwtRequestFilter;

    @PostMapping("/add")
    public ResponseEntity<String> addNewWarehouse(@RequestBody Map<String, String> requestMap) {
        log.info("Inside addNewWarehouse{}", requestMap);
        try {
            if (jwtRequestFilter.isAdmin()) {
                if (validateWarehouseMap(requestMap, false)) {
                    warehouseRepo.save(getWarehouseFromMap(requestMap, false));
                    return InventoryUtils.getResponseEntity("Warehouse Added Successfully", HttpStatus.OK);
                }
            } else {
                return InventoryUtils.getResponseEntity(InventoryConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        System.out.println(InventoryConstants.SOMETHING_WENT_WRONG);
        return InventoryUtils.getResponseEntity(InventoryConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/get")
    public ResponseEntity<List<WarehouseWrapper>> getAllWarehouse() {
        try {
            return new ResponseEntity<>(warehouseRepo.getAllWarehouse(), HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("/update")
    public ResponseEntity<String> update(@RequestBody(required = true) Map<String, String> requestMap) {
        try {
            if (jwtRequestFilter.isAdmin()) {
                if (validateWarehouseMap(requestMap, true)) {
                    Optional optional = warehouseRepo.findById(Long.parseLong(requestMap.get("id")));
                    if (!optional.isEmpty()) {
                        warehouseRepo.save(getWarehouseFromMap(requestMap, true));
                        return InventoryUtils.getResponseEntity("Warehouse is updated successfully", HttpStatus.OK);
                    } else {
                        return InventoryUtils.getResponseEntity("Warehouse id doesn't exist", HttpStatus.OK);
                    }

                }
                return InventoryUtils.getResponseEntity(InventoryConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
            } else {
                return InventoryUtils.getResponseEntity(InventoryConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return InventoryUtils.getResponseEntity(InventoryConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("/updateStatus")
    public ResponseEntity<String> updateStatus(@RequestBody(required = true) Map<String, String> requestMap) {
        try {
            if (jwtRequestFilter.isAdmin()) {
                Optional<Warehouse> optionalWarehouse = warehouseRepo.findById(Long.parseLong(requestMap.get("id")));
                if (!optionalWarehouse.isEmpty()) {
                    warehouseRepo.updateWarehouseStatus(Boolean.parseBoolean(requestMap.get("status")), Long.parseLong(requestMap.get("id")));
                    return InventoryUtils.getResponseEntity("Warehouse status updated successfully. ", HttpStatus.OK);
                } else {
                    return InventoryUtils.getResponseEntity("Warehouse id doesn't exist", HttpStatus.OK);
                }
            } else {
                InventoryUtils.getResponseEntity(InventoryConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return InventoryUtils.getResponseEntity(InventoryConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        try {
            if (jwtRequestFilter.isAdmin()) {
                Optional optional = warehouseRepo.findById(id);
                if (!optional.isEmpty()) {
                    warehouseRepo.deleteById(id);
                    //System.out.println("Warehouse is deleted successfully");
                    return InventoryUtils.getResponseEntity("Warehouse is deleted successfully", HttpStatus.OK);
                }
                //System.out.println("Warehouse id doesn't exist");
                return InventoryUtils.getResponseEntity("Warehouse id doesn't exist", HttpStatus.OK);
            } else {
                return InventoryUtils.getResponseEntity(InventoryConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        System.out.println(InventoryConstants.SOMETHING_WENT_WRONG);
        return InventoryUtils.getResponseEntity(InventoryConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/getWarehouseById/{id}")
    public ResponseEntity<WarehouseWrapper> getWarehouseById(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(warehouseRepo.getWarehouseById(id), HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new WarehouseWrapper(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/getWarehouseByLocation/{id}")
    public ResponseEntity<List<WarehouseWrapper>> getWarehouseByLocation(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(warehouseRepo.getByLocation(id), HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private boolean validateWarehouseMap(Map<String, String> requestMap, boolean validateId) {
        if (requestMap.containsKey("name")) {
            if (requestMap.containsKey("id") && validateId) {
                return true;
            } else if (!validateId) {
                return true;
            }
        }
        return false;
    }

    private Warehouse getWarehouseFromMap(Map<String, String> requestMap, boolean isAdd) {
        Warehouse warehouse = new Warehouse();
        Location location = new Location();
        location.setId(Long.parseLong(requestMap.get("locationId")));

        if (isAdd) {
            warehouse.setId(Long.parseLong(requestMap.get("id")));
        } else {
            warehouse.setStatus(true);
        }
        warehouse.setLocation(location);
        warehouse.setName(requestMap.get("name"));
        warehouse.setIsRefrigerated(false);
        warehouse.setStatus(isAdd);

        return warehouse;
    }

}
