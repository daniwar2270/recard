package bg.codexio.recard.filesmanagement.service;

import bg.codexio.recard.filesmanagement.model.payload.response.ImageUploadResponse;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    ImageUploadResponse upload(MultipartFile file, String userId);
}