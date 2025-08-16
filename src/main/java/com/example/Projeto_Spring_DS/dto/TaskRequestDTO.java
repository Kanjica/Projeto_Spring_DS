package com.example.Projeto_Spring_DS.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskRequestDTO {
    @NotBlank(message = "O título não pode estar em branco")
    private String title;

    @Size(max = 500, message = "A descrição não pode ter mais de 200 caracteres")
    private String description;

    @FutureOrPresent(message = "A data de vencimento não pode ser no passado")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dueDate;
}