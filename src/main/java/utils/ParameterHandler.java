package utils;

import models.Genre;
import org.apache.commons.fileupload.FileItem;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ParameterHandler {
    private static final String ENCODING = "UTF-8";

    public static int tryParseInteger(String string) {
        int value = -1;
        try {
            value = Integer.parseInt(string);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    public static LocalDate tryParseDate(DateTimeFormatter formatter, String dateString) {
        LocalDate date = null;
        try {
            date = LocalDate.parse(dateString, formatter);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    public static Map<String, String> getAuthorParams(List<FileItem> formItems) {
        Map<String, String> fields = null;
        try {
            if (formItems != null) {
                fields = new HashMap<>();
                for (FileItem formItem : formItems) {
                    if (!formItem.isFormField() && formItem.getFieldName().equals("poster")) {
                        byte[] fileBytes = formItem.get();
                        fields.put("imageHash", ImageHashUtil.encodeFromBytes(fileBytes));
                    } else {
                        fields.put(formItem.getFieldName(), formItem.getString(ENCODING));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fields;
    }

    public static Map<String, String> getBookParams(List<FileItem> formItems, List<Genre> genres) {
        Map<String, String> fields = null;
        try {
            if (formItems != null) {
                fields = new HashMap<>();
                for (FileItem formItem : formItems) {
                    if (!formItem.isFormField() && formItem.getFieldName().equals("poster")) {
                        byte[] fileBytes = formItem.get();
                        fields.put("imageHash", ImageHashUtil.encodeFromBytes(fileBytes));
                    } else {
                        String fieldName = formItem.getFieldName();
                        if (Objects.equals(fieldName, "genresList")) {
                            int id = tryParseInteger(formItem.getString(ENCODING));
                            Genre genre = DAOInstances.getGenreDAO().getById(id);
                            if (genre != null) genres.add(genre);
                        } else {
                            fields.put(fieldName, formItem.getString(ENCODING));
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fields;
    }
}
