package models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Book {
    private int id;
    private String title;
    private int createdYear;
    private int publishedYear;
    private String description;
    private String imageHash;
    private Author author;
    private Publisher publisher;
}
