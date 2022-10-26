package com.example.account_service.services;

import com.example.account_service.enumeration.Authorities;
import com.example.account_service.models.masters.Master;
import com.example.account_service.models.security.Account;
import com.example.account_service.repositoryes.AccountRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    public AccountService(AccountRepository accountRepository, PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void addNewAccountMaster(String name, String password){
        Account account = new Account();
        account.setEmail(name);
        account.setPassword(passwordEncoder.encode(password));
        account.setAuthorities(Authorities.ROLE_MASTER);

        accountRepository.save(account);
    }

//    @Transactional(propagation = Propagation.MANDATORY)
    public Account addNewAccountMaster(Account account){
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        account.setAuthorities(Authorities.ROLE_MASTER);
        return accountRepository.save(account);
    }


}
