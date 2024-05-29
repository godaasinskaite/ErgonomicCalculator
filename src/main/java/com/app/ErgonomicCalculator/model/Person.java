package com.app.ErgonomicCalculator.model;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    @NotNull
    private String email;

    private String firstName;
    private String lastName;
    private String password;

    @OneToOne(mappedBy = "person", cascade = CascadeType.ALL)
    private PersonAnthropometrics personAnthropometrics;

    @OneToOne(mappedBy = "person", cascade = CascadeType.REMOVE)
    private WorkspaceMetrics workspaceMetrics;

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
