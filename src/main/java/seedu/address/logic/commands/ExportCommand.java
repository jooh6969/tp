package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import seedu.address.commons.util.CsvManager;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Exports all members in the address book to a CSV file.
 */
public class ExportCommand extends Command {

    public static final String COMMAND_WORD = "export";

    public static final String MESSAGE_USAGE = COMMAND_WORD
        + ": Exports all members to a CSV file.\n"
        + "Format: " + COMMAND_WORD + " [/to FILEPATH]\n"
        + "Example: " + COMMAND_WORD + " /to members.csv";

    public static final String MESSAGE_SUCCESS = "Export successful: %1$s";
    public static final String MESSAGE_FAILURE = "Failed to export members: %1$s";
    public static final String MESSAGE_INVALID_FILETYPE =
        "Invalid file format. Only .csv files are supported. Example: export /to members.csv";

    private final String filePath;

    /**
     * Creates an ExportCommand with an optional file path.
     */
    public ExportCommand(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        // ===== File path validation =====
        if (filePath != null && !filePath.trim().isEmpty()) {
            String lowerPath = filePath.toLowerCase();
            if (!lowerPath.endsWith(".csv")) {
                throw new CommandException(MESSAGE_INVALID_FILETYPE);
            }
        }

        List<Person> persons = model.getFilteredPersonList();

        try {
            Path exportedFile = CsvManager.exportPersons(persons, filePath);
            return new CommandResult(String.format(MESSAGE_SUCCESS, exportedFile));
        } catch (IOException e) {
            throw new CommandException(String.format(MESSAGE_FAILURE, e.getMessage()));
        }
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof ExportCommand)) {
            return false;
        }

        ExportCommand otherExportCommand = (ExportCommand) other;
        return (filePath == null && otherExportCommand.filePath == null)
            || (filePath != null && filePath.equals(otherExportCommand.filePath));
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
            .add("filePath", filePath)
            .toString();
    }
}


