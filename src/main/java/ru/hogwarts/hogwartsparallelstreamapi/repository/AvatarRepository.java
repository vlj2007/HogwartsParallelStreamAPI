package ru.hogwarts.hogwartsparallelstreamapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hogwarts.hogwartsparallelstreamapi.model.Avatar;

import java.util.Optional;

public interface AvatarRepository extends JpaRepository<Avatar, Long> {

    Optional<Avatar> findByStudentId(Long studentId);
}