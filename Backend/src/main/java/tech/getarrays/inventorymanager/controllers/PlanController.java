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
import tech.getarrays.inventorymanager.repo.PlanRepo;
import tech.getarrays.inventorymanager.util.InventoryUtils;
import tech.getarrays.inventorymanager.wrapper.PlanCodeWrapper;
import tech.getarrays.inventorymanager.wrapper.PlanWrapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/plan")
public class PlanController {

    @Autowired
    PlanRepo planRepo;

    @Autowired
    JwtRequestFilter jwtRequestFilter;

    @PostMapping("/add")
    public ResponseEntity<String> addPlan(@RequestBody(required = true) Map<String, String> requestMap) {
        log.info("Inside addPlan{}", requestMap);
        try {
            if (jwtRequestFilter.isAdmin()) {
                if (validatePlanMap(requestMap, false)) {
                    planRepo.save(getPlanFromMap(requestMap , false));
                    return InventoryUtils.getResponseEntity("Plan Added Successfully", HttpStatus.OK);
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

    private boolean validatePlanMap(Map<String, String> requestMap, boolean validateId) {
        if (requestMap.containsKey("name")) {
            if (requestMap.containsKey("id") && validateId) {
                return true;
            } else if(!validateId) {
                return true;
            }
        }
        return false;
    }

    private Plan getPlanFromMap(Map<String, String> requestMap, boolean isAdd) {
        Plan plan = new Plan();
        LocalDateTime now = LocalDateTime.now();
        if (isAdd) {
            Long id = Long.parseLong(requestMap.get("id"));
            plan.setId(id);
            plan.setCreatedTime(LocalDateTime.parse(requestMap.get("createdTime")));
            // LocalDateTime.parse(requestMap.get("createdTime")) planRepo.findFirstById(id).getCreatedTime()
        } else {
            plan.setCreatedTime(now);
        }
        plan.setName(requestMap.get("name"));
        plan.setDescription(requestMap.get("description"));
        plan.setCreatedBy(jwtRequestFilter.getCurrentUsername());

        plan.setUpdatedTime(now);
        plan.setCode(requestMap.get("code"));
        plan.setPrice(Float.parseFloat(requestMap.get("price")));
        plan.setDuration(Integer.parseInt(requestMap.get("duration")));

        String sta = requestMap.get("status");
        if (sta != null && !sta.isEmpty()) {
            plan.setStatus(sta);
        } else {
            plan.setStatus("true");
        }

        return plan;
    }

    @GetMapping("/get")
    public ResponseEntity<List<Plan>> getAllPlans(@RequestParam(required = false) String Value) {
        try {
            if(!Strings.isNullOrEmpty(Value) && Value.equalsIgnoreCase("true")) {
                return new ResponseEntity<List<Plan>>(new ArrayList<>(), HttpStatus.OK);
            }
            return new ResponseEntity<>(planRepo.findAll(), HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity<List<Plan>>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/getAllPlanCode")
    public ResponseEntity<List<PlanCodeWrapper>> getAllPlanCode(@RequestParam(required = false) String Value) {
        try {
            if(!Strings.isNullOrEmpty(Value) && Value.equalsIgnoreCase("true")) {
                return new ResponseEntity<List<PlanCodeWrapper>>(new ArrayList<>(), HttpStatus.OK);
            }
            return new ResponseEntity<>(planRepo.getAllPlanCode(), HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity<List<PlanCodeWrapper>>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

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

    @PostMapping("/update")
    public ResponseEntity<String> update(@RequestBody(required = true) Map<String, String> requestMap) {
        try {
            if (jwtRequestFilter.isAdmin()) {
                if (validatePlanMap(requestMap , true)) {

                    Optional optional = planRepo.findById(Long.parseLong(requestMap.get("id")));

                    if (!optional.isEmpty()) {
                        planRepo.save(getPlanFromMap(requestMap,true));
                        return InventoryUtils.getResponseEntity("Plan is updated successfully", HttpStatus.OK);

                    } else {
                        return InventoryUtils.getResponseEntity("Plan id doesn't exist", HttpStatus.OK);
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
                    return InventoryUtils.getResponseEntity("Plan is deleted successfully", HttpStatus.OK);
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
