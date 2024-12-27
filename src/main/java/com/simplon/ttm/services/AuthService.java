package com.simplon.ttm.services;

import com.simplon.ttm.dto.LoginDto;

public interface AuthService {
    String login(LoginDto loginDto);
}
