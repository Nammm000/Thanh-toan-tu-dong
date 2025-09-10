package tech.getarrays.inventorymanager.controllers;

import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.getarrays.inventorymanager.constents.InventoryConstants;
import tech.getarrays.inventorymanager.filters.JwtRequestFilter;
import tech.getarrays.inventorymanager.models.POJO.Product;
import tech.getarrays.inventorymanager.models.POJO.Provider;
import tech.getarrays.inventorymanager.repo.ProviderRepo;
import tech.getarrays.inventorymanager.util.InventoryUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/provider")
public class ProviderController {

    @Autowired
    ProviderRepo providerRepo;

    @Autowired
    JwtRequestFilter jwtRequestFilter;

    @PostMapping("/add")
    public ResponseEntity<String> addNewProvider(@RequestBody(required = true) Map<String, String> requestMap) {
        log.info("Inside addNewProvider{}", requestMap);
        try {
            if (jwtRequestFilter.isAdmin()) {
                if (validateProviderMap(requestMap, false)) {
                    providerRepo.save(getProviderFromMap(requestMap , false));
                    return InventoryUtils.getResponseEntity("Provider Added Successfully", HttpStatus.OK);
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

    private boolean validateProviderMap(Map<String, String> requestMap, boolean validateId) {
        if (requestMap.containsKey("name")) {
            if (requestMap.containsKey("id") && validateId) {
                return true;
            } else if(!validateId) {
                return true;
            }
        }
        return false;
    }

    private Provider getProviderFromMap(Map<String, String> requestMap, boolean isAdd) {
        Provider provider = new Provider();
        if (isAdd) {
            provider.setId(Long.parseLong(requestMap.get("id")));
        }
        provider.setName(requestMap.get("name"));
        provider.setAddress(requestMap.get("address"));
        return provider;
    }

    @GetMapping("/get")
    public ResponseEntity<List<Provider>> getAllProvider(@RequestParam(required = false) String Value) {
        try {
            if(!Strings.isNullOrEmpty(Value) && Value.equalsIgnoreCase("true")) {
                return new ResponseEntity<List<Provider>>(new ArrayList<>(), HttpStatus.OK);
            }
            return new ResponseEntity<>(providerRepo.findAll(), HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity<List<Provider>>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("/update")
    public ResponseEntity<String> update(@RequestBody(required = true) Map<String, String> requestMap) {
        try {
            if (jwtRequestFilter.isAdmin()) {
                if (validateProviderMap(requestMap , true)) {

                    Optional optional = providerRepo.findById(Long.parseLong(requestMap.get("id")));

                    if (!optional.isEmpty()) {
                        providerRepo.save(getProviderFromMap(requestMap,true));
                        return InventoryUtils.getResponseEntity("Provider is updated successfully", HttpStatus.OK);

                    } else {
                        return InventoryUtils.getResponseEntity("Provider id doesn't exist", HttpStatus.OK);
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
                Optional<Provider> optionalProduct = providerRepo.findById(Long.parseLong(requestMap.get("id")));
                if (!optionalProduct.isEmpty()) {
                    providerRepo.updateProviderStatus(Boolean.parseBoolean(requestMap.get("status")), Long.parseLong(requestMap.get("id")));
                    return InventoryUtils.getResponseEntity("Product status updated successfully. ", HttpStatus.OK);
                } else {
                    return InventoryUtils.getResponseEntity("product id doesn't exist", HttpStatus.OK);
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
                Optional optional = providerRepo.findById(id);
                if (!optional.isEmpty()) {
                    providerRepo.deleteById(id);
                    return InventoryUtils.getResponseEntity("Provider is updated successfully", HttpStatus.OK);
                }
                return InventoryUtils.getResponseEntity("Provider id doesn't exist", HttpStatus.OK);
            } else {
                return InventoryUtils.getResponseEntity(InventoryConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return InventoryUtils.getResponseEntity(InventoryConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
