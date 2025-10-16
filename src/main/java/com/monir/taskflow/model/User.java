package com.monir.taskflow.model;


import jakarta.persistence.*;
import lombok.*;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class User {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;


@Column(unique = true, nullable = false)
private String username;


@Column(nullable = false)
private String password; // stocker haché


@Enumerated(EnumType.STRING)
private Role role;
}