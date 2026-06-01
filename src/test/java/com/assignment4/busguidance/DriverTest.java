package com.assignment4.busguidance;

/**
 * Referennces:
 * https://www.geeksforgeeks.org/advance-java/writing-templates-for-test-cases-using-junit-5/
 * https://github.com/robsonagapito/unit-testing-java
 */
import java.io.File;
import java.io.FileWriter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for Driver conditions D1 to D5.
 * - 3 tests for D1
 * - 3 tests for D2
 * - 3 tests for D3
 * - 3 tests for D4
 * - 3 tests for D5
 */
public class DriverTest {

    /*
     * file is only used for testing; keeps test data separate from the real driver-data.txt file.
     */
    private static final String TEST_FILE = "src/test/resources/test-data/driver-test-data.txt";

    private DriverRepository repository;

    /**
     * Runs before every test-clears the test file so each test starts with no saved drivers.
     */
    @BeforeEach
    void setUp() throws Exception {
        File file = new File(TEST_FILE);

        if (file.getParentFile() != null) {
            file.getParentFile().mkdirs();
        }

        FileWriter writer = new FileWriter(file);
        writer.write("");
        writer.close();

        repository = new DriverRepository(TEST_FILE);
    }

    /**
     * Reusable valid driver for testing
     */
    private Driver createValidDriver() {
        return new Driver(
                "23@@4567AB",
                "Jon Snow",
                5,
                "Heavy",
                "12|Winterfell Road|Winterfell|North|Westeros",
                "15-04-1995"
        );
    }

    
    // D1: Driver ID Rules- A valid driverID should be accepted.

    @Test
    void validDriverIDShouldBeAccepted() {
        assertTrue(repository.isValidDriverID("23@@4567AB"));
    }

    /**
     * D1 invalid input: (driverID with the wrong length is  rejected)
     */
    @Test
    void driverIDWithWrongLengthShouldBeRejected() {
        assertFalse(repository.isValidDriverID("23@@456AB"));
    }

    /**
     * D1 invalid input (Duplicate driver IDs are rejected aswell)
     */
    @Test
    void duplicateDriverIDShouldBeRejected() {
        Driver firstDriver = createValidDriver();

        Driver duplicateDriver = new Driver(
                "23@@4567AB",
                "Arya Stark",
                3,
                "Medium",
                "8|Needle Street|Winterfell|North|Westeros",
                "22-06-2000"
        );

        assertTrue(repository.addDriver(firstDriver));
        assertFalse(repository.addDriver(duplicateDriver));
    }

  
    // D2: Address Format (sould be: Street Number|Street Name|City|State|Country)
    
    @Test
    void validAddressShouldBeAccepted() {
        assertTrue(repository.isValidAddress("12|Winterfell Road|Winterfell|North|Westeros"));
    }

    /**
     * D2 invalid input (Address is missing one mandatory field)
     */
    @Test
    void addressWithMissingFieldShouldBeRejected() {
        assertFalse(repository.isValidAddress("12|Winterfell Road|Winterfell|Westeros"));
    }

    /**
     * D2 edge/invalid case (address has five parts but one part  empty)
     */
    @Test
    void addressWithEmptyFieldShouldBeRejected() {
        assertFalse(repository.isValidAddress("12|Winterfell Road||North|Westeros"));
    }


    // D3: Birthdate Format (njormal case: Birthdate follows DD-MM-YYYY format.)
   
    
    @Test
    void validBirthdateShouldBeAccepted() {
        assertTrue(repository.isValidBirthdate("15-04-1995"));
    }

    /**
     * D3 invalid input (Birthdate uses slashes instead of hyphens)
     */
    @Test
    void birthdateWithSlashesShouldBeRejected() {
        assertFalse(repository.isValidBirthdate("15/04/1995"));
    }

    /**
     * D3 edge/invalid case (the date is impossible)
     */
    @Test
    void impossibleBirthdateShouldBeRejected() {
        assertFalse(repository.isValidBirthdate("31-02-1995"));
    }

    // D4: Licence Update Restriction (invalid case: A driver > 10 years of experience cant change licence type.)
    
    @Test
    void driverWithMoreThanTenYearsCannotChangeLicenseType() {
        Driver originalDriver = new Driver(
                "45!!7890CD",
                "Ned Stark",
                11,
                "Heavy",
                "1|Stark Street|Winterfell|North|Westeros",
                "01-01-1970"
        );

        Driver updatedDriver = new Driver(
                "45!!7890CD",
                "Ned Stark",
                11,
                "PublicTransport",
                "1|Stark Street|Winterfell|North|Westeros",
                "01-01-1970"
        );

        assertTrue(repository.addDriver(originalDriver));
        assertFalse(repository.updateDriver("45!!7890CD", updatedDriver));
    }

    /**
     * D4 edge case: (10 yeatrs exactly allow license type change)
     */
    @Test
    void driverWithExactlyTenYearsCanChangeLicenseType() {
        Driver originalDriver = new Driver(
                "56##1234EF",
                "Robb Stark",
                10,
                "Medium",
                "20|King Road|Winterfell|North|Westeros",
                "10-05-1990"
        );

        Driver updatedDriver = new Driver(
                "56##1234EF",
                "Robb Stark",
                10,
                "Heavy",
                "20|King Road|Winterfell|North|Westeros",
                "10-05-1990"
        );

        assertTrue(repository.addDriver(originalDriver));
        assertTrue(repository.updateDriver("56##1234EF", updatedDriver));
    }

    /**
     * D4 normal allowed case: driver with more than 10 years can still update other detailsif the licence type stays the same.
    
     */
    @Test
    void driverWithMoreThanTenYearsCanUpdateAddressIfLicenseStaysSame() {
        Driver originalDriver = new Driver(
                "67$$2345GH",
                "Tyrion Lannister",
                12,
                "PublicTransport",
                "3|Casterly Road|Casterly Rock|Westerlands|Westeros",
                "11-11-1980"
        );

        Driver updatedDriver = new Driver(
                "67$$2345GH",
                "Tyrion Lannister",
                12,
                "PublicTransport",
                "9|Harbour Street|Kings Landing|Crownlands|Westeros",
                "11-11-1980"
        );

        assertTrue(repository.addDriver(originalDriver));
        assertTrue(repository.updateDriver("67$$2345GH", updatedDriver));
    }

   
    // D5: Immutable Fields (D5 invalid case:driverID cannot be changed during update.)

    @Test
    void updatingDriverIDShouldBeRejected() {
        Driver originalDriver = createValidDriver();

        Driver updatedDriver = new Driver(
                "78%%3456IJ",
                "Jon Snow",
                5,
                "Heavy",
                "12|Winterfell Road|Winterfell|North|Westeros",
                "15-04-1995"
        );

        assertTrue(repository.addDriver(originalDriver));
        assertFalse(repository.updateDriver("23@@4567AB", updatedDriver));
    }

    /**
     * D5 invalid case:name cant be changed during update.
    
     */
    @Test
    void updatingNameShouldBeRejected() {
        Driver originalDriver = createValidDriver();

        Driver updatedDriver = new Driver(
                "23@@4567AB",
                "Aegon Targaryen",
                5,
                "Heavy",
                "12|Winterfell Road|Winterfell|North|Westeros",
                "15-04-1995"
        );

        assertTrue(repository.addDriver(originalDriver));
        assertFalse(repository.updateDriver("23@@4567AB", updatedDriver));
    }

    /**
     * D5 normal allowed case: If driverID and name stay the same, allowed fields can be updated.
     */
     
    @Test
    void updatingAllowedFieldsShouldBeAccepted() {
        Driver originalDriver = createValidDriver();

        Driver updatedDriver = new Driver(
                "23@@4567AB",
                "Jon Snow",
                6,
                "PublicTransport",
                "100|Castle Black Road|The Wall|North|Westeros",
                "15-04-1995"
        );

        assertTrue(repository.addDriver(originalDriver));
        assertTrue(repository.updateDriver("23@@4567AB", updatedDriver));

        Driver savedDriver = repository.retrieveDriver("23@@4567AB");

        assertEquals(6, savedDriver.getExperienceYears());
        assertEquals("PublicTransport", savedDriver.getLicenseType());
        assertEquals("100|Castle Black Road|The Wall|North|Westeros", savedDriver.getAddress());
    }
}