package com.ssafy.api.repository;

import com.ssafy.api.domain.User;
import com.ssafy.core.code.JoinCode;
import com.ssafy.core.code.YNCode;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepoCommon {
    User findUserLogin(String uid, JoinCode type);

    User findByUid(String uid, YNCode isBind);

}






































