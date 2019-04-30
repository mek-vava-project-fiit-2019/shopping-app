package com.example.shoppingapp.ui.login;

import android.support.annotation.Nullable;

import com.example.shoppingapp.model.entity.User;

/**
 * Authentication result : success (user details) or error message.
 */
class LoginResult {
    @Nullable
    private User success;
    @Nullable
    private Integer error;

    LoginResult(@Nullable Integer error) {
        this.error = error;
    }

    LoginResult(@Nullable User success) {
        this.success = success;
    }

    @Nullable
    User getSuccess() {
        return success;
    }

    @Nullable
    Integer getError() {
        return error;
    }
}
