package com.bundang.monitor.domain;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Port {
    @Id
    @GeneratedValue
    private Long id;

    @Setter
    @NotNull
    private Integer number;

    @Setter
    @NotEmpty
    private String state;

    @Setter
    @CreationTimestamp
    private LocalDateTime date;

    public void deativate() {
        state = "NONE";
    }
}
