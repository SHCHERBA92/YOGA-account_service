package com.example.account_service.services;

import com.example.account_service.enumeration.Authorities;
import com.example.account_service.models.masters.Master;
import com.example.account_service.models.security.Account;
import com.example.account_service.repositoryes.AccountRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    public AccountService(AccountRepository accountRepository, PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void addNewAccount(String name, String password, Authorities authorities){
        Account account = new Account();
        account.setEmail(name);
        account.setPassword(passwordEncoder.encode(password));
        account.setAuthorities(authorities);

        accountRepository.save(account);
    }

//    @Transactional(propagation = Propagation.MANDATORY)
    public Account addNewAccountMaster(Account account){
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        account.setAuthorities(Authorities.ROLE_MASTER);
        return accountRepository.save(account);
    }

    public Account createAccountFor(Account account, Authorities authorities) {
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        account.setAuthorities(authorities);
        //TODO: потом поставить false и менять только тогда, когда пользователь введёт секретное слово
        account.setEnable(true);
        account.setCode(this.generateCode());
        return account;
    }

    private String generateCode() {
        StringBuilder builder = new StringBuilder("");
        for (int i = 0; i < 4; i++) {
            builder.append(new Random().nextInt(10));
        }
        return builder.toString();
    }
}
