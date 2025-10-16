package com.monir.taskflow.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;


import java.time.LocalDate;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Task {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;


@NotBlank
private String titre;


private String description;


private LocalDate dateCreation;


private LocalDate dateEcheance;


@Enumerated(EnumType.STRING)
private Statut statut;

@PrePersist
public void prePersist() {
if (dateCreation == null) dateCreation = LocalDate.now();
if (statut == null) statut = Statut.TODO;
}
}