package seedu.address.model.person;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.tag.Tag;

/**
 * Represents a Person in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Person {

    // Identity fields
    private final Name name;
    private final Year year;
    private final StudentNumber studentNumber;
    private final Email email;
    private final Phone phone;

    // Data fields
    private final DietaryRequirements dietaryRequirements;
    private final Role role;
    private final Set<Tag> tags = new HashSet<>();

    /**
     * Every field must be present and not null.
     */
    public Person(Name name, Year year, StudentNumber studentNumber, Email email, Phone phone,
                  DietaryRequirements dietaryRequirements, Role role, Set<Tag> tags) {
        requireAllNonNull(name, year, studentNumber, email, phone, dietaryRequirements, role, tags);
        this.name = name;
        this.year = year;
        this.studentNumber = studentNumber;
        this.email = email;
        this.phone = phone;
        this.dietaryRequirements = dietaryRequirements;
        this.role = role;
        this.tags.addAll(tags);
    }

    public Name getName() {
        return name;
    }

    public Year getYear() {
        return year;
    }

    public StudentNumber getStudentNumber() {
        return studentNumber;
    }

    public Email getEmail() {
        return email;
    }

    public Phone getPhone() {
        return phone;
    }

    public DietaryRequirements getDietaryRequirements() {
        return dietaryRequirements;
    }

    public Role getRole() {
        return role;
    }

    // Legacy method for backward compatibility - returns dietary requirements as address
    public DietaryRequirements getAddress() {
        return dietaryRequirements;
    }

    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    /**
     * Returns true if both persons have the same student number (case-insensitive).
     * This defines identity for duplicate detection and uniqueness.
     * Duplicate names are allowed; uniqueness is enforced on student numbers only.
     */
    public boolean isSamePerson(Person otherPerson) {
        if (otherPerson == this) {
            return true;
        }

        return otherPerson != null
                && otherPerson.getStudentNumber().value.equalsIgnoreCase(getStudentNumber().value);
    }

    /**
     * Returns true if both persons have the same {@code StudentNumber}.
     * This defines equality based only on student identity, regardless of name or other fields.
     */
    public boolean isSameStudentNumber(Person otherPerson) {
        if (otherPerson == this) {
            return true;
        }

        return otherPerson != null
            && otherPerson.getStudentNumber().equals(getStudentNumber());
    }

    /**
     * Returns true if both persons have the same identity and data fields.
     * This defines a stronger notion of equality between two persons.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Person)) {
            return false;
        }

        Person otherPerson = (Person) other;
        return name.equals(otherPerson.name)
                && year.equals(otherPerson.year)
                && studentNumber.equals(otherPerson.studentNumber)
                && email.equals(otherPerson.email)
                && phone.equals(otherPerson.phone)
                && dietaryRequirements.equals(otherPerson.dietaryRequirements)
                && role.equals(otherPerson.role)
                && tags.equals(otherPerson.tags);
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, year, studentNumber, email, phone, dietaryRequirements, role, tags);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("name", name)
                .add("year", year)
                .add("studentNumber", studentNumber)
                .add("email", email)
                .add("phone", phone)
                .add("dietaryRequirements", dietaryRequirements)
                .add("role", role)
                .add("tags", tags)
                .toString();
    }

}
