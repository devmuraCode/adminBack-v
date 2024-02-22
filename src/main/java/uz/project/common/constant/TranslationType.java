package uz.project.common.constant;

import lombok.Getter;

@Getter
public enum TranslationType {
    ADMIN_CABINET("Admin cabinet"),
    CLIENT_CABINET("Client cabinet");

    private final String title;

    TranslationType(String title) {
        this.title = title;
    }
}
