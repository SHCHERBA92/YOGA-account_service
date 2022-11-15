package com.example.account_service.models.masters;


import com.example.account_service.enumeration.Rating;
import com.example.account_service.models.security.Account;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Participant implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String lastName;
    private int age;

    @Enumerated(EnumType.ORDINAL)
    private Rating rating;

    @OneToMany(mappedBy = "participant")
    List<Review> reviews;

    @Lob
    private String expectations;

    @ManyToOne
    @JoinColumn(name = "city_id", referencedColumnName = "id")
    private City city;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    private Account account;
}
