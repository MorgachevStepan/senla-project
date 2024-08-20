package com.stepanew.senlaproject.domain.entity;

import com.stepanew.senlaproject.domain.enums.RoleType;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Table(name = "roles")
@ToString
@Entity
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", unique = true)
    @Enumerated(value = EnumType.STRING)
    private RoleType name;

}
