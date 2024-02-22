package uz.project.common;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Slf4j
@UtilityClass
public class DateUtil {
    public static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    public static DateTimeFormatter idGovUzDateFormatBirthDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static DateTimeFormatter idGovUzDateFormatNew = DateTimeFormatter.ofPattern("yyyy-dd-MM");
    public static DateTimeFormatter dbDateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static DateTimeFormatter uzbekistanDateFormat = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    public static DateTimeFormatter uzbekistanDateAndTimeFormat = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

    public static LocalDateTime toLocalDateTime(String dateAsString, DateTimeFormatter dateFormat) {
        try {
            return LocalDateTime.parse(dateAsString, dateFormat);
        } catch (Exception ignored) {
            log.info("Could not parse {}", dateAsString);
            return null;
        }
    }

    public static LocalDate toLocalDate(String dateAsString, DateTimeFormatter dateFormat) {
        try {
            return LocalDate.parse(dateAsString, dateFormat);
        } catch (Exception ignored) {
            log.info("Could not parse {}", dateAsString);
            return null;
        }
    }

    public static Date toDate(String format, String val) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        try {
            return formatter.parse(val);
        } catch (Exception exception) {
            log.info("Could not parse {}", val);
            return null;
        }
    }

    public static String convertDate(LocalDateTime date, DateTimeFormatter dateFormat) {
        try {
            return date.format(dateFormat);
        } catch (Exception e) {
            log.warn(e.getMessage());
            return null;
        }
    }
}
