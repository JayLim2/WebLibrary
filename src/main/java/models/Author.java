package models;

import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
    private String imageHash;
    private List<Book> books;

    {
        books = new ArrayList<>();
    }
}
