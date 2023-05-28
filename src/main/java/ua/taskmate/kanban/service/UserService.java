package ua.taskmate.kanban.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.taskmate.kanban.entity.User;
import ua.taskmate.kanban.exception.ResourceNotFoundException;
import ua.taskmate.kanban.repository.UserRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User getUserById(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    public User getUserBySub(String sub) {
        return userRepository.findUserBySub(sub)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Transactional
    public void saveUser(User user) {
        userRepository.save(user);
    }

    public boolean existsUserBySub(String sub) {
        return userRepository.existsUserBySub(sub);
    }

    @Transactional
    public User updateUserBySub(String sub, User updated) {
        User userToUpdate = getUserBySub(sub);
        userToUpdate.setEmail(updated.getEmail());
        userToUpdate.setProfileImageUrl(updated.getProfileImageUrl());
        userToUpdate.setFistName(updated.getFistName());
        userToUpdate.setLastName(updated.getLastName());
        return userToUpdate;
    }
}
