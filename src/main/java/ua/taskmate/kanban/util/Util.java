package ua.taskmate.kanban.util;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class Util {

    public static UserDetails getPrincipal() {
        return (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
    }

}
