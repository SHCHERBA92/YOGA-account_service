package com.example.account_service.models.masters;

import com.example.account_service.enumeration.DistrictName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class District implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private DistrictName districtName;

    //    @ManyToOne(cascade = CascadeType.ALL)
    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinColumn(name = "id_city", referencedColumnName = "id")
    private City city;

    public District(DistrictName districtName) {
        this.districtName = districtName;
    }
}
