package models;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
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
