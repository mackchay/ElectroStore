package ru.haskov.electronicshop.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BestEmployeeDto implements Serializable {
    private Long id;

    private String lastName;

    private String firstName;

    private String patronymic;

    private LocalDate birthDate;

    private String positionTypeName;

    private boolean gender;

    private Long soldItemsQuantity;

    private Long soldItemsSum;
}
