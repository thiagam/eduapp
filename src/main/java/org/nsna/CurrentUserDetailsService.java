package org.nsna;

import org.nsna.domain.User;
import org.nsna.domain.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CurrentUserDetailsService implements UserDetailsService {
	@Autowired
	UserRepository userRepository;

    @Override
    public CurrentUser loadUserByUsername(String userName) throws UsernameNotFoundException {
        User user = userRepository.findOneByUserName(userName)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User with User Name=%s was not found", userName)));
        return new CurrentUser(user);
    }
}
