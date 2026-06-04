package com.assignment4.busguidance;

/*
 * Integration tests for Bus operations.
 * Unlike unit tests, these use a real TXT file and real classes — no mocks.
 * BusRepository reads directly from the file on every call, so retrieve()
 * and count() always reflect what is actually stored on disk.
 */

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BusIntegrationTest {

    // Separate file from unit tests to avoid interference
    private static final String TEST_FILE =
            "src/test/resources/test-data/bus-integration-data.txt";

    private BusRepository repository;

    // Creates and clears the test file before every test
    // The file must exist before retrieve() and count() are called
    @BeforeEach
    void setUp() throws Exception {
        File file = new File(TEST_FILE);
        if (file.getParentFile() != null) {
            file.getParentFile().mkdirs();
        }
        FileWriter writer = new FileWriter(file);
        writer.write("");
        writer.close();
        repository = new BusRepository(TEST_FILE);
    }

    // IT-B-01: Valid bus is stored and retrieved correctly from TXT file
    @Test
    void validBusIsStoredAndRetrievedFromFile() throws IOException {
        // busID "12345678" satisfies B1 — exactly 8 digits
        Bus bus = new Bus("12345678", 50, 80.0, "Diesel");
        repository.add(bus);

        // retrieve() reads directly from the TXT file, confirming the record was written to disk
        Bus retrieved = repository.retrieve("12345678");

        assertNotNull(retrieved, "Bus should be retrievable from TXT file after being added.");
        assertEquals("12345678", retrieved.getBusID());
        assertEquals(50, retrieved.getCapacity());
        assertEquals(80.0, retrieved.getFuelLevel(), 0.001);
        assertEquals("Diesel", retrieved.getFuelType());
    }

    // IT-B-02: Invalid bus is rejected and not written to TXT file
    @Test
    void invalidBusIsRejectedAndNotSavedToFile() throws IOException {
        // "ABCD1234" fails B1 — contains letters, not digits
        Bus invalidBus = new Bus("ABCD1234", 40, 60.0, "Diesel");

        assertThrows(IllegalArgumentException.class,
                () -> repository.add(invalidBus),
                "Bus with non-digit ID should be rejected.");

        // File should still be empty
        assertEquals(0, repository.count(),
                "TXT file should remain empty after an invalid bus is rejected.");
    }

    // IT-B-03: Updated bus details are persisted correctly to TXT file
    @Test
    void updatedBusDetailsArePersistedToFile() throws IOException {
        // Add original bus with capacity 50
        Bus originalBus = new Bus("12345678", 50, 80.0, "Diesel");
        repository.add(originalBus);

        // Update with lower capacity — B2 allows capacity to decrease
        Bus updatedBus = new Bus("12345678", 40, 75.0, "Hybrid");
        repository.update(updatedBus);

        // retrieve() reads from file — confirms update was written to disk
        Bus retrieved = repository.retrieve("12345678");

        assertNotNull(retrieved);
        assertEquals(40, retrieved.getCapacity(), "Decreased capacity should be persisted.");
        assertEquals(75.0, retrieved.getFuelLevel(), 0.001, "Updated fuel level should be persisted.");
        assertEquals("Hybrid", retrieved.getFuelType(), "Updated fuel type should be persisted.");
    }

    // IT-B-04: Record count updates correctly after multiple adds
    @Test
    void recordCountUpdatesCorrectlyAfterEachAdd() throws IOException {
        // File starts empty
        assertEquals(0, repository.count());

        repository.add(new Bus("11111111", 30, 70.0, "Diesel"));
        assertEquals(1, repository.count());

        repository.add(new Bus("22222222", 45, 90.0, "Hybrid"));
        assertEquals(2, repository.count());

        repository.add(new Bus("33333333", 60, 50.0, "Electricity"));
        assertEquals(3, repository.count());
    }

    // IT-B-05: Capacity increase during update is rejected (B2)
    @Test
    void capacityIncreaseIsRejectedAndOriginalValueUnchanged() throws IOException {
        // Add a bus with capacity 30
        Bus originalBus = new Bus("44444444", 30, 75.0, "Hybrid");
        repository.add(originalBus);

        // Try to increase capacity to 50 — B2 does not allow this
        Bus increasedCapacity = new Bus("44444444", 50, 75.0, "Hybrid");

        assertThrows(IllegalArgumentException.class,
                () -> repository.update(increasedCapacity),
                "Increasing bus capacity should be rejected (B2).");

        // Original capacity should be unchanged in the file
        Bus retrieved = repository.retrieve("44444444");
        assertEquals(30, retrieved.getCapacity(),
                "Capacity should remain 30 after rejected update.");
    }
}