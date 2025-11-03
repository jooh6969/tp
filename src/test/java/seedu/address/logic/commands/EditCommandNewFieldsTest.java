package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DIETARY_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ROLE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.logic.commands.CommandTestUtil.VALID_YEAR_BOB;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.EditCommand.EditPersonDescriptor;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.testutil.EditPersonDescriptorBuilder;
import seedu.address.testutil.PersonBuilder;

/**
 * Contains comprehensive tests for EditCommand with the new club member fields.
 */
public class EditCommandNewFieldsTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_allNewFieldsSpecifiedUnfilteredList_success() {
        Person editedPerson = new PersonBuilder()
                .withName("John Doe")
                .withYear("4")
                .withStudentNumber("A1234567X")
                .withEmail("john@example.com")
                .withPhone("98765432")
                .withDietaryRequirements("Vegetarian")
                .withRole("President")
                .withTags("leadership")
                .build();
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder(editedPerson).build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON, descriptor);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS, Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(model.getFilteredPersonList().get(0), editedPerson);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_someNewFieldsSpecifiedUnfilteredList_success() {
        Index indexLastPerson = Index.fromOneBased(model.getFilteredPersonList().size());
        Person lastPerson = model.getFilteredPersonList().get(indexLastPerson.getZeroBased());

        PersonBuilder personInList = new PersonBuilder(lastPerson);
        Person editedPerson = personInList.withName(VALID_NAME_BOB)
                .withYear(VALID_YEAR_BOB)
                .withStudentNumber("Z9999999Z")
                .withEmail(VALID_EMAIL_BOB)
                .withPhone(VALID_PHONE_BOB)
                .withDietaryRequirements(VALID_DIETARY_BOB)
                .withRole(VALID_ROLE_BOB)
                .withTags(VALID_TAG_HUSBAND).build();

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder()
                .withName(VALID_NAME_BOB)
                .withYear(VALID_YEAR_BOB)
                .withStudentNumber("Z9999999Z")
                .withEmail(VALID_EMAIL_BOB)
                .withPhone(VALID_PHONE_BOB)
                .withDietaryRequirements(VALID_DIETARY_BOB)
                .withRole(VALID_ROLE_BOB)
                .withTags(VALID_TAG_HUSBAND).build();
        EditCommand editCommand = new EditCommand(indexLastPerson, descriptor);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS, Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(lastPerson, editedPerson);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_editOnlyYear_success() {
        Index indexFirstPerson = INDEX_FIRST_PERSON;
        Person personInList = model.getFilteredPersonList().get(indexFirstPerson.getZeroBased());

        PersonBuilder personInListBuilder = new PersonBuilder(personInList);
        Person editedPerson = personInListBuilder.withYear("4").build();

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder()
                .withYear("4").build();
        EditCommand editCommand = new EditCommand(indexFirstPerson, descriptor);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS, Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(personInList, editedPerson);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_editOnlyStudentNumber_success() {
        Index indexFirstPerson = INDEX_FIRST_PERSON;
        Person personInList = model.getFilteredPersonList().get(indexFirstPerson.getZeroBased());

        PersonBuilder personInListBuilder = new PersonBuilder(personInList);
        Person editedPerson = personInListBuilder.withStudentNumber("A9999999X").build();

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder()
                .withStudentNumber("A9999999X").build();
        EditCommand editCommand = new EditCommand(indexFirstPerson, descriptor);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS, Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(personInList, editedPerson);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_editOnlyDietaryRequirements_success() {
        Index indexFirstPerson = INDEX_FIRST_PERSON;
        Person personInList = model.getFilteredPersonList().get(indexFirstPerson.getZeroBased());

        PersonBuilder personInListBuilder = new PersonBuilder(personInList);
        Person editedPerson = personInListBuilder.withDietaryRequirements("Halal").build();

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder()
                .withDietaryRequirements("Halal").build();
        EditCommand editCommand = new EditCommand(indexFirstPerson, descriptor);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS, Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(personInList, editedPerson);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_editOnlyRole_success() {
        Index indexFirstPerson = INDEX_FIRST_PERSON;
        Person personInList = model.getFilteredPersonList().get(indexFirstPerson.getZeroBased());

        PersonBuilder personInListBuilder = new PersonBuilder(personInList);
        Person editedPerson = personInListBuilder.withRole("Vice President").build();

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder()
                .withRole("Vice President").build();
        EditCommand editCommand = new EditCommand(indexFirstPerson, descriptor);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS, Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(personInList, editedPerson);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_editMultipleNewFields_success() {
        Index indexFirstPerson = INDEX_FIRST_PERSON;
        Person personInList = model.getFilteredPersonList().get(indexFirstPerson.getZeroBased());

        PersonBuilder personInListBuilder = new PersonBuilder(personInList);
        Person editedPerson = personInListBuilder
                .withYear("4")
                .withStudentNumber("A8888888X")
                .withDietaryRequirements("Vegan")
                .withRole("Secretary")
                .build();

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder()
                .withYear("4")
                .withStudentNumber("A8888888X")
                .withDietaryRequirements("Vegan")
                .withRole("Secretary")
                .build();
        EditCommand editCommand = new EditCommand(indexFirstPerson, descriptor);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS, Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(personInList, editedPerson);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_filteredList_success() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Person personInFilteredList = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person editedPerson = new PersonBuilder(personInFilteredList)
                .withName(VALID_NAME_BOB)
                .withYear(VALID_YEAR_BOB)
                .withRole(VALID_ROLE_BOB)
                .build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON,
                new EditPersonDescriptorBuilder()
                        .withName(VALID_NAME_BOB)
                        .withYear(VALID_YEAR_BOB)
                        .withRole(VALID_ROLE_BOB)
                        .build());

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS, Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(model.getFilteredPersonList().get(0), editedPerson);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_duplicatePersonUnfilteredList_failure() {
        Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder(firstPerson).build();
        EditCommand editCommand = new EditCommand(INDEX_SECOND_PERSON, descriptor);

        assertCommandFailure(editCommand, model, EditCommand.MESSAGE_DUPLICATE_STUDENT_NUMBER);
    }

    @Test
    public void execute_duplicatePersonFilteredList_failure() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        // edit person in filtered list into a duplicate in address book
        Person personInList = model.getAddressBook().getPersonList().get(INDEX_SECOND_PERSON.getZeroBased());
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON,
                new EditPersonDescriptorBuilder(personInList).build());

        assertCommandFailure(editCommand, model, EditCommand.MESSAGE_DUPLICATE_STUDENT_NUMBER);
    }

    @Test
    public void execute_invalidPersonIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder()
                .withName(VALID_NAME_BOB)
                .withYear(VALID_YEAR_BOB)
                .build();
        EditCommand editCommand = new EditCommand(outOfBoundIndex, descriptor);

        assertCommandFailure(editCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_invalidPersonIndexFilteredList_failure() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);
        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getPersonList().size());

        EditCommand editCommand = new EditCommand(outOfBoundIndex,
                new EditPersonDescriptorBuilder()
                        .withName(VALID_NAME_BOB)
                        .withYear(VALID_YEAR_BOB)
                        .build());

        assertCommandFailure(editCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_editYearToAllValidValues_success() {
        Index indexFirstPerson = INDEX_FIRST_PERSON;
        Person personInList = model.getFilteredPersonList().get(indexFirstPerson.getZeroBased());

        // Test editing to each valid year (1-10)
        for (int year = 1; year <= 10; year++) {
            PersonBuilder personInListBuilder = new PersonBuilder(personInList);
            Person editedPerson = personInListBuilder.withYear(String.valueOf(year)).build();

            EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder()
                    .withYear(String.valueOf(year)).build();
            EditCommand editCommand = new EditCommand(indexFirstPerson, descriptor);

            String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS,
                    Messages.format(editedPerson));

            Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
            expectedModel.setPerson(personInList, editedPerson);

            assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);

            // Update the person in list for next iteration
            personInList = editedPerson;
        }
    }

    @Test
    public void execute_editRoleToVariousValues_success() {
        Index indexFirstPerson = INDEX_FIRST_PERSON;
        Person personInList = model.getFilteredPersonList().get(indexFirstPerson.getZeroBased());

        String[] roles = {"President", "Vice President", "Secretary", "Treasurer", "Member",
            "Committee Member", "Event Coordinator"};

        for (String role : roles) {
            PersonBuilder personInListBuilder = new PersonBuilder(personInList);
            Person editedPerson = personInListBuilder.withRole(role).build();

            EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder()
                    .withRole(role).build();
            EditCommand editCommand = new EditCommand(indexFirstPerson, descriptor);

            String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS,
                    Messages.format(editedPerson));

            Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
            expectedModel.setPerson(personInList, editedPerson);

            assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);

            // Update the person in list for next iteration
            personInList = editedPerson;
        }
    }

    @Test
    public void execute_editDietaryRequirementsToVariousValues_success() {
        Index indexFirstPerson = INDEX_FIRST_PERSON;
        Person personInList = model.getFilteredPersonList().get(indexFirstPerson.getZeroBased());

        String[] dietaryOptions = {
            "No restrictions", "Vegetarian", "Vegan", "Halal", "Kosher",
            "Gluten-free", "Dairy-free", "Nut allergy", "Seafood allergy", "Multiple allergies"
        };

        for (String dietary : dietaryOptions) {
            PersonBuilder personInListBuilder = new PersonBuilder(personInList);
            Person editedPerson = personInListBuilder.withDietaryRequirements(dietary).build();

            EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder()
                    .withDietaryRequirements(dietary).build();
            EditCommand editCommand = new EditCommand(indexFirstPerson, descriptor);

            String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS,
                    Messages.format(editedPerson));

            Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
            expectedModel.setPerson(personInList, editedPerson);

            assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);

            // Update the person in list for next iteration
            personInList = editedPerson;
        }
    }

    @Test
    public void execute_editStudentNumberToVariousValues_success() {
        Index indexFirstPerson = INDEX_FIRST_PERSON;
        Person personInList = model.getFilteredPersonList().get(indexFirstPerson.getZeroBased());

        String[] studentNumbers = {
            "A1234567X", "B9876543Y", "C1111111Z", "D9999999A",
            "E5555555B", "F7777777C", "G3333333D", "H8888888E"
        };

        for (String studentNumber : studentNumbers) {
            PersonBuilder personInListBuilder = new PersonBuilder(personInList);
            Person editedPerson = personInListBuilder.withStudentNumber(studentNumber).build();

            EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder()
                    .withStudentNumber(studentNumber).build();
            EditCommand editCommand = new EditCommand(indexFirstPerson, descriptor);

            String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS,
                    Messages.format(editedPerson));

            Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
            expectedModel.setPerson(personInList, editedPerson);

            assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);

            // Update the person in list for next iteration
            personInList = editedPerson;
        }
    }

    @Test
    public void equals() {
        EditPersonDescriptor descriptorAmy = new EditPersonDescriptorBuilder()
                .withName("Amy Bee")
                .withYear("2")
                .withStudentNumber("A1111111X")
                .withPhone("11111111")
                .withEmail("amy@example.com")
                .withDietaryRequirements("Vegetarian")
                .withRole("Member")
                .build();
        EditPersonDescriptor descriptorBob = new EditPersonDescriptorBuilder()
                .withName("Bob Choo")
                .withYear("3")
                .withStudentNumber("B2222222Y")
                .withPhone("22222222")
                .withEmail("bob@example.com")
                .withDietaryRequirements("No restrictions")
                .withRole("President")
                .build();
        final EditCommand standardCommand = new EditCommand(INDEX_FIRST_PERSON, descriptorAmy);

        // same values -> returns true
        EditPersonDescriptor copyDescriptor = new EditPersonDescriptor(descriptorAmy);
        EditCommand commandWithSameValues = new EditCommand(INDEX_FIRST_PERSON, copyDescriptor);
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(new EditCommand(INDEX_SECOND_PERSON, descriptorAmy)));

        // different descriptor -> returns false
        assertFalse(standardCommand.equals(new EditCommand(INDEX_FIRST_PERSON, descriptorBob)));
    }

    @Test
    public void toStringMethod() {
        Index index = Index.fromOneBased(1);
        EditPersonDescriptor editPersonDescriptor = new EditPersonDescriptorBuilder()
                .withName("Test Person")
                .withYear("3")
                .withRole("President")
                .build();
        EditCommand editCommand = new EditCommand(index, editPersonDescriptor);
        String expected = EditCommand.class.getCanonicalName() + "{index=" + index + ", editPersonDescriptor="
                + editPersonDescriptor + "}";
        assertEquals(expected, editCommand.toString());
    }
}
