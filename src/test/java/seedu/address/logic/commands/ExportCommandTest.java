package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;

/**
 * Tests for ExportCommand.
 */
public class ExportCommandTest {

    private final Model model = new ModelManager(); // uses in-memory address book

    @Test
    public void execute_exportCreatesFile_success() throws Exception {
        ExportCommand command = new ExportCommand("test_export.csv");
        command.execute(model);

        Path exportedPath = Path.of("test_export.csv");
        assertTrue(Files.exists(exportedPath));

        // cleanup
        Files.deleteIfExists(exportedPath);
    }
}
