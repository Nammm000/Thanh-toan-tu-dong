package tech.getarrays.inventorymanager.controllers;

import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import tech.getarrays.inventorymanager.constents.InventoryConstants;
import tech.getarrays.inventorymanager.filters.JwtRequestFilter;
import tech.getarrays.inventorymanager.models.POJO.Location;
import tech.getarrays.inventorymanager.repo.LocationRepo;
import tech.getarrays.inventorymanager.util.InventoryUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/location")
public class LocationController {

    @Autowired
    LocationRepo locationRepo;

    @Autowired
    JwtRequestFilter jwtRequestFilter;

    @PostMapping("/add")
    public ResponseEntity<String> addNewLocation(@RequestBody(required = true) Map<String, String> requestMap) {
        log.info("Inside addNewLocation{}", requestMap);
        try {
            if (jwtRequestFilter.isAdmin()) {
                if (validateLocationMap(requestMap, false)) {
                    locationRepo.save(getLocationFromMap(requestMap , false));
                    return InventoryUtils.getResponseEntity("Location Added Successfully", HttpStatus.OK);
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

    private boolean validateLocationMap(Map<String, String> requestMap, boolean validateId) {
        if (requestMap.containsKey("name")) {
            if (requestMap.containsKey("id") && validateId) {
                return true;
            } else if(!validateId) {
                return true;
            }
        }
        return false;
    }

    private Location getLocationFromMap(Map<String, String> requestMap, boolean isAdd) {
        Location location = new Location();
        if (isAdd) {
            location.setId(Long.parseLong(requestMap.get("id")));
        }
        location.setName(requestMap.get("name"));
        location.setAddress(requestMap.get("address"));
        return location;
    }

    @GetMapping("/get")
    public ResponseEntity<List<Location>> getAllLocation(@RequestParam(required = false) String Value) {
        try {
            if(!Strings.isNullOrEmpty(Value) && Value.equalsIgnoreCase("true")) {
                return new ResponseEntity<List<Location>>(new ArrayList<>(), HttpStatus.OK);
            }
            return new ResponseEntity<>(locationRepo.findAll(), HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity<List<Location>>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("/update")
    public ResponseEntity<String> update(@RequestBody(required = true) Map<String, String> requestMap) {
        try {
            if (jwtRequestFilter.isAdmin()) {
                if (validateLocationMap(requestMap , true)) {

                    Optional optional = locationRepo.findById(Long.parseLong(requestMap.get("id")));

                    if (!optional.isEmpty()) {
                        locationRepo.save(getLocationFromMap(requestMap,true));
                        return InventoryUtils.getResponseEntity("Location is updated successfully", HttpStatus.OK);

                    } else {
                        return InventoryUtils.getResponseEntity("Location id doesn't exist", HttpStatus.OK);
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

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        try {
            if (jwtRequestFilter.isAdmin()) {
                Optional optional = locationRepo.findById(id);
                if (!optional.isEmpty()) {
                    locationRepo.deleteById(id);
                    return InventoryUtils.getResponseEntity("Location is updated successfully", HttpStatus.OK);
                }
                return InventoryUtils.getResponseEntity("Location id doesn't exist", HttpStatus.OK);
            } else {
                return InventoryUtils.getResponseEntity(InventoryConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return InventoryUtils.getResponseEntity(InventoryConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
