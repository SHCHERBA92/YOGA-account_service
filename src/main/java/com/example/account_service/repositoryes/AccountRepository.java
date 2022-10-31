package com.example.account_service.repositoryes;

import com.example.account_service.models.security.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    @Query(value = "select * from account left join " +
            "(select * from master left join city c on master.city_id = c.id) m " +
            "on account.id = m.account_id " +
            "left join " +
            "(SELECT * from participant left join city c on participant.city_id = c.id) p " +
            "on account.id = p.account_id " +
            "where email=?1", nativeQuery = true)
    Optional<Account> gfAccount(String name);


    Optional<Account> findAccountByEmail(String email);

//    select * from account left join
//            (SELECT * from master  left join city c on master.city_id = c.id) m
//    on account.id = m.account_id
//    left join
//            (SELECT * from participant left join city c on participant.city_id = c.id) p
//    on account.id = p.account_id
//    where email='bobik2@mail' ;

//    @Query(value = "select a FROM Account a" +
//        " left join a.master " +
//        "left join a.participant " +
//        "where a.email =:name")
//    Account gfAccount(String name);

}


//  select * from account left join master on account.id = master.account_id left join participant p on account.id = p.account_id where email='bobik2@mail' ;



//@Query(value = "select a FROM Account a" +
//        " left join a.master " +
//        "left join a.participant " +
//        "where a.email = ?1")