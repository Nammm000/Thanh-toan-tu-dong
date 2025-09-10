package tech.getarrays.inventorymanager.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.getarrays.inventorymanager.repo.BillRepo;
import tech.getarrays.inventorymanager.repo.CategoryRepo;
import tech.getarrays.inventorymanager.repo.ProductRepo;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    CategoryRepo categoryRepo;

    @Autowired
    ProductRepo productRepo;

    @Autowired
    BillRepo billRepo;

    @GetMapping("/details")
    public ResponseEntity<Map<String, Object>> getCount() {
        System.out.println("DashboardController getCount");

        Map<String, Object> map = new HashMap<>();
        map.put("category", categoryRepo.count());
        map.put("product", productRepo.count());
        map.put("bill", billRepo.count());

        return new ResponseEntity<>(map, HttpStatus.OK);
    }
}
