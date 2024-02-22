package uz.project.common.constant;

import lombok.Getter;

@Getter
public enum FileType {
    DRAFT("Qoralama file"),
    ACTIVE("Faol file");

    private final String title;

    FileType(String title) {
        this.title = title;
    }
}
