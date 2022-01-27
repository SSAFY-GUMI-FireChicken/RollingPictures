package com.ssafy.api.repository;

import com.ssafy.api.domain.ChannelUser;
import com.ssafy.api.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChannelUserRepository extends JpaRepository<ChannelUser, Long> {
    ChannelUser findByUser(User user);
}
