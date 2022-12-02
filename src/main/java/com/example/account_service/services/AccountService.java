package com.example.account_service.services;

import com.example.account_service.enumeration.Authorities;
import com.example.account_service.exceptions.simple_exception.NewAccountException;
import com.example.account_service.models.security.Account;
import com.example.account_service.repositoryes.AccountRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.util.StringUtils.hasLength;

import java.util.Random;

@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    public AccountService(AccountRepository accountRepository, PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean addNewAccount(String name, String password, Authorities authorities) {
        if (!hasLength(name) && !hasLength(password))
            throw new NewAccountException("Отсутствуют или пустые имя/пароль");
        if (password.length() < 3)
            throw new NewAccountException("Пароль меньше 3 символо");    // TODO: подправить на 6 символов.
        if (authorities == null) throw new NewAccountException("Для аккаунта " + name + " не определа роль!");
        Account account = new Account();
        account.setEmail(name);
        account.setPassword(passwordEncoder.encode(password));
        account.setAuthorities(authorities);

        accountRepository.save(account);
        return true;
    }


    @Transactional
    public Account getAccountByEmail(String email) {
        return accountRepository.findAccountByEmail(email).orElseThrow(() -> new NewAccountException("Такой пользователь не найден!"));
    }

    public Account updateAccount(Account account) {
        return accountRepository.save(account);
    }

    public Account createAccountFor(Account account, Authorities authorities) {
        if (account == null || authorities == null) throw new NewAccountException("Отустствует аккаунт");
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        account.setAuthorities(authorities);
        account.setEnable(false);
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
