package ru.hogwarts.hogwartsparallelstreamapi.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.hogwartsparallelstreamapi.model.Avatar;
import ru.hogwarts.hogwartsparallelstreamapi.service.AvatarService;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpStatus;
import jakarta.servlet.http.HttpServletResponse;

import java.io.OutputStream;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@RestController
@RequestMapping("/avatars")
public class AvatarController {
    private final AvatarService avatarService;

    public AvatarController(AvatarService avatarService) {
        this.avatarService = avatarService;
    }

    @PostMapping(value = "/{Id}/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadAvatar(@PathVariable Long Id, @RequestParam MultipartFile avatar) throws IOException {
        if (avatar.getSize() >= 1024 * 300) {
            return ResponseEntity.badRequest().body("File is too big");
        }
        avatarService.uploadAvatar(Id, avatar);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping({"{id}"})
    public ResponseEntity<Void> deleteAvatar(@PathVariable Long id) {
        avatarService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/{id}/avatar/preview")
    public ResponseEntity<?> downloadAvatarPreview(@PathVariable Long id) {
        Avatar avatar = avatarService.findAvatar(id);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(avatar.getMediaType()));
        headers.setContentLength(avatar.getData().length);
        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(avatar.getData());
    }

    @GetMapping(value = "/{id}/avatar")
    public void downloadAvatar(@PathVariable Long id, HttpServletResponse response) throws IOException {
        Avatar avatar = avatarService.findAvatar(id);

        Path path = Path.of(avatar.getFilePath());

        try (InputStream is = Files.newInputStream(path);
             OutputStream os = response.getOutputStream()) {
            response.setStatus(200);
            response.setContentType(avatar.getMediaType());
            response.setContentLength((int) avatar.getFileSize());
            is.transferTo(os);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<Avatar>> getAllAvatars(@RequestParam("page") Integer pageNumber,
                                                      @RequestParam("size") Integer pageSize){
        return ResponseEntity.ok(avatarService.getAll(pageNumber,pageSize));
    }

    @GetMapping("/summa")
    public ResponseEntity<Integer> getTime(){
        return ResponseEntity.ok(avatarService.summa());
    }

    @GetMapping("/summaParallel")
    public ResponseEntity<Integer> getTimeParallel(){
        return ResponseEntity.ok(avatarService.summaParallel());
    }

}