package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.util.Base64;
import java.util.Scanner;

public class ImageHashUtil {
    private static final Base64.Encoder encoder;
    private static final Base64.Decoder decoder;

    private static String DEFAULT_AUTHOR = "";
    private static String DEFAULT_BOOK = "";

    static {
        encoder = Base64.getEncoder();
        decoder = Base64.getDecoder();

        File file = new File("default.txt");
        try (Scanner scanner = new Scanner(new FileReader(file))) {
            DEFAULT_AUTHOR = scanner.nextLine();
            DEFAULT_BOOK = scanner.nextLine();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getDefaultAuthor() {
        return DEFAULT_AUTHOR;
    }

    public static String getDefaultBook() {
        return DEFAULT_BOOK;
    }

    public static String encode(String imagePath) {
        File image = new File(imagePath);
        try (FileInputStream in = new FileInputStream(image)) {
            byte[] imageData = new byte[(int) image.length()];
            if (in.read(imageData) > 0) {
                return encoder.encodeToString(imageData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void decode(String hash) {
        //coming soon
    }
}
