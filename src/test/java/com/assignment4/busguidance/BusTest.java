package com.assignment4.busguidance;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/// Test class for validating the Bus class and its related functionalities.
public class BusTest {
    // Testing for B1 - Bus ID Rules
    // Valid Bus ID: 8 digits
    @Test
    void validBusID() {
        assertTrue(BusRepository.isValidBusID("12345678"));
    }
    // Invalid Bus ID: 7 digits
    @Test
    void busIDTooShort() {
        assertFalse(BusRepository.isValidBusID("1234567"));
    }
    // Invalid Bus ID: contains letters
    @Test
    void busIDContainsLetters() {
        assertFalse(BusRepository.isValidBusID("12AB5678"));
    }

    // Testing for B2 - Capacity Update Restriction
    // Valid capacity update: decrease from 50 to 45
    @Test
    void decreaseCapacityAllowed() {
        assertTrue(BusRepository.isValidCapacityUpdate(50, 45));
    }
    // Valid capacity update: same capacity of 50
    @Test
    void sameCapacityAllowed() {
        assertTrue(BusRepository.isValidCapacityUpdate(50, 50));
    }
    // Invalid capacity update: increase from 50 to 55
    @Test
    void increaseCapacityRejected() {
        assertFalse(BusRepository.isValidCapacityUpdate(50, 55));
    }

    // Testing for B3 - Driver Age Restriction
    // Valid age: 49 can drive a bus with capacity 50
    @Test
    void age49CanDriveCapacity50Bus() {
        assertTrue(BusRepository.isValidDriverAgeForBus(49, 50));
    }
    // Invalid age: 51 cannot drive a bus with capacity 50
    @Test
    void age51CannotDriveCapacity50Bus() {
        assertFalse(BusRepository.isValidDriverAgeForBus(51, 50));
    }
    // Valid age: 60 can drive a bus with capacity 49
    @Test
    void age60CanDriveCapacity49Bus() {
        assertTrue(BusRepository.isValidDriverAgeForBus(60, 49));
    }

    // Testing for B4 - Electric Bus Restriction
    // Valid experience: 5 years can drive an electric bus
    @Test
    void experienceFiveCanDriveElectricBus() {
        assertTrue(BusRepository.isValidElectricBusExperience(5, "Electricity"));
    }
    // Invalid experience: 4 years cannot drive an electric bus
    @Test
    void experienceFourCannotDriveElectricBus() {
        assertFalse(BusRepository.isValidElectricBusExperience(4, "Electricity"));
    }
    // Valid experience: 15 years can drive an electric bus
    @Test
    void experienceFifteenCanDriveElectricBus() {
        assertTrue(BusRepository.isValidElectricBusExperience(15, "Electricity"));
    }

    // Testing for B5 - Driver Licence Restriction
    // Valid licence: Heavy can drive an electric bus   
    @Test
    void heavyLicenceCanDriveElectricBus() {
        assertTrue(BusRepository.isValidLicenceForBus("Heavy", "Electricity"));
    }
    // Valid licence: PublicTransport can drive a Hybrid bus
    @Test
    void publicTransportLicenceCanDriveHybridBus() {
        assertTrue(BusRepository.isValidLicenceForBus("PublicTransport", "Hybrid"));
    }
    // Invalid licence: Light cannot drive an electric bus
    @Test
    void lightLicenceCannotDriveElectricBus() {
        assertFalse(BusRepository.isValidLicenceForBus("Light", "Electricity"));
    }
}
