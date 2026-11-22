package com.task.gymproject.service.api;

import com.task.gymproject.model.User;

public interface UserService {

    void initializeUser(final User user,
                        final String firstName,
                        final String lastName);
}
