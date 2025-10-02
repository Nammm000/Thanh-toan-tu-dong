package tech.getarrays.inventorymanager.controllers;


import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.getarrays.inventorymanager.constents.InventoryConstants;
import tech.getarrays.inventorymanager.filters.JwtRequestFilter;
import tech.getarrays.inventorymanager.models.POJO.News;
import tech.getarrays.inventorymanager.repo.NewsRepo;
import tech.getarrays.inventorymanager.repo.PlanRepo;
import tech.getarrays.inventorymanager.util.InventoryUtils;
import tech.getarrays.inventorymanager.wrapper.NewsWrapper;
import tech.getarrays.inventorymanager.wrapper.PlanWrapper;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/news")
public class NewsController {

    @Autowired
    NewsRepo newsRepo;

    @Autowired
    PlanRepo planRepo;

    @Autowired
    JwtRequestFilter jwtRequestFilter;

    @PostMapping("/add")
    public ResponseEntity<String> addNews(@RequestBody(required = true) Map<String, String> requestMap) {
        log.info("Inside addNews{}", requestMap);
        try {
            if (jwtRequestFilter.isAdmin()) {
                if (validateNewsMap(requestMap, false)) {
                    newsRepo.save(getNewsFromMap(requestMap , false));
                    return InventoryUtils.getResponseEntity("News Added Successfully", HttpStatus.OK);
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

    private boolean validateNewsMap(Map<String, String> requestMap, boolean validateId) {
        if (requestMap.containsKey("title")) {
            if (requestMap.containsKey("id") && validateId) {
                return true;
            } else if(!validateId) {
                return true;
            }
        }
        return false;
    }

    private News getNewsFromMap(Map<String, String> requestMap, boolean isAdd) {
        News news = new News();
        LocalDateTime now = LocalDateTime.now();
        if (isAdd) {
            Long id = Long.parseLong(requestMap.get("id"));
            news.setId(id);
            news.setCreatedTime(LocalDateTime.parse(requestMap.get("createdTime")));
        } else {
            news.setCreatedTime(now);
        }
        news.setTitle(requestMap.get("title"));
        news.setDescription(requestMap.get("description"));
        news.setContent(requestMap.get("content"));
        String plc = requestMap.get("plan_code");
        news.setPlan_code(plc);
        if (Objects.equals(plc, "ALL") || plc.isEmpty()) {
            news.setPrice((float) 0);
        } else {
            news.setPrice(planRepo.findFirstByCode(plc).getPrice());
            System.out.println("news planRepo.findFirstByCode(plc).getPrice(): "+planRepo.findFirstByCode(plc).getPrice());
        }

        System.out.println("jwtRequestFilter.getCurrentUsername(): "+jwtRequestFilter.getCurrentUsername());
        news.setAuthor(jwtRequestFilter.getCurrentUsername());


        news.setUpdatedTime(now);
        news.setStatus("true");
        news.setView(0);
        return news;
    }

    @GetMapping("/get")
    public ResponseEntity<List<News>> getAllNews(@RequestParam(required = false) String Value) {
        try {
            if(!Strings.isNullOrEmpty(Value) && Value.equalsIgnoreCase("true")) {
                return new ResponseEntity<List<News>>(new ArrayList<>(), HttpStatus.OK);
            }
            return new ResponseEntity<>(newsRepo.findAll(), HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity<List<News>>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/getNewsById/{id}")
    public ResponseEntity<NewsWrapper> getNewsById(@PathVariable Long id) {
        try {
            if (jwtRequestFilter.isAdmin()) {
                return new ResponseEntity<>(newsRepo.getNewsById(id), HttpStatus.OK);
            }
            new ResponseEntity<NewsWrapper>(new NewsWrapper(), HttpStatus.UNAUTHORIZED);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity<NewsWrapper>(new NewsWrapper(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("/update")
    public ResponseEntity<String> update(@RequestBody(required = true) Map<String, String> requestMap) {
        try {
            if (jwtRequestFilter.isAdmin()) {
                if (validateNewsMap(requestMap , true)) {

                    Optional optional = newsRepo.findById(Long.parseLong(requestMap.get("id")));

                    if (!optional.isEmpty()) {
                        newsRepo.save(getNewsFromMap(requestMap,true));
                        return InventoryUtils.getResponseEntity("News is updated successfully", HttpStatus.OK);

                    } else {
                        return InventoryUtils.getResponseEntity("News id doesn't exist", HttpStatus.OK);
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
                Optional<News> optionalNews = newsRepo.findById(Long.parseLong(requestMap.get("id")));
                if (!optionalNews.isEmpty()) {
                    newsRepo.updateNewsStatus(requestMap.get("status"), Long.parseLong(requestMap.get("id")));
                    return InventoryUtils.getResponseEntity("News status updated successfully. ", HttpStatus.OK);
                } else {
                    return InventoryUtils.getResponseEntity("News id doesn't exist", HttpStatus.OK);
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
                Optional optional = newsRepo.findById(id);
                if (!optional.isEmpty()) {
                    newsRepo.deleteById(id);
                    return InventoryUtils.getResponseEntity("News is deleted successfully", HttpStatus.OK);
                }
                return InventoryUtils.getResponseEntity("News id doesn't exist", HttpStatus.OK);
            } else {
                return InventoryUtils.getResponseEntity(InventoryConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return InventoryUtils.getResponseEntity(InventoryConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
