package com.example.account_service.models.masters;

import com.example.account_service.enumeration.CityName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class City implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private CityName nameOfCity;

    //    @OneToMany(mappedBy = "city", cascade = CascadeType.ALL)
    @OneToMany(mappedBy = "city")
//    @JsonManagedReference
    private List<Master> master;

    @OneToMany(mappedBy = "city")
    private List<Participant> participant;

    @OneToMany(mappedBy = "city")
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<District> districts;

    public City(CityName cityName) {
        this.nameOfCity = cityName;
    }
}
