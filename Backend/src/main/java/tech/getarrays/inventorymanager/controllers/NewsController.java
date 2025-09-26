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
import tech.getarrays.inventorymanager.util.InventoryUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/news")
public class NewsController {

    @Autowired
    NewsRepo newsRepo;

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
        if (requestMap.containsKey("name")) {
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
        if (isAdd) {
            news.setId(Long.parseLong(requestMap.get("id")));
        }
        news.setTitle(requestMap.get("title"));
        news.setDescription(requestMap.get("description"));
        news.setContent(requestMap.get("content"));
        news.setPlan_code(requestMap.get("plan_code"));
        news.setAuthor(jwtRequestFilter.getCurrentUsername());

        LocalDateTime now = LocalDateTime.now();
        news.setCreatedTime(now);
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
                    return InventoryUtils.getResponseEntity("News is updated successfully", HttpStatus.OK);
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
