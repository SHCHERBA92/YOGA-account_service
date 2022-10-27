package com.example.account_service.services.security;

import com.example.account_service.repositoryes.AccountRepository;
import com.example.account_service.security.AccountDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AccountDetailsService implements UserDetailsService {

    private final AccountRepository accountRepository;

    public AccountDetailsService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //TODO: сделать кастомный запрос , так как болшьшой запрос от hibernate для взятия аккаунта из БД
        var user = accountRepository.findByEmail(username).orElseThrow(() -> new RuntimeException("Такой пользователь не найдень"));
        return new AccountDetails(user);
    }

}
