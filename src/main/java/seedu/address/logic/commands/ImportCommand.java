package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.io.IOException;

import seedu.address.commons.util.CsvManager;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Imports member details from a CSV file into the address book.
 */
public class ImportCommand extends Command {

    public static final String COMMAND_WORD = "import";

    public static final String MESSAGE_USAGE = COMMAND_WORD
        + ": Imports members from a CSV file.\n"
        + "Format: " + COMMAND_WORD + " [/from FILEPATH]\n"
        + "Example: " + COMMAND_WORD + " /from members.csv";

    public static final String MESSAGE_SUCCESS = "Import complete: %1$d member(s) added.";
    public static final String MESSAGE_FAILURE = "Failed to import members: %1$s";
    public static final String MESSAGE_INVALID_FILETYPE =
        "Invalid file format. Only .csv files are supported. Example: import /from members.csv";

    private final String filePath;

    /**
     * Creates an ImportCommand with an optional file path.
     */
    public ImportCommand(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        // ===== File type validation =====
        if (filePath != null && !filePath.trim().isEmpty()) {
            String lowerPath = filePath.toLowerCase();
            if (!lowerPath.endsWith(".csv")) {
                throw new CommandException(MESSAGE_INVALID_FILETYPE);
            }
        }

        try {
            CsvManager.ImportResult result = CsvManager.importPersons(filePath);

            int addedCount = 0;
            int duplicateCount = 0;

            for (Person person : result.persons) {
                if (model.hasPerson(person)) {
                    duplicateCount++;
                } else {
                    model.addPerson(person);
                    addedCount++;
                }
            }

            StringBuilder message = new StringBuilder();
            message.append(String.format(MESSAGE_SUCCESS, addedCount));

            if (duplicateCount > 0) {
                message.append(String.format(
                    "\n\nNote: %d existing member(s) were skipped as duplicates.",
                    duplicateCount));
            }

            // keep your existing errorSummary logic untouched
            if (!result.errorSummary.isEmpty()) {
                message.append("\n\nSome entries were skipped due to invalid data.\n");
                message.append(result.errorSummary);
            }

            return new CommandResult(message.toString());

        } catch (IOException e) {
            throw new CommandException(String.format(MESSAGE_FAILURE, e.getMessage()));
        }
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof ImportCommand)) {
            return false;
        }

        ImportCommand otherImportCommand = (ImportCommand) other;
        return (filePath == null && otherImportCommand.filePath == null)
            || (filePath != null && filePath.equals(otherImportCommand.filePath));
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
            .add("filePath", filePath)
            .toString();
    }
}




