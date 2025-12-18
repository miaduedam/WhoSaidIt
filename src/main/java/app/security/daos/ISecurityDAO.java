package app.security.daos;

import app.exceptions.EntityNotFoundException;
import app.exceptions.ValidationException;
import app.security.entities.User;
import dk.bugelhartmann.UserDTO;

public interface ISecurityDAO {
    UserDTO getVerifiedUser(String username, String password) throws ValidationException;
    User createUser(String username, String password);
    User addRole(UserDTO user, String newRole);
}