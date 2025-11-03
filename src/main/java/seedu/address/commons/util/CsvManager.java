package seedu.address.commons.util;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
 * Handles import and export of Person data to and from CSV files.
 */
public class CsvManager {

    private static final String HEADER =
        "Name,Year,StudentNumber,Email,Phone,DietaryRequirements,Role,Tags";

    /**
     * Exports a list of persons to a CSV file.
     * If no path is provided, a new file is created automatically with a timestamped name.
     *
     * @param persons      the list of persons to export
     * @param optionalPath the optional file path (can be null or empty)
     * @return the Path of the exported CSV file
     * @throws IOException if writing fails
     */
    public static Path exportPersons(List<Person> persons, String optionalPath) throws IOException {
        Path filePath;

        // Determine file path
        if (optionalPath == null || optionalPath.trim().isEmpty()) {
            filePath = Paths.get("members.csv");
        } else {
            filePath = Paths.get(optionalPath);
        }

        // Create parent directory (if missing)
        if (filePath.getParent() != null) {
            Files.createDirectories(filePath.getParent());
        }

        // Create file if missing
        if (!Files.exists(filePath)) {
            Files.createFile(filePath);
        }

        // Write CSV data
        try (BufferedWriter writer = Files.newBufferedWriter(filePath)) {
            writer.write(HEADER);
            writer.write(System.lineSeparator());

            for (Person p : persons) {
                String tags = p.getTags().stream()
                    .map(tag -> tag.tagName) // remove [ ]
                    .collect(Collectors.joining(";"));

                writer.write(String.join(",",
                    csvEscape(p.getName()),
                    csvEscape(p.getYear()),
                    csvEscape(p.getStudentNumber()),
                    csvEscape(p.getEmail()),
                    csvEscape(p.getPhone()),
                    csvEscape(p.getDietaryRequirements()),
                    csvEscape(p.getRole()),
                    csvEscape(tags)
                ));
                writer.write(System.lineSeparator());
            }
        }

        System.out.println("Export successful: " + filePath);

        // Try to auto-open file for user
        try {
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(filePath.toFile());
            }
        } catch (IOException e) {
            System.out.println("Export complete, but unable to open file automatically.");
        }

        return filePath;
    }

    /**
     * Imports Person data from a CSV file.
     * Handles missing fields gracefully by filling them with default placeholders.
     *
     * @param optionalPath the file path string (can be null or empty)
     * @return the list of imported Person objects
     * @throws IOException if reading fails or file not found
     */
    public static ImportResult importPersons(String optionalPath) throws IOException {
        Path filePath;

        if (optionalPath == null || optionalPath.trim().isEmpty()) {
            filePath = Paths.get("members.csv");
        } else {
            filePath = Paths.get(optionalPath);
        }

        if (!Files.exists(filePath)) {
            throw new FileNotFoundException("CSV file not found: " + filePath);
        }

        List<Person> persons = new ArrayList<>();
        List<String> errors = new ArrayList<>();
        Set<String> seenStudentNumbers = new HashSet<>();
        Set<String> seenPhones = new HashSet<>();

        // Validation patterns (from User Guide)
        final String nameRegex = "^[A-Za-z ]+$";
        final String studentRegex = "^[A-Za-z]\\d{7}[A-Za-z]$";
        final String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        final String phoneRegex = "^\\d{8}$";
        final Set<String> validYears = Set.of("1", "2", "3", "4", "Year 1", "Year 2", "Year 3", "Year 4");

        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            String line = reader.readLine(); // skip header
            if (line == null) {
                throw new IOException("CSV file is empty.");
            }

            int lineNumber = 1;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] parts = line.split(",", -1);
                if (parts.length < 8) {
                    errors.add("Line " + lineNumber + ": Missing required columns.");
                    continue;
                }

                String nameStr = getOrDefault(parts, 0, "").trim();
                String yearStr = getOrDefault(parts, 1, "").trim();
                String studentNoStr = getOrDefault(parts, 2, "").trim();
                String emailStr = getOrDefault(parts, 3, "").trim();
                String phoneStr = getOrDefault(parts, 4, "").trim();
                String dietStr = getOrDefault(parts, 5, "").trim();
                String roleStr = getOrDefault(parts, 6, "").trim();
                String tagsStr = getOrDefault(parts, 7, "").trim();

                boolean valid = true;

                // ===== Field Validation =====
                if (nameStr.isEmpty() || !nameStr.matches(nameRegex)) {
                    errors.add("Line " + lineNumber + ": Invalid name (" + nameStr + ")");
                    valid = false;
                }

                if (yearStr.isEmpty() || !validYears.contains(yearStr)) {
                    errors.add("Line " + lineNumber + ": Invalid year (" + yearStr + ")");
                    valid = false;
                }

                if (!studentNoStr.matches(studentRegex)) {
                    errors.add("Line " + lineNumber + ": Invalid student number (" + studentNoStr + ")");
                    valid = false;
                } else if (seenStudentNumbers.contains(studentNoStr)) {
                    errors.add("Line " + lineNumber + ": Duplicate student number (" + studentNoStr + ")");
                    valid = false;
                }

                if (!emailStr.matches(emailRegex)) {
                    errors.add("Line " + lineNumber + ": Invalid email (" + emailStr + ")");
                    valid = false;
                }

                if (!phoneStr.matches(phoneRegex)) {
                    errors.add("Line " + lineNumber + ": Invalid phone number (" + phoneStr + ")");
                    valid = false;
                } else if (seenPhones.contains(phoneStr)) {
                    errors.add("Line " + lineNumber + ": Duplicate phone number (" + phoneStr + ")");
                    valid = false;
                }

                if (dietStr.isEmpty()) {
                    errors.add("Line " + lineNumber + ": Missing dietary requirements.");
                    valid = false;
                } else if (!dietStr.matches("^[A-Za-z ]+$")) {
                    errors.add("Line " + lineNumber + ": Invalid role (" + roleStr + ")");
                    valid = false;
                }

                if (roleStr.isEmpty()) {
                    errors.add("Line " + lineNumber + ": Missing role.");
                    valid = false;
                } else if (!roleStr.matches("^[A-Za-z ]+$")) {
                    errors.add("Line " + lineNumber + ": Invalid role (" + roleStr + ")");
                    valid = false;
                }

                if (!valid) {
                    continue; // skip this line entirely
                }

                // ===== Construct Valid Person =====
                try {
                    Person p = new Person(
                        new Name(nameStr),
                        new Year(yearStr),
                        new StudentNumber(studentNoStr),
                        new Email(emailStr),
                        new Phone(phoneStr),
                        new DietaryRequirements(dietStr),
                        new Role(roleStr),
                        parseTags(tagsStr)
                    );
                    persons.add(p);
                    seenStudentNumbers.add(studentNoStr);
                    seenPhones.add(phoneStr);
                } catch (Exception e) {
                    errors.add("Line " + lineNumber + ": Error creating person (" + e.getMessage() + ")");
                }
            }
        }

        // ===== Build summary string =====
        String errorSummary = "";
        if (!errors.isEmpty()) {
            errorSummary = "Skipped " + errors.size() + " invalid line(s):\n" + String.join("\n", errors);
            System.out.println(errorSummary);
        }

        System.out.println("Import finished: " + persons.size() + " valid entries loaded from " + filePath);

        return new ImportResult(persons, errorSummary);
    }



    // ===== Helper Methods =====

    private static String safe(Object obj) {
        return (obj == null) ? "" : obj.toString();
    }

    private static String getOrDefault(String[] arr, int index, String def) {
        if (index >= arr.length) {
            return def;
        }
        String value = arr[index].trim();
        return value.isEmpty() ? def : value;
    }

    private static Set<Tag> parseTags(String tagString) {
        if (tagString == null || tagString.isEmpty()) {
            return new HashSet<>();
        }
        return Arrays.stream(
                tagString.replace("[", "")
                    .replace("]", "")
                    .split("[;]"))
            .map(String::trim)
            .filter(s -> !s.isEmpty())
            .map(Tag::new)
            .collect(Collectors.toSet());
    }

    /**
     * Escapes commas and quotes in CSV values.
     */
    private static String csvEscape(Object obj) {
        if (obj == null) {
            return "";
        }
        String value = obj.toString();
        if (value.contains(",") || value.contains("\"")) {
            value = "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }

    /**
     * Merges trailing array elements after a certain index into one string (for flexible tag counts).
     */
    private static String[] mergeTrailing(String[] arr, int expectedLength) {
        if (arr.length <= expectedLength) {
            return arr;
        }
        String[] merged = new String[expectedLength];
        System.arraycopy(arr, 0, merged, 0, expectedLength - 1);
        // merge all remaining into last cell (tags)
        merged[expectedLength - 1] = String.join(",", Arrays.copyOfRange(arr, expectedLength - 1, arr.length));
        return merged;
    }

    /**
     * Represents the result of an import operation, containing the successfully
     * parsed persons and a summary of any errors encountered.
     */
    public static class ImportResult {
        public final List<Person> persons;
        public final String errorSummary;

        /**
         * Creates an ImportResult object with the provided list of persons and error summary.
         */
        public ImportResult(List<Person> persons, String errorSummary) {
            this.persons = persons;
            this.errorSummary = errorSummary;
        }
    }

}


