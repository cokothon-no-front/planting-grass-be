package com.nofront.plantinggrassbe.service;

import com.nofront.plantinggrassbe.DTO.UserDetails;
import com.nofront.plantinggrassbe.domain.User;
import com.nofront.plantinggrassbe.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class UserDetailsService {

    @Autowired
    private final UserRepository userRepository;

    public UserDetails findUser(String userId, String provider) throws UsernameNotFoundException {
        User user = userRepository
                .findByUsernameAndProvider(userId, provider)
                .orElseThrow(() -> new UsernameNotFoundException("cannot find such user"));

        return UserDetails.builder()
                .username(userId)
                .provider(provider)
                .isEnabled(true)
                .isExpired(false)
                .isLocked(false)
                .build();
    }


}
