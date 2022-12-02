package com.example.account_service.models.masters;

import com.example.account_service.enumeration.Rating;
import com.example.account_service.models.security.Account;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class Master implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String lastName;
    private int age;
    @Lob
    private String description;

    private int experience;

    private int price;

    @Enumerated(EnumType.ORDINAL)
    private Rating rating;

    @OneToMany(mappedBy = "master", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    List<Review> reviews;

    @ManyToOne(cascade = CascadeType.ALL)
//    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    @JoinColumn(name = "city_id", referencedColumnName = "id")
//    @JsonBackReference
    private City city;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    @JsonIgnore
    private Account account;
}
