package com.assignment4.busguidance;

/*
 * Integration tests for Driver operations.
 * Unlike unit tests, these use a real TXT file and real classes — no mocks.
 * After each operation, a fresh DriverRepository is loaded from the same file
 * to confirm data was actually written to disk, not just stored in memory.
 */

import java.io.File;
import java.io.FileWriter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DriverIntegrationTest {

    // Separate file from unit tests to avoid interference
    private static final String TEST_FILE =
            "src/test/resources/test-data/driver-integration-data.txt";

    private DriverRepository repository;

    // Clears the test file and creates a fresh repository before every test
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

    // IT-D-01: Valid driver is stored and retrieved correctly from TXT file
    @Test
    void validDriverIsStoredAndRetrievedFromFile() {
        Driver driver = new Driver(
                "23@@4567AB",
                "Jon Snow",
                5,
                "Heavy",
                "12|Winterfell Road|Winterfell|North|Westeros",
                "15-04-1995"
        );

        assertTrue(repository.addDriver(driver));

        // Load a fresh repository from the same file to confirm data was written to disk
        DriverRepository freshRepository = new DriverRepository(TEST_FILE);
        Driver retrieved = freshRepository.retrieveDriver("23@@4567AB");

        // Check all fields match exactly
        assertNotNull(retrieved, "Driver should exist in the TXT file after being added.");
        assertEquals("23@@4567AB", retrieved.getDriverID());
        assertEquals("Jon Snow", retrieved.getName());
        assertEquals(5, retrieved.getExperienceYears());
        assertEquals("Heavy", retrieved.getLicenseType());
        assertEquals("12|Winterfell Road|Winterfell|North|Westeros", retrieved.getAddress());
        assertEquals("15-04-1995", retrieved.getBirthdate());
    }

    // IT-D-02: Invalid driver is rejected and nothing is written to the file
    @Test
    void invalidDriverIsRejectedAndNotSavedToFile() {
        // "BADID12345" fails D1 — first two characters must be digits 2-9, not letters
        Driver invalidDriver = new Driver(
                "BADID12345",
                "Invalid Person",
                3,
                "Light",
                "5|Test Street|Melbourne|Victoria|Australia",
                "01-01-1990"
        );

        assertFalse(repository.addDriver(invalidDriver),
                "Driver with invalid ID should be rejected.");

        // Load a fresh repository and confirm nothing was written
        DriverRepository freshRepository = new DriverRepository(TEST_FILE);
        assertEquals(0, freshRepository.countDrivers(),
                "TXT file should remain empty after an invalid driver is rejected.");
    }

    // IT-D-03: Updated driver details are persisted correctly to TXT file
    @Test
    void updatedDriverDetailsArePersistedToFile() {
        // Add the original driver
        Driver originalDriver = new Driver(
                "34##5678CD",
                "Sansa Stark",
                3,
                "Light",
                "5|Red Keep Road|Kings Landing|Crownlands|Westeros",
                "10-02-1998"
        );
        assertTrue(repository.addDriver(originalDriver));

        // Update allowed fields — D5: driverID and name must stay the same
        Driver updatedDriver = new Driver(
                "34##5678CD",
                "Sansa Stark",
                4,
                "Medium",
                "9|Mockingbird Lane|The Eyrie|The Vale|Westeros",
                "10-02-1998"
        );
        assertTrue(repository.updateDriver("34##5678CD", updatedDriver));

        // Load a fresh repository to confirm the update was written to disk
        DriverRepository freshRepository = new DriverRepository(TEST_FILE);
        Driver savedDriver = freshRepository.retrieveDriver("34##5678CD");

        assertNotNull(savedDriver);
        assertEquals(4, savedDriver.getExperienceYears());
        assertEquals("Medium", savedDriver.getLicenseType());
        assertEquals("9|Mockingbird Lane|The Eyrie|The Vale|Westeros", savedDriver.getAddress());
    }

    // IT-D-04: Record count updates correctly in TXT file after multiple adds
    @Test
    void recordCountUpdatesCorrectlyAfterEachAdd() {
        // File starts empty
        assertEquals(0, new DriverRepository(TEST_FILE).countDrivers());

        repository.addDriver(new Driver(
                "23@@4567AB", "Jon Snow", 5, "Heavy",
                "12|Winterfell Road|Winterfell|North|Westeros", "15-04-1995"
        ));
        assertEquals(1, new DriverRepository(TEST_FILE).countDrivers());

        repository.addDriver(new Driver(
                "45!!7890CD", "Arya Stark", 2, "Light",
                "8|Needle Street|Winterfell|North|Westeros", "15-06-2000"
        ));
        assertEquals(2, new DriverRepository(TEST_FILE).countDrivers());

        repository.addDriver(new Driver(
                "67$$2345EF", "Sansa Stark", 3, "Medium",
                "5|Red Keep Road|Kings Landing|Crownlands|Westeros", "10-02-1998"
        ));
        assertEquals(3, new DriverRepository(TEST_FILE).countDrivers());
    }

    // IT-D-05: Duplicate driver ID is rejected and file count stays unchanged
    @Test
    void duplicateDriverIDIsRejectedAndFileCountUnchanged() {
        Driver firstDriver = new Driver(
                "23@@4567AB", "Jon Snow", 5, "Heavy",
                "12|Winterfell Road|Winterfell|North|Westeros", "15-04-1995"
        );
        assertTrue(repository.addDriver(firstDriver));

        // Try to add a second driver with the same ID — D1 requires unique IDs
        Driver duplicateDriver = new Driver(
                "23@@4567AB",
                "Ghost Rider",
                2,
                "Light",
                "1|Dragon Road|Dragonstone|Dragonstone|Westeros",
                "01-01-2000"
        );
        assertFalse(repository.addDriver(duplicateDriver),
                "Driver with duplicate ID should be rejected.");

        // Only the original driver should be in the file
        assertEquals(1, new DriverRepository(TEST_FILE).countDrivers());
    }
}