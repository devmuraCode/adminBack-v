package uz.project.common.constant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Name {
    private String uz;
    private String oz;
    private String ru;

    public String getByLang(String lang) {
        return switch (lang) {
            case "oz" -> oz;
            case "ru" -> ru;
            default -> uz;
        };
    }
}
