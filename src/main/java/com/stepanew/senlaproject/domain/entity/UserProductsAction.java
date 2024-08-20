package com.stepanew.senlaproject.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.stepanew.senlaproject.domain.enums.ActionType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Table(name = "user_products_actions")
@Entity
public class UserProductsAction {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "action_seq")
    @SequenceGenerator(name = "action_seq", sequenceName = "action_seq", allocationSize = 50)
    @Column(name = "id")
    private Long id;

    @Column(name = "email")
    private String email;

    @Column(name = "product")
    private String product;

    @Column(name = "action_type")
    @Enumerated(value = EnumType.STRING)
    private ActionType actionType;

    @Column(name = "action_date")
    @JsonFormat(pattern = "dd.MM.yyyy HH:mm:ss")
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime actionDate;

}