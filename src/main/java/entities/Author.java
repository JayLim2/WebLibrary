package entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Author {
    private int id;
    private String name;
    private LocalDate birthDate;
    private LocalDate deathDate;
    private String description;
}
