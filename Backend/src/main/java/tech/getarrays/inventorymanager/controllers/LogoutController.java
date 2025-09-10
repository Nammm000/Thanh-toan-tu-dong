package tech.getarrays.inventorymanager.controllers;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.getarrays.inventorymanager.constents.InventoryConstants;
import tech.getarrays.inventorymanager.dto.AuthenticationDTO;
import tech.getarrays.inventorymanager.dto.HelloResponse;
import tech.getarrays.inventorymanager.dto.LogoutResponse;
import tech.getarrays.inventorymanager.filters.JwtRequestFilter;
import tech.getarrays.inventorymanager.models.User;
import tech.getarrays.inventorymanager.repo.BillRepo;
import tech.getarrays.inventorymanager.repo.UserRepo;
import tech.getarrays.inventorymanager.util.InventoryUtils;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class LogoutController {

    @Autowired
    UserRepo userRepo;

    @Autowired
    JwtRequestFilter jwtRequestFilter;

    @PostMapping("/logout")
    public LogoutResponse deleteAuthenticationToken() {
        SecurityContextHolder.clearContext();
//        System.out.println("ok ok ok");
        return new LogoutResponse("Logout OK");

    }

    @PostMapping("/changePassword")
    public ResponseEntity<String> changePassword(@RequestBody Map<String, String> requestMap) {
//        return null;
        try {
            User user = userRepo.findFirstByEmail(jwtRequestFilter.getCurrentUsername());
            if (!user.equals(null)) {
                final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
                if (passwordEncoder.matches(requestMap.get("oldPassword"), user.getPassword())) {
                    System.out.println("LogoutController user.getPassword().equals(oldPass)");
                    user.setPassword(new BCryptPasswordEncoder().encode(requestMap.get("newPassword")));
//                    user.setPassword(requestMap.get("newPassword"));
                    userRepo.save(user);
                    return InventoryUtils.getResponseEntity("Password Updated Successfully", HttpStatus.OK);
                }
                return InventoryUtils.getResponseEntity("Incorrect Old Password", HttpStatus.BAD_REQUEST);
            }
            return InventoryUtils.getResponseEntity(InventoryConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return InventoryUtils.getResponseEntity(InventoryConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
