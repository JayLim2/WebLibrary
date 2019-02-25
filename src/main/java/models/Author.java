package models;

import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Author {
    private int id;
    private String name;
    private LocalDate birthDate;
    private LocalDate deathDate;
    private String description;
}
