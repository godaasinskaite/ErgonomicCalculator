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
public class WorkspaceMetrics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double tableHeightSeated;
    private Double tableHeightStanding;
    private Double tableWidth;

    private Double displayHeightSeated;
    private Double displayHeightStanding;

    private Double chairSeatHeight;
    private Double chairSeatWidth;
    private Double armRestHeight;
    private Double chairBackSupportHeight;
    private Double chairBackSupportWidth;
    private Double chairHeadSupportHeight;

    private String imagePath;

    @OneToOne
    @JoinColumn(name = "person_id")
    private Person person;

    @Override
    public String toString() {
        return "WorkspaceMetrics{" +
                "id=" + id +
                ", tableHeightSeated=" + tableHeightSeated +
                ", tableHeightStanding=" + tableHeightStanding +
                ", tableWidth=" + tableWidth +
                ", displayHeightSeated=" + displayHeightSeated +
                ", displayHeightStanding=" + displayHeightStanding +
                ", chairSeatHeight=" + chairSeatHeight +
                ", chairSeatWidth=" + chairSeatWidth +
                ", armRestHeight=" + armRestHeight +
                ", chairBackSupportHeight=" + chairBackSupportHeight +
                ", chairBackSupportWidth=" + chairBackSupportWidth +
                ", chairHeadSupportHeight=" + chairHeadSupportHeight +
                ", imagePath='" + imagePath + '\'' +
                '}';
    }
}
