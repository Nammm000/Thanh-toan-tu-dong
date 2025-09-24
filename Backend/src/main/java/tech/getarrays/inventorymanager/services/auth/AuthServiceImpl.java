package tech.getarrays.inventorymanager.services.auth;

import tech.getarrays.inventorymanager.dto.SignupDTO;
import tech.getarrays.inventorymanager.dto.UserDTO;
import tech.getarrays.inventorymanager.models.User;
import tech.getarrays.inventorymanager.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDTO createUser(SignupDTO signupDTO) {
        User user = new User();
        user.setName(signupDTO.getName());
        user.setEmail(signupDTO.getEmail());
        user.setPhone(signupDTO.getPhone());
        user.setRole(User.Role.USER); // chu y
        user.setStatus("true");
        user.setPassword(new BCryptPasswordEncoder().encode(signupDTO.getPassword()));
        user.setCreatedTime(LocalDateTime.now());
        User createdUser = userRepo.save(user);
//        System.out.println("HELLO " + User.Role.ADMIN);

        UserDTO userDTO = new UserDTO();
        userDTO.setId(createdUser.getId());
        userDTO.setEmail(createdUser.getEmail());
        userDTO.setPhone(createdUser.getPhone());
        userDTO.setName(createdUser.getName());
        return userDTO;
    }
}
