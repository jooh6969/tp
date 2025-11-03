package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DEADLINE;

import java.time.LocalDateTime;

import seedu.address.logic.commands.AddTaskCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.task.Task;

/**
 * Parses input arguments and creates a new AddTaskCommand object
 */
public class AddTaskCommandParser implements Parser<AddTaskCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AddTaskCommand
     * and returns an AddTaskCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddTaskCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_DEADLINE);

        // Extract title (everything before the first prefix)
        String title = argMultimap.getPreamble().trim();

        if (title.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddTaskCommand.MESSAGE_USAGE));
        }

        // Validate title
        if (!Task.isValidTitle(title)) {
            throw new ParseException(Task.MESSAGE_CONSTRAINTS);
        }

        // Parse deadline if present; detect auto-adjustments to inform user
        LocalDateTime deadline = null;
        String adjustmentNote = null;
        if (argMultimap.getValue(PREFIX_DEADLINE).isPresent()) {
            String deadlineString = argMultimap.getValue(PREFIX_DEADLINE).get().trim();
            if (!Task.isValidDeadline(deadlineString)) {
                throw new ParseException(Task.DEADLINE_CONSTRAINTS);
            }
            try {
                deadline = Task.parseDeadline(deadlineString);
                // Compare normalized input vs formatted parsed to detect auto-adjustment
                // If input has time, compare full; else compare date-only
                boolean hasTime = deadlineString.contains(" ");
                String formattedParsed = hasTime
                        ? deadline.format(java.time.format.DateTimeFormatter.ofPattern(Task.DATE_TIME_FORMAT))
                        : deadline.toLocalDate().format(java.time.format.DateTimeFormatter.ofPattern(Task.DATE_FORMAT));
                String normalizedInput = hasTime ? deadlineString : deadlineString; // already trimmed
                if (!formattedParsed.equals(normalizedInput)) {
                    adjustmentNote = String.format(
                            "Note: Invalid date '%s' adjusted to '%s'.",
                            deadlineString,
                            hasTime ? formattedParsed : deadline.toLocalDate()
                                    .format(java.time.format.DateTimeFormatter.ofPattern(Task.DATE_FORMAT))
                                    + " " + Task.DEFAULT_DEADLINE_TIME
                    );
                }
            } catch (IllegalArgumentException e) {
                throw new ParseException(Task.DEADLINE_CONSTRAINTS);
            }
        }

        Task task = new Task(title, deadline);
        return adjustmentNote == null ? new AddTaskCommand(task) : new AddTaskCommand(task, adjustmentNote);
    }
}
