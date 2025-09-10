package tech.getarrays.inventorymanager.services.auth;

import tech.getarrays.inventorymanager.dto.SignupDTO;
import tech.getarrays.inventorymanager.dto.UserDTO;

public interface AuthService {
    UserDTO createUser(SignupDTO signupDTO);
}
