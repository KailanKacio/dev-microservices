package academy.devdojo.springboot.service;

import academy.devdojo.springboot.repository.BookUsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookUsersDetailsService implements UserDetailsService {
    private final BookUsersRepository bookUsersRepository;

    @Override
    public UserDetails loadUserByUsername(String userName) {
        return Optional.ofNullable(bookUsersRepository.findByUsername(userName))
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
