package com.example.account_service.models.security;

import com.example.account_service.enumeration.Authorities;
import com.example.account_service.models.masters.Master;
import com.example.account_service.models.masters.Participant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email")
    private String email;
    private String password;

    private boolean isEnable;
    private String code;

    @Enumerated(EnumType.STRING)
    private Authorities authorities;

    //    @OneToOne(mappedBy = "account", cascade = CascadeType.ALL)
    @OneToOne(mappedBy = "account", fetch = FetchType.LAZY)
    private Master master;

    //    @OneToOne(mappedBy = "account", cascade = CascadeType.ALL)
    @OneToOne(mappedBy = "account")
    private Participant participant;
}
