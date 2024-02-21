package ru.hogwarts.hogwartsparallelstreamapi.service;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.hogwartsparallelstreamapi.api.IAvatar;
import ru.hogwarts.hogwartsparallelstreamapi.model.Avatar;
import ru.hogwarts.hogwartsparallelstreamapi.model.Student;
import ru.hogwarts.hogwartsparallelstreamapi.repository.AvatarRepository;
import ru.hogwarts.hogwartsparallelstreamapi.repository.StudentRepository;

import javax.imageio.ImageIO;

import javax.imageio.ImageIO;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Service
@Transactional
public class AvatarService implements IAvatar {

    Logger logger = LoggerFactory.getLogger(AvatarService.class);
    private final AvatarRepository avatarRepository;
    private final StudentRepository studentRepository;

    @Value("${path.to.avatars.folder}")
    private String avatarsDir;

    public AvatarService(AvatarRepository avatarRepository, StudentRepository studentRepository) {
        this.avatarRepository = avatarRepository;
        this.studentRepository = studentRepository;
    }

    public void uploadAvatar(Long studentId, MultipartFile avatarFile) throws IOException {
        logger.info("Was invoked method for upload Avatar");
        Student student = studentRepository.getById(studentId);

        Path filePath = Path.of(avatarsDir, student + "." + getExtensions(avatarFile.getOriginalFilename()));
        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);
        try (
                InputStream is = avatarFile.getInputStream();
                OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
                BufferedInputStream bis = new BufferedInputStream(is, 1024);
                BufferedOutputStream bos = new BufferedOutputStream(os, 1024);
        ) {
            bis.transferTo(bos);
        }
        Avatar avatar = findAvatar(studentId);

        if (avatar == null) {
            avatar = new Avatar();
        }
        avatar.setStudent(student);
        avatar.setFilePath(filePath.toString());
        avatar.setFileSize(avatarFile.getSize());
        avatar.setMediaType(avatarFile.getContentType());
        avatar.setData(generateDataForDB(filePath));
        avatarRepository.save(avatar);
    }

    private String getExtensions(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    public Avatar findAvatar(Long studentId) {
        logger.info("Was invoked method for find Avatar");
        return avatarRepository.findByStudentId(studentId).orElse(new Avatar());
    }

    public void delete(Long id) {
        logger.info("Was invoked method for delete Avatar");
        avatarRepository.deleteById(id);
    }

    private byte[] generateDataForDB(Path filePath) throws IOException {
        logger.info("Was invoked method generate Data For DB Avatar");
        try (
                InputStream is = Files.newInputStream(filePath);
                BufferedInputStream bis = new BufferedInputStream(is, 1024);
                ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            BufferedImage image = ImageIO.read(bis);

            int height = image.getHeight() / (image.getWidth() / 100);
            BufferedImage preview = new BufferedImage(100, height, image.getType());
            Graphics2D graphics2D = preview.createGraphics();
            graphics2D.drawImage(image, 0, 0, 100, height, null);
            graphics2D.dispose();

            ImageIO.write(preview, getExtensions(filePath.getFileName().toString()), baos);
            return baos.toByteArray();

        }

    }

    public List<Avatar> getAll(Integer number, Integer size) {
        logger.info("Was invoked method for get all Avatar");
        PageRequest pageRequest = PageRequest.of(number - 1, size);
        return avatarRepository.findAll(pageRequest).getContent();
    }






    public int summa() {
        long start = System.currentTimeMillis();
        logger.info("Was invoked method for summa");
        int sum = Stream.iterate(1, a -> a + 1).limit(1_000_000)
                .reduce(0, (a, b) -> a + b);
        logger.info("time=" + (System.currentTimeMillis() - start));
        return sum;
    }


    public int summaParallel() {
        long start = System.currentTimeMillis();
        logger.info("Was invoked method for summaParallel");
        int sum1 = IntStream.iterate(1, a -> a + 1)
                .limit(1_000_000)
                .parallel()
                .sum();
        logger.info("time=" + (System.currentTimeMillis() - start));
        return sum1;
    }









}