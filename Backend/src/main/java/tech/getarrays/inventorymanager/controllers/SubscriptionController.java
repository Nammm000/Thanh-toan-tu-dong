package tech.getarrays.inventorymanager.controllers;


import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.getarrays.inventorymanager.constents.InventoryConstants;
import tech.getarrays.inventorymanager.filters.JwtRequestFilter;
import tech.getarrays.inventorymanager.models.POJO.Plan;
import tech.getarrays.inventorymanager.models.POJO.Subscription;
import tech.getarrays.inventorymanager.models.User;
import tech.getarrays.inventorymanager.repo.PlanRepo;
import tech.getarrays.inventorymanager.repo.SubscriptionRepo;
import tech.getarrays.inventorymanager.repo.UserRepo;
import tech.getarrays.inventorymanager.util.InventoryUtils;
import tech.getarrays.inventorymanager.wrapper.PlanWrapper;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/subscription")
public class SubscriptionController {

    @Autowired
    SubscriptionRepo subscriptionRepo;

    @Autowired
    PlanRepo planRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    JwtRequestFilter jwtRequestFilter;

    @PostMapping("/add")
    public ResponseEntity<String> addSubscription(@RequestBody(required = true) Map<String, String> requestMap) {
        log.info("Inside addSubscription{}", requestMap);
        try {
            if (jwtRequestFilter.isAdmin()) {
                if (validatePlanMap(requestMap, false)) {
                    System.out.println("getSubscriptionFromMap(requestMap)");
                    Subscription sub = getSubscriptionFromMap(requestMap);
                    if (sub==null) {
                        return InventoryUtils.getResponseEntity("User is admin.", HttpStatus.UNAUTHORIZED);
                    }
                    subscriptionRepo.save(sub);
                    return InventoryUtils.getResponseEntity("Subscription Added Successfully", HttpStatus.OK);
                }
            } else {
                return InventoryUtils.getResponseEntity(InventoryConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
//        System.out.println(InventoryConstants.SOMETHING_WENT_WRONG);
        return InventoryUtils.getResponseEntity(InventoryConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private boolean validatePlanMap(Map<String, String> requestMap, boolean validateId) {
        if (requestMap.containsKey("email")) {
            if (requestMap.containsKey("id") && validateId) {
                return true;
            } else if(!validateId) {
                return true;
            }
        }
        return false;
    }

    private Subscription getSubscriptionFromMap(Map<String, String> requestMap) {
        Subscription subscription = new Subscription();
        LocalDateTime now = LocalDateTime.now();
//        if (isAdd) {
//            Long id = Long.parseLong(requestMap.get("id"));
//            plan.setId(id);
//            plan.setCreatedTime(planRepo.findFirstById(id).getCreatedTime());
//        } else {
//            plan.setCreatedTime(now);
//        }

        String em = requestMap.get("email");
        User.Role uR = userRepo.findFirstByEmail(em).getRole();
        if (User.Role.ADMIN.equals(uR)) {
            return null;
        }
        System.out.println("em"+em);
        subscription.setSubscriber(em);
        String plc = requestMap.get("plan_code");
        subscription.setPlan_code(plc);
        if (Objects.equals(plc, "ALL") || plc.isEmpty()) {
            subscription.setPrice((float) 0);
        } else {
            subscription.setPrice(planRepo.findFirstByCode(plc).getPrice());
            System.out.println("subscription planRepo.findFirstByCode(plc).getPrice(): "+planRepo.findFirstByCode(plc).getPrice());
        }

        subscription.setStartDate(now);
        subscription.setExpirationDate(now.plusDays(30));

        subscription.setStatus("true");

        return subscription;
    }

    @GetMapping("/get")
    public ResponseEntity<List<Subscription>> getAllSubscriptions(@RequestParam(required = false) String Value) {
        try {
            if(!Strings.isNullOrEmpty(Value) && Value.equalsIgnoreCase("true")) {
                return new ResponseEntity<List<Subscription>>(new ArrayList<>(), HttpStatus.OK);
            }
            return new ResponseEntity<>(subscriptionRepo.findAll(), HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity<List<Subscription>>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

//    @GetMapping("/getAllPlanCode")
//    public ResponseEntity<List<PlanWrapper>> getAllPlanCode(@RequestParam(required = false) String Value) {
//        try {
//            if(!Strings.isNullOrEmpty(Value) && Value.equalsIgnoreCase("true")) {
//                return new ResponseEntity<List<PlanWrapper>>(new ArrayList<>(), HttpStatus.OK);
//            }
//            return new ResponseEntity<>(planRepo.getAllPlanCode(), HttpStatus.OK);
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//        return new ResponseEntity<List<PlanWrapper>>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
//    }

    @GetMapping("/getPlanById/{id}")
    public ResponseEntity<PlanWrapper> getPlanById(@PathVariable Long id) {
        try {
            if (jwtRequestFilter.isAdmin()) {
                return new ResponseEntity<>(planRepo.getPlanById(id), HttpStatus.OK);
            }
            new ResponseEntity<PlanWrapper>(new PlanWrapper(), HttpStatus.UNAUTHORIZED);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity<PlanWrapper>(new PlanWrapper(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/getUserSub")
    public ResponseEntity<String> getUserSub() {
        log.info("Inside /news/getUserSubNews()");
        try {

            if (jwtRequestFilter.isUser()) {
                List<Subscription> listUserSub = subscriptionRepo.findAllByEmail(jwtRequestFilter.getCurrentUsername());
                if (!listUserSub.isEmpty()) {
                    Subscription subNow = listUserSub.get(0);
                    LocalDateTime now = LocalDateTime.now();
                    if ( subNow.getExpirationDate().compareTo(now) < 0 ) {
                        return InventoryUtils.getResponseEntity(subNow.getPlan_code(), HttpStatus.OK);
                    } else {
                        return new ResponseEntity<>("Gói đăng ký hết hạn vào ngày " + subNow.getExpirationDate(), HttpStatus.OK);
                    }
                } else {
                    return new ResponseEntity<>("Người dùng không đăng ký", HttpStatus.OK);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity<>(InventoryConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("/updateStatus")
    public ResponseEntity<String> updateStatus(@RequestBody(required = true) Map<String, String> requestMap) {
        try {
            if (jwtRequestFilter.isAdmin()) {
                Optional<Plan> optionalPlan = planRepo.findById(Long.parseLong(requestMap.get("id")));
                if (!optionalPlan.isEmpty()) {
                    planRepo.updatePlanStatus(requestMap.get("status"), Long.parseLong(requestMap.get("id")));
                    return InventoryUtils.getResponseEntity("Plan status updated successfully. ", HttpStatus.OK);
                } else {
                    return InventoryUtils.getResponseEntity("Plan id doesn't exist", HttpStatus.OK);
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
                Optional optional = planRepo.findById(id);
                if (!optional.isEmpty()) {
                    planRepo.deleteById(id);
                    return InventoryUtils.getResponseEntity("Plan is updated successfully", HttpStatus.OK);
                }
                return InventoryUtils.getResponseEntity("Plan id doesn't exist", HttpStatus.OK);
            } else {
                return InventoryUtils.getResponseEntity(InventoryConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return InventoryUtils.getResponseEntity(InventoryConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
