package uz.project.common.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.format.DateTimeFormatter;

@Getter
@Schema(
        name = "Type of search",
        description = """
                    IF PARAM IS NUMBER NEED TO CHOOSE ex: 1,1L,\n
                    IF PARAM IS STRING NEED TO CHOOSE ex:Islombek,\n
                    IF PARAM IS LOCALDATE NEED TO CHOOSE ex:2020.05.14,\n
                    IF PARAM IS LOCALDATE NEED TO CHOOSE ex:2020.05.14 15:15:15,\n
                    IF PARAM IS DATE OLD API NEED TO CHOOSE ex:2020.05.14 15:15:15,\n
                    IF PARAM IS JSON NEED TO CHOOSE ex:Islombek,\n
                    IF PARAM IS BOOL NEED TO CHOOSE ex:true, false
                """
)
public enum TypeSearch {
    NUMBER,
    STRING,
    BOOL,
    LOCALDATE,
    LOCALDATETIME,
    DATE,
    JSON;

    public boolean isJson() {
        return this.equals(JSON);
    }

    public static final String FORMAT_DATE = "yyyy.MM.dd";
    public static final DateTimeFormatter FORMAT_LOCALDATE = DateTimeFormatter.ofPattern("yyyy.MM.dd");
    public static final DateTimeFormatter FORMAT_LOCALDATETIME = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss");
}
