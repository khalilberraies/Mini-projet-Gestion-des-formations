package com.isi.gestion_formation.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "utilisateur")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Utilisateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Login obligatoire")
    @Column(unique = true)
    private String login;

    @NotBlank(message = "Mot de passe obligatoire")
    @Size(min = 8, message = "Mot de passe : 8 caractères minimum")
    private String password;

    @ManyToOne
    @JoinColumn(name = "id_role")
    private Role role;
}