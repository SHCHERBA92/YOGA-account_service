package com.example.account_service.services;

import com.example.account_service.enumeration.Authorities;
import com.example.account_service.models.security.Account;
import com.example.account_service.repositoryes.AccountRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.Assert;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest

class AccountServiceTest {

    @InjectMocks
    private AccountService accountService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AccountRepository accountRepository;

    @Test
    @DisplayName(value = "Тестирование на добавления нового аккаунта")
    void addAccount(){
        Account account = mock(Account.class);
        Account currentAccount = new Account();

        account.setEmail("test@mail");
        account.setPassword("123");
        account.setAuthorities(Authorities.ROLE_MASTER);

        when(accountRepository.save(account)).thenReturn(currentAccount);

        Assert.isTrue(accountService.addNewAccount("test@mail", "123", Authorities.ROLE_MASTER), "Аккаунт создан");

//        verify(accountRepository).save(account);
    }

    @Test
    void getAccountByEmail() {
        Account account = new Account();
        account.setEmail("test@mail");
        when(accountRepository.findAccountByEmail("test@mail")).thenReturn(Optional.of(account));

        Account actual = accountService.getAccountByEmail("test@mail");
        assertNotNull(actual);
        assertEquals(actual, account);
        verify(accountRepository).findAccountByEmail("test@mail");
    }

    @Test
    void createAccountFor() {
        final Account account = new Account();

        account.setCode("1111");
        account.setPassword("123");
        account.setAuthorities(Authorities.ROLE_MASTER);
        when(passwordEncoder.encode(account.getPassword())).thenReturn("2222");
        var actual = accountService.createAccountFor(account, Authorities.ROLE_MASTER);

        assertEquals(account.getEmail(), actual.getEmail());
        assertEquals(account.getPassword(), actual.getPassword());
        assertNotNull(actual.getCode());
    }
}