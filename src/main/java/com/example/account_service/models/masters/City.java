package com.example.account_service.models.masters;

import com.example.account_service.enumeration.CityName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private CityName cityName;

//    @OneToMany(mappedBy = "city", cascade = CascadeType.ALL)
    @OneToMany(mappedBy = "city")
    private List<Master> master;

    @OneToMany(mappedBy = "city")
    private List<Participant> participant;

    @OneToMany(mappedBy = "city")
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    private List<District> districts;


}
