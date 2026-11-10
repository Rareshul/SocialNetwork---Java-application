package Repository;

import Domain.Entity;
import Exceptions.RepositoryException;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

abstract class AbstractFileRepo<ID, E extends Entity<ID>> extends InMemoryRepo<ID, E> {
    private final String filePath;
    private final Validation.Validator<E> validator;

    public AbstractFileRepo(String filePath, Validation.Validator<E> val) {
        this.filePath = filePath;
        validator = val;
    }

    @Override
    public void add(E a)
    {
        if (a == null)
            throw new IllegalArgumentException();
        super.add(a);
        saveToFile();
    }
    @Override
    public void remove(E a)
    {
        if (a == null)
            throw new IllegalArgumentException();
        super.remove(a);
        saveToFile();
    }

    /**
     * Writes all the entities to the file, one per line.
     *
     * @throws RepositoryException if the file cannot be written
     */
    public void saveToFile()
    {
        List<String> lines = stream()
                .map(this::createEntityToString)
                .collect(Collectors.toList());
        try {
            Files.write(Path.of(filePath), lines);
        } catch (IOException e) {
            throw new RepositoryException("Datele nu sunt in regula");
        }
    }

    /**
     * Reads all the entities from the file. Blank lines are skipped.
     *
     * @throws RepositoryException if the file is missing or cannot be read,
     *                             or if a line is corrupt (the message contains the line number)
     */
    public void loadFromFile()
    {
        List<String> lines = readLines();
        IntStream.range(0, lines.size())
                .filter(i -> !lines.get(i).isBlank())
                .mapToObj(i -> parseLine(i + 1, lines.get(i)))
                .collect(Collectors.toList())
                .forEach(super::add);
    }

    /**
     * @return all the lines of the file
     * @throws RepositoryException if the file cannot be read
     */
    private List<String> readLines()
    {
        try (Stream<String> lines = Files.lines(Path.of(filePath))) {
            return lines.collect(Collectors.toList());
        } catch (IOException | UncheckedIOException e) {
            throw new RepositoryException("Datele nu sunt in regula");
        }
    }

    /**
     * Converts one line to an entity and validates it.
     *
     * @param lineNumber the number of the line in the file (starting from 1), used in the error message
     * @param line       the text of the line
     * @return the validated entity
     * @throws RepositoryException if the line is corrupt or the entity is not valid
     */
    private E parseLine(int lineNumber, String line)
    {
        try {
            E entity = extractEntity(line);
            validator.validate(entity);
            return entity;
        } catch (RuntimeException e) {
            throw new RepositoryException("Linia " + lineNumber + " din fisierul " + filePath
                    + " este corupta: " + e.getMessage());
        }
    }

    public abstract E extractEntity(String line);
    public abstract String createEntityToString(E entity);

}
