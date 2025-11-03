package seedu.address.model.task;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;

import seedu.address.commons.util.ToStringBuilder;

/**
 * Represents a Task in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Task {

    public static final String MESSAGE_CONSTRAINTS =
            "Task title should not be empty and should not exceed 100 characters";
    public static final String DEADLINE_CONSTRAINTS =
            "Deadline should be in YYYY-MM-DD HH:mm format or YYYY-MM-DD format";
    public static final String VALIDATION_REGEX = "^.{1,100}$";
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm";
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String DEFAULT_DEADLINE_TIME = "23:59";
    public static final int MAX_TITLE_LENGTH = 100;

    private final String title;
    private final LocalDateTime deadline;
    private final boolean isDone;

    /**
     * Constructs a new Task with the specified title and deadline.
     * The task is created with an initial completion status of not done.
     *
     * @param title The title of the task (must not be null, empty, or exceed 100 characters)
     * @param deadline The deadline for the task (can be null for tasks without deadlines)
     * @throws IllegalArgumentException if the title is invalid
     */
    public Task(String title, LocalDateTime deadline) {
        requireAllNonNull(title);
        if (!isValidTitle(title)) {
            throw new IllegalArgumentException(MESSAGE_CONSTRAINTS);
        }
        this.title = title;
        this.deadline = deadline;
        this.isDone = false;
    }

    /**
     * Constructs a Task with all fields specified.
     * This constructor is primarily used for loading tasks from storage or creating task copies.
     *
     * @param title The title of the task (must not be null, empty, or exceed 100 characters)
     * @param deadline The deadline for the task (can be null for tasks without deadlines)
     * @param isDone The completion status of the task
     * @throws IllegalArgumentException if the title is invalid
     */
    public Task(String title, LocalDateTime deadline, boolean isDone) {
        requireAllNonNull(title);
        if (!isValidTitle(title)) {
            throw new IllegalArgumentException(MESSAGE_CONSTRAINTS);
        }
        this.title = title;
        this.deadline = deadline;
        this.isDone = isDone;
    }

    public String getTitle() {
        return title;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public boolean isDone() {
        return isDone;
    }

    /**
     * Returns a new Task with the completion status toggled.
     * If the task is currently done, returns a copy marked as not done, and vice versa.
     *
     * @return A new Task object with the opposite completion status
     */
    public Task toggleDone() {
        return new Task(this.title, this.deadline, !this.isDone);
    }

    /**
     * Returns true if both tasks have the same identity for duplicate detection.
     * Two tasks are considered the same task if they have the same title AND the same deadline.
     * This allows multiple tasks to share the same title as long as their deadlines differ.
     *
     * @param otherTask The other task to compare with
     * @return true if both tasks have the same title and deadline, false otherwise
     */
    public boolean isSameTask(Task otherTask) {
        if (otherTask == this) {
            return true;
        }

        return otherTask != null
                && otherTask.getTitle().equals(getTitle())
                && Objects.equals(otherTask.getDeadline(), getDeadline());
    }

    /**
     * Returns true if a given string is a valid task title.
     * A valid title must not be null, not empty when trimmed, and not exceed 100 characters.
     *
     * @param test The title string to validate
     * @return true if the title is valid, false otherwise
     */
    public static boolean isValidTitle(String test) {
        return test != null && !test.trim().isEmpty() && test.matches(VALIDATION_REGEX);
    }

    /**
     * Returns true if a given string is a valid deadline format.
     * Accepts formats: "YYYY-MM-DD HH:mm" or "YYYY-MM-DD"
     *
     * @param test The deadline string to validate
     * @return true if the deadline is valid or empty (optional), false otherwise
     */
    public static boolean isValidDeadline(String test) {
        if (test == null || test.trim().isEmpty()) {
            return true; // deadline is optional
        }

        try {
            parseDeadline(test);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Parses a deadline string into LocalDateTime.
     * Accepts two formats:
     * 1. "YYYY-MM-DD HH:mm" - Full date and time
     * 2. "YYYY-MM-DD" - Date only (time defaults to 23:59)
     *
     * @param deadlineString The deadline string to parse
     * @return LocalDateTime object, or null if the input is empty
     * @throws IllegalArgumentException if the deadline format is invalid
     */
    public static LocalDateTime parseDeadline(String deadlineString) {
        if (deadlineString == null || deadlineString.trim().isEmpty()) {
            return null;
        }

        String trimmed = deadlineString.trim();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);

        try {
            return LocalDateTime.parse(trimmed, dateTimeFormatter);
        } catch (DateTimeParseException e) {
            try {
                return LocalDateTime.parse(trimmed + " 23:59", dateTimeFormatter);
            } catch (DateTimeParseException e2) {
                throw new IllegalArgumentException(String.format(
                    "Invalid deadline format: '%s'. Expected format: %s or %s",
                    trimmed, DATE_TIME_FORMAT, DATE_FORMAT));
            }
        }
    }

    /**
     * Returns true if both tasks have the same identity and data fields.
     * This defines a stronger notion of equality between two tasks.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Task)) {
            return false;
        }

        Task otherTask = (Task) other;
        return title.equals(otherTask.title)
                && Objects.equals(deadline, otherTask.deadline)
                && isDone == otherTask.isDone;
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(title, deadline, isDone);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("title", title)
                .add("deadline", deadline)
                .add("isDone", isDone)
                .toString();
    }
}
