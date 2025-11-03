package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DEADLINE;

import java.util.Objects;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.task.Task;

/**
 * Adds a task to the address book.
 */
public class AddTaskCommand extends Command {

    public static final String COMMAND_WORD = "addtask";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a task to the address book. "
            + "Format: " + COMMAND_WORD + " TITLE "
            + "[" + PREFIX_DEADLINE + "DEADLINE]\n"
            + "Example: " + COMMAND_WORD + " Complete project report "
            + PREFIX_DEADLINE + "2024-12-31 23:59";

    public static final String MESSAGE_SUCCESS = "New task added: %1$s";
    public static final String MESSAGE_DUPLICATE_TASK = "This task already exists in the address book";

    private final Task toAdd;
    // Optional note to append to the success message (e.g., date auto-adjustment notice)
    private final String postMessageNote;

    /**
     * Creates an AddTaskCommand to add the specified {@code Task}
     */
    public AddTaskCommand(Task task) {
        requireNonNull(task);
        toAdd = task;
        this.postMessageNote = null;
    }

    /**
     * Creates an AddTaskCommand with an optional note to append to the success message.
     */
    public AddTaskCommand(Task task, String postMessageNote) {
        requireNonNull(task);
        toAdd = task;
        this.postMessageNote = (postMessageNote == null || postMessageNote.trim().isEmpty())
                ? null : postMessageNote;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (model.hasTask(toAdd)) {
            throw new CommandException(MESSAGE_DUPLICATE_TASK);
        }

        model.addTask(toAdd);
        String base = String.format(MESSAGE_SUCCESS, Messages.format(toAdd));
        String message = (postMessageNote == null) ? base : base + "\n\n" + postMessageNote;
        return new CommandResult(message);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddTaskCommand)) {
            return false;
        }

        AddTaskCommand otherAddTaskCommand = (AddTaskCommand) other;
        return toAdd.equals(otherAddTaskCommand.toAdd)
                && Objects.equals(postMessageNote, otherAddTaskCommand.postMessageNote);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("toAdd", toAdd)
                .toString();
    }
}
