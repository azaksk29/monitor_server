package com.bundang.monitor.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Port {
    @Id
    @GeneratedValue
    @Setter
    private Long id;

    @Setter
    @NotNull
    private Integer number;

    @Setter
    @NotEmpty
    private String state;

    public void deativate() {
        state = "NONE";
    }

//    public String getInformation() {
//        return "Port "+ number + " on " + state;
//    }
}
