package ru.hogwarts.hogwartsparallelstreamapi.api;

import ru.hogwarts.hogwartsparallelstreamapi.model.Faculty;

import java.util.Collection;
import java.util.List;

public interface IFaculty {
    Faculty createFaculty(Faculty faculty);

    Faculty editFaculty(Faculty faculty);

    Faculty findFaculty(Long id);

    List<Faculty> showAllFaculty();

    List<Faculty> findFacultyByName(String name);

    Collection<Faculty> findFacultyByNameIgnoreCase(String name);

    Collection<Faculty> findFacultyByColorIgnoreCase(String color);

    public void deleteFacultyById(long id);

    public void deleteAllFaculty(Faculty faculty);
}