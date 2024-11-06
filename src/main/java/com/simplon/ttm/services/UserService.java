package com.simplon.ttm.services;

import com.simplon.ttm.dto.RegisterDto;
import com.simplon.ttm.models.User;


public interface UserService {
    User saveGodparent(RegisterDto userMapping);

    User saveLeaderProject(RegisterDto userMapping);
}
