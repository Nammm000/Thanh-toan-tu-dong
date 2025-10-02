package tech.getarrays.inventorymanager.controllers;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import tech.getarrays.inventorymanager.constents.InventoryConstants;
import tech.getarrays.inventorymanager.filters.JwtRequestFilter;
import tech.getarrays.inventorymanager.models.User;
import tech.getarrays.inventorymanager.repo.UserRepo;
import tech.getarrays.inventorymanager.util.InventoryUtils;
import tech.getarrays.inventorymanager.wrapper.UserWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserRepo userRepo;

    @Autowired
    JwtRequestFilter jwtRequestFilter;

    @GetMapping("/get")
    public ResponseEntity<List<UserWrapper>> getAllUser() {
        try {
            if (jwtRequestFilter.isAdmin()) {
                return new ResponseEntity<>(userRepo.getAllUser(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ArrayList<>(), HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("/update")
    public ResponseEntity<String> update(@RequestBody(required = true) Map<String, String> requestMap) {
        try {
            if (jwtRequestFilter.isAdmin()) {
                Optional<User> optional = userRepo.findById(Long.parseLong(requestMap.get("id")));
                if (!optional.isEmpty()) {
                    userRepo.updateStatus(requestMap.get("status"), Long.parseLong(requestMap.get("id")));
//                    sendEmailToAllAdmin(requestMap.get("status"), optional.get().getEmail(), userRepo.getAllAdmin("admin"));
                    return InventoryUtils.getResponseEntity("user updated successfully", HttpStatus.OK);
                } else {
                    return InventoryUtils.getResponseEntity("User Id doesn't exist ", HttpStatus.OK);
                }
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
                Optional optional = userRepo.findById(id);
                if (!optional.isEmpty()) {
                    userRepo.deleteById(id);
                    return InventoryUtils.getResponseEntity("User is deleted successfully", HttpStatus.OK);
                }
                return InventoryUtils.getResponseEntity("User id doesn't exist", HttpStatus.OK);
            } else {
                return InventoryUtils.getResponseEntity(InventoryConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return InventoryUtils.getResponseEntity(InventoryConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
