package tech.getarrays.inventorymanager.controllers;

import com.google.common.base.Strings;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import tech.getarrays.inventorymanager.constents.InventoryConstants;
import tech.getarrays.inventorymanager.dto.AuthenticationDTO;
import tech.getarrays.inventorymanager.dto.AuthenticationResponse;
import tech.getarrays.inventorymanager.dto.SignupDTO;
import tech.getarrays.inventorymanager.dto.UserDTO;
import tech.getarrays.inventorymanager.models.User;
import tech.getarrays.inventorymanager.repo.UserRepo;
import tech.getarrays.inventorymanager.services.auth.AuthService;
import tech.getarrays.inventorymanager.services.jwt.UserDetailsServiceImpl;
import tech.getarrays.inventorymanager.util.EmailUtil;
import tech.getarrays.inventorymanager.util.InventoryUtils;
import tech.getarrays.inventorymanager.util.JwtUtil;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private AuthService authService;

    @Autowired
    private EmailUtil emailUtil;

    @Autowired
    private UserRepo userRepo;

    @PostMapping("/login")
    public AuthenticationResponse createAuthenticationToken(@RequestBody AuthenticationDTO authenticationDTO, HttpServletResponse response) throws BadCredentialsException, DisabledException, UsernameNotFoundException, IOException {
        System.out.println("AuthenticationController createAuthenticationToken");
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationDTO.getEmail(), authenticationDTO.getPassword()));
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Incorrect username or password!");
        } catch (DisabledException disabledException) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "User is not activated");
            return null;
        }

        String email = authenticationDTO.getEmail();
        final UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        final User.Role role = userRepo.findFirstByEmail(email).getRole();// doi sau

//        System.out.println("userDetails.getAuthorities() " + userDetails.getAuthorities());
//        System.out.println("userDetails.getUsername() " + userDetails.getUsername());
        final String jwt = jwtUtil.generateToken(userDetails.getUsername(), role); //userDetails.getAuthorities()

        return new AuthenticationResponse(jwt);
    }

    @PostMapping("/sign-up")
    public ResponseEntity<?> signupUser(@RequestBody SignupDTO signupDTO) {
        System.out.println("SignupController signupUser");
        UserDTO createdUser = authService.createUser(signupDTO);
        System.out.println("SignupController authService.createUser(signupDTO);");
        if (createdUser == null) {
            return new ResponseEntity<>("User not created, come again later!", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @PostMapping("/forgotPassword")
    public ResponseEntity<String> forgetPassword(@RequestBody Map<String, String> requestMap) {
        try {
            String email = requestMap.get("email");
            User user = userRepo.findFirstByEmail(email);
            System.out.println("user email is: " + email);
            if (!Objects.isNull(user) && !Strings.isNullOrEmpty(user.getEmail())) {
                System.out.println("!Objects.isNull(user) && !Strings.isNullOrEmpty(user.getEmail())");
                emailUtil.forgetMail(email, "Credentials by Inventory Management System" , user.getPassword());
                return InventoryUtils.getResponseEntity("Check Your mail for Credentials", HttpStatus.OK);
            } else if (Objects.isNull(user)) {
                return InventoryUtils.getResponseEntity("Check your mail for credentials.", HttpStatus.OK);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return InventoryUtils.getResponseEntity(InventoryConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/hello")
    public String hello() {
        return "Hello";
    }
}
