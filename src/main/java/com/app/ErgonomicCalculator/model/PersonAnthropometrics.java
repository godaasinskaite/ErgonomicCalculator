package com.app.ErgonomicCalculator.model;

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
public class PersonAnthropometrics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double height;
    private Double sittingHeight;
    private Double shoulderHeight;
    private Double lowerLegLength;
    private Double hipBreadth;
    private Double elbowHeight;
    private Double eyeHeightStanding;
    private Double elbowHeightStanding;
    private Double thighClearance;
    private Double eyeHeight;
    private Double shoulderBreadth;
    private Double kneeHeight;

    @OneToOne()
    @JoinColumn(name = "person_id")
    private Person person;

    @Override
    public String toString() {
        return "PersonAnthropometrics{" +
                "id=" + id +
                ", height=" + height +
                ", sittingHeight=" + sittingHeight +
                ", shoulderHeight=" + shoulderHeight +
                ", lowerLegLength=" + lowerLegLength +
                ", hipBreadth=" + hipBreadth +
                ", elbowHeight=" + elbowHeight +
                ", eyeHeightStanding=" + eyeHeightStanding +
                ", elbowHeightStanding=" + elbowHeightStanding +
                ", thighClearance=" + thighClearance +
                ", eyeHeight=" + eyeHeight +
                ", shoulderBreadth=" + shoulderBreadth +
                ", kneeHeight=" + kneeHeight +
                '}';
    }
}
