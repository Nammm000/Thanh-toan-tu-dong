package tech.getarrays.inventorymanager.controllers;

import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import tech.getarrays.inventorymanager.constents.InventoryConstants;
import tech.getarrays.inventorymanager.filters.JwtRequestFilter;
import tech.getarrays.inventorymanager.models.POJO.Customer;
import tech.getarrays.inventorymanager.repo.CustomerRepo;
import tech.getarrays.inventorymanager.util.InventoryUtils;
import tech.getarrays.inventorymanager.wrapper.CustomerWrapper;
import tech.getarrays.inventorymanager.wrapper.ProductWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    CustomerRepo customerRepo;

    @Autowired
    JwtRequestFilter jwtRequestFilter;

    @PostMapping("/add")
    public ResponseEntity<String> addNewCustomer(@RequestBody(required = true) Map<String, String> requestMap) {
        log.info("Inside addNewCustomer{}", requestMap);
        try {
            if (jwtRequestFilter.isAdmin()) {
                if (validateCustomerMap(requestMap, false)) {
                    customerRepo.save(getCustomerFromMap(requestMap, false));
                    return InventoryUtils.getResponseEntity("Customer Added Successfully", HttpStatus.OK);
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
    public ResponseEntity<List<Customer>> getAllCustomer(@RequestParam(required = false) String Value) {
        try {
            if(!Strings.isNullOrEmpty(Value) && Value.equalsIgnoreCase("true")) {
                return new ResponseEntity<List<Customer>>(new ArrayList<>(), HttpStatus.OK);
            }
            return new ResponseEntity<>(customerRepo.findAll(), HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity<List<Customer>>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/getContactNumbers")
    public ResponseEntity<List<CustomerWrapper>> getContactNumbers(@RequestParam(required = false) String Value) {
        try {
            if(!Strings.isNullOrEmpty(Value) && Value.equalsIgnoreCase("true")) {
                return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
            }
            return new ResponseEntity<>(customerRepo.findAllContactNumbers(), HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/getCustomerById/{id}")
    public ResponseEntity<CustomerWrapper> getCustomerById(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(customerRepo.getCustomerById(id), HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new CustomerWrapper(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("/update")
    public ResponseEntity<String> update(@RequestBody(required = true) Map<String, String> requestMap) {
        try {
            if (jwtRequestFilter.isAdmin()) {
                if (validateCustomerMap(requestMap , true)) {

                    Optional optional = customerRepo.findById(Long.parseLong(requestMap.get("id")));

                    if (!optional.isEmpty()) {
                        customerRepo.save(getCustomerFromMap(requestMap,true));
                        return InventoryUtils.getResponseEntity("Customer is updated successfully", HttpStatus.OK);

                    } else {
                        return InventoryUtils.getResponseEntity("Customer id doesn't exist", HttpStatus.OK);
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
                Optional optional = customerRepo.findById(id);
                if (!optional.isEmpty()) {
                    customerRepo.deleteById(id);
                    return InventoryUtils.getResponseEntity("Customer is updated successfully", HttpStatus.OK);
                }
                return InventoryUtils.getResponseEntity("Customer id doesn't exist", HttpStatus.OK);
            } else {
                return InventoryUtils.getResponseEntity(InventoryConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return InventoryUtils.getResponseEntity(InventoryConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private boolean validateCustomerMap(Map<String, String> requestMap, boolean validateId) {
        if (requestMap.containsKey("username")) {
            if (requestMap.containsKey("id") && validateId) {
                return true;
            } else if (!validateId) {
                return true;
            }
        }
        return false;
    }

    private Customer getCustomerFromMap(Map<String, String> requestMap, boolean isAdd) {
        Customer customer = new Customer();
        if (isAdd) {
            customer.setId(Long.parseLong(requestMap.get("id")));
        }
        customer.setUsername(requestMap.get("username"));
        customer.setContactNumber(requestMap.get("contactNumber"));
        customer.setAddress(requestMap.get("address"));
        return customer;
    }
}
