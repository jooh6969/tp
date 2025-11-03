package seedu.address.commons.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.model.person.DietaryRequirements;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Role;
import seedu.address.model.person.StudentNumber;
import seedu.address.model.person.Year;
import seedu.address.model.tag.Tag;

/**
 * Unit tests for CsvManager.
 */
public class CsvManagerTest {

    private static final Path TEST_EXPORT_PATH = Path.of("test_members.csv");
    private static final Path TEST_IMPORT_PATH = Path.of("test_import.csv");

    private Person samplePerson;

    @BeforeEach
    public void setUp() {
        Set<Tag> tags = new HashSet<>();
        tags.add(new Tag("logistics"));
        samplePerson = new Person(
            new Name("John Doe"),
            new Year("1"),
            new StudentNumber("A1234567X"),
            new Email("john@example.com"),
            new Phone("98765432"),
            new DietaryRequirements("None"),
            new Role("Member"),
            tags);
    }

    @AfterEach
    public void tearDown() throws IOException {
        Files.deleteIfExists(TEST_EXPORT_PATH);
        Files.deleteIfExists(TEST_IMPORT_PATH);
        // Also clean up default members_import.csv if created
        Files.deleteIfExists(Path.of("members_import.csv"));
    }

    @Test
    public void exportPersons_validList_createsCsvFile() throws Exception {
        CsvManager.exportPersons(List.of(samplePerson), TEST_EXPORT_PATH.toString());
        assertTrue(Files.exists(TEST_EXPORT_PATH));

        List<String> lines = Files.readAllLines(TEST_EXPORT_PATH);
        assertTrue(lines.get(0).contains("Name,Year,StudentNumber")); // header
        assertTrue(lines.get(1).contains("John Doe"));
    }

    @Test
    public void exportPersons_nullPath_createsTimestampedFile() throws Exception {
        Path exportedFile = CsvManager.exportPersons(List.of(samplePerson), null);
        assertTrue(Files.exists(exportedFile));
        assertTrue(exportedFile.getFileName().toString().startsWith("members"));
        Files.deleteIfExists(exportedFile);
    }

    @Test
    public void importPersons_validCsv_returnsListOfPersons() throws Exception {
        try (BufferedWriter writer = Files.newBufferedWriter(TEST_IMPORT_PATH)) {
            writer.write("Name,Year,StudentNumber,Email,Phone,DietaryRequirements,Role,Tags\n");
            writer.write("John Doe,1,A1234567X,john@example.com,98765432,None,Member,logistics\n");
        }

        CsvManager.ImportResult result = CsvManager.importPersons(TEST_IMPORT_PATH.toString());
        List<Person> persons = result.persons;

        assertEquals(1, persons.size());
        assertEquals("John Doe", persons.get(0).getName().toString());
        assertEquals("A1234567X", persons.get(0).getStudentNumber().toString());
        assertTrue(result.errorSummary.isEmpty()); // optional sanity check
    }


    @Test
    public void importPersons_noFileSpecifiedButDefaultExists_readsDefaultFile() throws Exception {
        // Write a valid default CSV file (the importer looks for "members.csv")
        Path defaultFile = Path.of("members.csv");
        try (BufferedWriter writer = Files.newBufferedWriter(defaultFile)) {
            writer.write("Name,Year,StudentNumber,Email,Phone,DietaryRequirements,Role,Tags\n");
            writer.write("Jane Tan,2,A7654321Z,jane@example.com,91234567,None,President,leadership\n");
        }

        // Run the import with null path (should pick up "members.csv")
        CsvManager.ImportResult result = CsvManager.importPersons(null);
        List<Person> persons = result.persons;

        // Verify the result
        assertEquals(1, persons.size());
        assertEquals("Jane Tan", persons.get(0).getName().toString());
        assertTrue(result.errorSummary.isEmpty()); // sanity check

        // Clean up
        Files.deleteIfExists(defaultFile);
    }



    @Test
    public void importPersons_missingFile_throwsIoException() {
        assertThrows(IOException.class, () -> CsvManager.importPersons("nonexistent.csv"));
    }

    @Test
    public void importPersons_noFileSpecifiedAndDefaultMissing_throwsIoException() throws IOException {
        Path defaultFile = Path.of("members.csv");

        // Ensure the default file does NOT exist
        Files.deleteIfExists(defaultFile);

        // Expect an IOException because the default file is missing
        assertThrows(IOException.class, () -> CsvManager.importPersons(null));
    }


    @Test
    public void importPersons_emptyCsv_returnsEmptyList() throws Exception {
        try (BufferedWriter writer = Files.newBufferedWriter(TEST_IMPORT_PATH)) {
            writer.write("Name,Year,StudentNumber,Email,Phone,DietaryRequirements,Role,Tags\n");
        }

        CsvManager.ImportResult result = CsvManager.importPersons(TEST_IMPORT_PATH.toString());
        List<Person> persons = result.persons;

        assertTrue(persons.isEmpty());
        // Optionally check no errors
        assertTrue(result.errorSummary.isEmpty());
    }

}
