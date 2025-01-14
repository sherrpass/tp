package seedu.siasa.storage;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static seedu.siasa.testutil.Assert.assertThrows;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.siasa.commons.exceptions.DataConversionException;
import seedu.siasa.model.ReadOnlySiasa;
import seedu.siasa.model.Siasa;

public class JsonSiasaStorageTest {
    private static final Path TEST_DATA_FOLDER = Paths.get("src", "test", "data", "JsonAddressBookStorageTest");

    @TempDir
    public Path testFolder;

    @Test
    public void readAddressBook_nullFilePath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> readAddressBook(null));
    }

    private java.util.Optional<ReadOnlySiasa> readAddressBook(String filePath) throws Exception {
        return new JsonSiasaStorage(Paths.get(filePath)).readSiasa(addToTestDataPathIfNotNull(filePath));
    }

    private Path addToTestDataPathIfNotNull(String prefsFileInTestDataFolder) {
        return prefsFileInTestDataFolder != null
                ? TEST_DATA_FOLDER.resolve(prefsFileInTestDataFolder)
                : null;
    }

    @Test
    public void read_missingFile_emptyResult() throws Exception {
        assertFalse(readAddressBook("NonExistentFile.json").isPresent());
    }

    @Test
    public void read_notJsonFormat_exceptionThrown() {
        assertThrows(DataConversionException.class, () -> readAddressBook("notJsonFormatAddressBook.json"));
    }

    @Test
    public void readAddressBook_invalidPersonAddressBook_throwDataConversionException() {
        assertThrows(DataConversionException.class, () -> readAddressBook("invalidPersonAddressBook.json"));
    }

    @Test
    public void readAddressBook_invalidAndValidPersonAddressBook_throwDataConversionException() {
        assertThrows(DataConversionException.class, () -> readAddressBook("invalidAndValidPersonAddressBook.json"));
    }

    /*
    @Test
    public void readAndSaveAddressBook_allInOrder_success() throws Exception {
        Path filePath = testFolder.resolve("TempAddressBook.json");
        Siasa original = getTypicalSiasa();
        JsonAddressBookStorage jsonAddressBookStorage = new JsonAddressBookStorage(filePath);

        // Save in new file and read back
        jsonAddressBookStorage.saveSiasa(original, filePath);
        ReadOnlySiasa readBack = jsonAddressBookStorage.readSiasa(filePath).get();
        assertEquals(original, new Siasa(readBack));

        // Modify data, overwrite exiting file, and read back
        original.addPerson(HOON);
        original.removePersonAndAssociatedPolicies(ALICE);
        jsonAddressBookStorage.saveSiasa(original, filePath);
        readBack = jsonAddressBookStorage.readSiasa(filePath).get();
        assertEquals(original, new Siasa(readBack));

        // Save and read without specifying file path
        original.addPerson(IDA);
        jsonAddressBookStorage.saveSiasa(original); // file path not specified
        readBack = jsonAddressBookStorage.readSiasa().get(); // file path not specified
        assertEquals(original, new Siasa(readBack));

    }

    @Test
    public void saveAddressBook_nullAddressBook_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> saveSiasa(null, "SomeFile.json"));
    }

    /**
     * Saves {@code addressBook} at the specified {@code filePath}.
     */
    private void saveAddressBook(ReadOnlySiasa addressBook, String filePath) {
        try {
            new JsonSiasaStorage(Paths.get(filePath))
                    .saveSiasa(addressBook, addToTestDataPathIfNotNull(filePath));
        } catch (IOException ioe) {
            throw new AssertionError("There should not be an error writing to the file.", ioe);
        }
    }

    @Test
    public void saveAddressBook_nullFilePath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> saveAddressBook(new Siasa(), null));
    }
}
