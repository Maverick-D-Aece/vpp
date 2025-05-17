package org.hamal.abhishek.vpp.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table("battery")
public class Battery {

    private UUID id;
    private String name;
    private int postCode;
    private int wattCapacity;

    public Battery(String name, int postCode, int wattCapacity) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.postCode = postCode;
        this.wattCapacity = wattCapacity;
    }

}
