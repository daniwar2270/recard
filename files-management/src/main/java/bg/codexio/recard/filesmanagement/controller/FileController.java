package bg.codexio.recard.filesmanagement.controller;

import bg.codexio.recard.filesmanagement.constant.HeaderConstants;
import bg.codexio.recard.filesmanagement.model.payload.response.ImageUploadResponse;
import bg.codexio.recard.filesmanagement.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/profiles")
public class FileController {

    private final FileService fileService;

    @ResponseStatus(HttpStatus.ACCEPTED)
    @PostMapping("/avatars")
    public ImageUploadResponse uploadFile(MultipartFile file, @RequestHeader(HeaderConstants.USER_ID) String userId) {
        return this.fileService.upload(file, userId);
    }
}