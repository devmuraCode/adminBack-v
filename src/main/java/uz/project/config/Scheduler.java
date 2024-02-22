package uz.project.config;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import uz.project.common.constant.FileType;
import uz.project.entity.file.File;
import uz.project.entity.file.FileRepository;

import java.util.List;

@Component
public class Scheduler {

    private final FileRepository fileRepository;

    public Scheduler(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    @Scheduled(cron = "0 52 3 * * ?")
    public void deleteDraftFiles() {
        List<File> files = fileRepository.findAllByType(FileType.DRAFT);
        for (File file : files) {
            fileRepository.delete(file);
        }
    }
}
