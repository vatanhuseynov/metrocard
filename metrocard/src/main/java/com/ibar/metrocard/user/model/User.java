package com.ibar.metrocard.user.model;
import com.ibar.metrocard.card.model.Card;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_gen")
    @SequenceGenerator(name = "user_gen", sequenceName = "user_seq", allocationSize = 1)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "pin", unique = true)
    private String pin;

    @Column(name = "password")
    private String password;

    @Column(name = "mail", unique = true)
    @Email(message = "Düzgün mail formatı daxil edin")
    private String mail;

    @Column(name = "mobile_number")
    private String mobileNumber;

    @Column(name = "balance")
    private BigDecimal balance;

    @OneToMany(mappedBy = "user")
    private List<Card> cards;

    @ManyToMany(fetch = LAZY , cascade = CascadeType.ALL)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"),
            uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "role_id"})} )
    private List<Role> roles;

}
