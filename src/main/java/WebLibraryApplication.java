import dao.AuthorDAO;
import models.Author;
import services.AuthorsService;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

public class WebLibraryApplication {
    public static void main(String[] args) {
        //initialize();

        AuthorsService authorsService = new AuthorsService();

        List<Author> authors = authorsService.getByBirthDateBetween(
                LocalDate.of(1921, 1, 1),
                LocalDate.of(1943, 1, 1)
        );
        authors = authorsService.getByDescription("som");
        authors.forEach(System.out::println);
    }

    public static void initialize() {
        AuthorDAO authorDAO = new AuthorDAO();

        int authorsCount = 7;
        for (int i = 1; i <= authorsCount; i++) {
            Author author = new Author();
            author.setName("Author " + i);
            LocalDate date = randomDate();
            author.setBirthDate(date);
            author.setDeathDate(randomOffsetDate(date));
            author.setDescription("some text about author " + i);
            authorDAO.save(author);
        }
    }

    private static final Random random = new Random();

    private static LocalDate randomDate() {
        return LocalDate.of(
                randomYear(1900, 2000),
                random.nextInt(12) + 1,
                random.nextInt(28) + 1
        );
    }

    private static LocalDate randomOffsetDate(LocalDate date) {
        int minYears = 25;
        int maxYears = 100;

        return date
                .plusYears(random.nextInt(maxYears - minYears) + minYears + 1)
                .plusMonths(random.nextInt(12) + 1)
                .plusDays(random.nextInt(28) + 1);
    }

    private static int randomYear(int left, int right) {
        return random.nextInt(right - left) + left + 1;
    }
}
