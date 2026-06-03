package com.assignment4.busguidance;

import org.junit.jupiter.api.Test;
import validation.BusValidator;
import static org.junit.jupiter.api.Assertions.*;

/// Test class for validating the Bus class and its related functionalities.
public class BusTest {
    // Testing for B1 - Bus ID Rules
    // Valid Bus ID: 8 digits
    @Test
    void validBusID() {
        assertTrue(BusValidator.validBusID("12345678"));
    }
    // Invalid Bus ID: 7 digits
    @Test
    void busIDTooShort() {
        assertFalse(BusValidator.validBusID("1234567"));
    }
    // Invalid Bus ID: contains letters
    @Test
    void busIDContainsLetters() {
        assertFalse(BusValidator.validBusID("12AB5678"));
    }

    // Testing for B2 - Capacity Update Restriction
    // Valid capacity update: decrease from 50 to 45
    @Test
    void decreaseCapacityAllowed() {
        assertTrue(BusValidator.validCapacityUpdate(50, 45));
    }
    // Valid capacity update: same capacity of 50
    @Test
    void sameCapacityAllowed() {
        assertTrue(BusValidator.validCapacityUpdate(50, 50));
    }
    // Invalid capacity update: increase from 50 to 55
    @Test
    void increaseCapacityRejected() {
        assertFalse(BusValidator.validCapacityUpdate(50, 55));
    }

    // Testing for B3 - Driver Age Restriction
    // Valid age: 49 can drive a bus with capacity 50
    @Test
    void age49CanDriveCapacity50Bus() {
        Driver driver = new Driver(
                "34@@12#1AB",
                "John",
                5,
                "Heavy",
                "1|Main Street|Melbourne|VIC|Australia",
                "01-01-1977"
        );
        Bus bus = new Bus(
                "12345678",
                50,
                80,
                "Diesel"
        );
        assertTrue(BusValidator.validateAgeRestriction(49, bus));
    }
    // Invalid age: 51 cannot drive a bus with capacity 50
    @Test
    void age51CannotDriveCapacity50Bus() {
        Bus bus = new Bus(
                "12345678",
                50,
                80,
                "Diesel"
        );
        assertFalse(BusValidator.validateAgeRestriction(51, bus));
    }
    // Valid age: 60 can drive a bus with capacity 49
    @Test
    void age60CanDriveCapacity49Bus() {
        Bus bus = new Bus(
                "12345678",
                49,
                80,
                "Diesel"
        );
        assertTrue(BusValidator.validateAgeRestriction(60, bus));
    }

    // Testing for B4 - Electric Bus Restriction
    // Valid experience: 5 years can drive an electric bus
    @Test
    void experienceFiveCanDriveElectricBus() {
        Driver driver = new Driver(
                "34@@12#1AB",
                "John",
                5,
                "Heavy",
                "1|Main Street|Melbourne|VIC|Australia",
                "01-01-1990"
        );
        Bus bus = new Bus(
                "12345678",
                40,
                100,
                "Electricity"
        );
        assertTrue(BusValidator.validateElectricBusExperience(driver, bus));
    }
    // Invalid experience: 4 years cannot drive an electric bus
    @Test
    void experienceFourCannotDriveElectricBus() {
        Driver driver = new Driver(
                "34@@12#1AB",
                "John",
                4,
                "Heavy",
                "1|Main Street|Melbourne|VIC|Australia",
                "01-01-1990"
        );
        Bus bus = new Bus(
                "12345678",
                40,
                100,
                "Electricity"
        );
        assertFalse(BusValidator.validateElectricBusExperience(driver, bus));
    }
    // Valid experience: 15 years can drive an electric bus
    @Test
    void experienceFifteenCanDriveElectricBus() {
        Driver driver = new Driver(
                "34@@12#1AB",
                "John",
                15,
                "Heavy",
                "1|Main Street|Melbourne|VIC|Australia",
                "01-01-1990"
        );
        Bus bus = new Bus(
                "12345678",
                40,
                100,
                "Electricity"
        );
        assertTrue(BusValidator.validateElectricBusExperience(driver, bus));
    }

    // Testing for B5 - Driver Licence Restriction
    // Valid licence: Heavy can drive an electric bus   
    @Test
    void heavyLicenceCanDriveElectricBus() {
        Driver driver = new Driver(
                "34@@12#1AB",
                "John",
                10,
                "Heavy",
                "1|Main Street|Melbourne|VIC|Australia",
                "01-01-1990"
        );
        Bus bus = new Bus(
                "12345678",
                40,
                100,
                "Electricity"
        );
        assertTrue(BusValidator.validateLicenceRestriction(driver, bus));
    }
    // Valid licence: PublicTransport can drive a Hybrid bus
    @Test
    void publicTransportLicenceCanDriveHybridBus() {
        Driver driver = new Driver(
                "34@@12#1AB",
                "John",
                10,
                "PublicTransport",
                "1|Main Street|Melbourne|VIC|Australia",
                "01-01-1990"
        );
        Bus bus = new Bus(
                "12345678",
                40,
                100,
                "Hybrid"
        );
        assertTrue(BusValidator.validateLicenceRestriction(driver, bus));
    }
    // Invalid licence: Light cannot drive an electric bus
    @Test
    void lightLicenceCannotDriveElectricBus() {
        Driver driver = new Driver(
                "34@@12#1AB",
                "John",
                10,
                "Light",
                "1|Main Street|Melbourne|VIC|Australia",
                "01-01-1990"
        );
        Bus bus = new Bus(
                "12345678",
                40,
                100,
                "Electricity"
        );
        assertFalse(BusValidator.validateLicenceRestriction(driver, bus));
    }
}
