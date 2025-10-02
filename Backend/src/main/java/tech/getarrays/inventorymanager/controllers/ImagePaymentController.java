package tech.getarrays.inventorymanager.controllers;

import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;
import tech.getarrays.inventorymanager.constents.InventoryConstants;
import tech.getarrays.inventorymanager.filters.JwtRequestFilter;
import tech.getarrays.inventorymanager.models.POJO.Image;
import tech.getarrays.inventorymanager.models.POJO.Location;
import tech.getarrays.inventorymanager.repo.ImageRepo;
import tech.getarrays.inventorymanager.util.InventoryUtils;

import java.util.*;

@Slf4j
@RestController
@RequestMapping("/imagePaymentController")
public class ImagePaymentController {

    @Autowired
    ImageRepo imageRepo;

    @Autowired
    JwtRequestFilter jwtRequestFilter;

    @PostMapping(value = {"/addNew"}, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<String> addNewImage (@RequestPart("imageFile") MultipartFile[] file,
                                               @RequestPart("attachedTo") String attachedTo) {
        try {
//            Set<Image> images = uploadImage(file);
            Image image = new Image();
            image.setType(file[0].getContentType());
            image.setName(file[0].getOriginalFilename());
            image.setPicByte(file[0].getBytes());
            image.setAttachedTo(attachedTo);
            imageRepo.save(image);
            return InventoryUtils.getResponseEntity("Image Payment Added Successfully", HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        System.out.println(InventoryConstants.SOMETHING_WENT_WRONG);
        return InventoryUtils.getResponseEntity(InventoryConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }


//    public Set<Image> uploadImage(MultipartFile[] multipartFiles) throws IOException {
//        Set<Image> imageModels = new HashSet<>();
//        for (MultipartFile file: multipartFiles) {
//            Image imageModel = new Image(
//                file.getOriginalFilename(),
//                file.getContentType(),
//                file.getBytes()
//            );
//            imageModels.add(imageModel);
//        }
//        return imageModels;
//    }


//    @PostMapping("/add")
//    public ResponseEntity<String> addNewLocation(@RequestBody(required = true) Map<String, String> requestMap) {
//        log.info("Inside addNewLocation{}", requestMap);
//        try {
//            if (jwtRequestFilter.isAdmin()) {
//                if (validateLocationMap(requestMap, false)) {
//                    locationRepo.save(getLocationFromMap(requestMap , false));
//                    return InventoryUtils.getResponseEntity("Location Added Successfully", HttpStatus.OK);
//                }
//            } else {
//                return InventoryUtils.getResponseEntity(InventoryConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//        System.out.println(InventoryConstants.SOMETHING_WENT_WRONG);
//        return InventoryUtils.getResponseEntity(InventoryConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
//    }

//    private boolean validateLocationMap(Map<String, String> requestMap, boolean validateId) {
//        if (requestMap.containsKey("name")) {
//            if (requestMap.containsKey("id") && validateId) {
//                return true;
//            } else if(!validateId) {
//                return true;
//            }
//        }
//        return false;
//    }

//    private Location getLocationFromMap(Map<String, String> requestMap, boolean isAdd) {
//        Location location = new Location();
//        if (isAdd) {
//            location.setId(Long.parseLong(requestMap.get("id")));
//        }
//        location.setName(requestMap.get("name"));
//        location.setAddress(requestMap.get("address"));
//        return location;
//    }

    @GetMapping("/get")
    public ResponseEntity<List<Image>> getAllImagePayment(@RequestParam(required = false) String Value) {
        try {
            if(!Strings.isNullOrEmpty(Value) && Value.equalsIgnoreCase("true")) {
                return new ResponseEntity<List<Image>>(new ArrayList<>(), HttpStatus.OK);
            }
            return new ResponseEntity<>(imageRepo.findAll(), HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity<List<Image>>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        try {
            if (jwtRequestFilter.isAdmin()) {
                Optional optional = imageRepo.findById(id);
                if (!optional.isEmpty()) {
                    imageRepo.deleteById(id);
                    return InventoryUtils.getResponseEntity("Image is updated successfully", HttpStatus.OK);
                }
                return InventoryUtils.getResponseEntity("Image id doesn't exist", HttpStatus.OK);
            } else {
                return InventoryUtils.getResponseEntity(InventoryConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return InventoryUtils.getResponseEntity(InventoryConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
