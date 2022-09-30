package gachon.BLoom.util;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Getter
@Component
public class FileUtil {

    private final String absolutePath;
    private final String memberFolderPath;

    public FileUtil() {
        this.absolutePath = new File("").getAbsolutePath() + "/";
        this.memberFolderPath = absolutePath + "src/main/resources/static/images/member/";
    }

    // uuid 추가한 이미지 이름 반환
    private String getUniqueImageName(MultipartFile file) {
        return UUID.randomUUID() + "_" + file.getOriginalFilename();
    }

    /*
    향후 중복 리팩토링
     */

    // 유저 이미지를 저장할 경로 반환
    private Path getUserImagePath(String imageName) {
        return Paths.get(memberFolderPath + imageName);
    }


    public String saveMemberImage(MultipartFile file) {
        String uniqueImageName;

        if(file.isEmpty()) {
            uniqueImageName = "Default.png";
        } else {
            uniqueImageName = getUniqueImageName(file);

            Path filePath = getUserImagePath(uniqueImageName);

            try {
                file.transferTo(filePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return uniqueImageName;
    }

    public void deleteUserImage(String imageName) {
        File file = new File(memberFolderPath + imageName);
        if (file.exists()) {
            file.delete();
        }
    }
}
