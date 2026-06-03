package com.assignment4.busguidance;

// Import necessary classes for file handling and collections
import java.io.*;
import java.nio.file.*;
import java.util.*;

// Repository for managing Bus data stored in a text file
public class BusRepository {
    private final String filePath;

    // Constructor to initialize the repository with the file path
    public BusRepository(String filePath) {
        this.filePath = filePath;
    }

    // Method to add a new Bus to the repository
    public void add(Bus bus) throws IOException {
        if (!isValidBusID(bus.getBusID())) {
            throw new IllegalArgumentException("Bus ID must be exactly 8 digits.");
        }
        
        // Check for duplicate Bus ID before adding
        if (retrieve(bus.getBusID()) != null) {
            throw new IllegalArgumentException("Duplicate Bus ID");
        }

        // Append the new Bus data to the file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(
                bus.getBusID() + "," +
                bus.getCapacity() + "," +
                bus.getFuelLevel() + "," +
                bus.getFuelType());
            writer.newLine();
        }
    }

    // Method to retrieve a Bus by its ID
    public Bus retrieve(String busID) throws IOException {
        // Read all lines from the file
        List<String> lines = Files.readAllLines(Paths.get(filePath));

        // Iterate through the lines to find the Bus with the specified ID
        for (String line : lines) {
            String[] parts = line.split(",");
            if (parts[0].equals(busID)) {
                return new Bus(
                        parts[0],
                        Integer.parseInt(parts[1]),
                        Double.parseDouble(parts[2]),
                        parts[3]
                );
            }
        }
        return null;
    }

    // Method to count the total number of Buses in the repository
    public int count() throws IOException {
        // Read all lines from the file and return the count
        return Files.readAllLines(Paths.get(filePath)).size();
    }

    // Method to update an existing Bus's information
    public void update(Bus updatedBus) throws IOException {
        // Validate the Bus ID and check for the existence of the Bus before updating
        Bus existingBus = retrieve(updatedBus.getBusID());
        if (existingBus == null) {
            throw new IllegalArgumentException("Bus not found.");
        }
        // Validate that the capacity update does not violate the restriction
        if (!isValidCapacityUpdate(existingBus.getCapacity(), updatedBus.getCapacity())) {
            throw new IllegalArgumentException("Capacity cannot increase.");
        }
        
        // Read all lines from the file
        List<String> lines = Files.readAllLines(Paths.get(filePath));
        List<String> newLines = new ArrayList<>();
        boolean found = false;

        // Iterate through the lines to find and update the Bus with the specified ID
        for (String line : lines) {
            String[] parts = line.split(",");
            if (parts[0].equals(updatedBus.getBusID())) {
                newLines.add(
                        updatedBus.getBusID() + "," +
                        updatedBus.getCapacity() + "," +
                        updatedBus.getFuelLevel() + "," +
                        updatedBus.getFuelType());
                found = true;
            } else {
                newLines.add(line);
            }
        }

        // If the Bus was not found, throw an exception
        if (!found) {
            throw new IllegalArgumentException(
                    "Bus not found");
        }

        // Write the updated lines back to the file
        Files.write(
                Paths.get(filePath),
                newLines);
    }

    // Validation methods for Bus and Driver objects based on specified rules
    // B1 - Bus ID Rules
    // Validates that the bus ID is an 8-digit number
    public static boolean isValidBusID(String busID) {
        if (busID == null) {
            return false;
        }
        return busID.matches("\\d{8}");
    }
    
    // B2 - Capacity Update Restriction
    // Validates that the new capacity does not exceed the old capacity
    public static boolean isValidCapacityUpdate(int oldCapacity, int newCapacity) {
        return newCapacity <= oldCapacity;
    }
    
    // B3 - Driver Age Restriction
    // Validates that the driver's age is suitable for the bus capacity
    public static boolean isValidDriverAgeForBus(int driverAge, int busCapacity) {
        if (driverAge > 50 && busCapacity >= 50) {
            return false;
        }
        return true;
    }
    
    // B4 - Electric Bus Restriction
    // Validates that the driver has at least 5 years of experience if the bus is electric
    public static boolean isValidElectricBusExperience(int experienceYears, String fuelType) {
        if (fuelType == null) {
            return false;
        }
        if (fuelType.equalsIgnoreCase("Electricity")) {
            return experienceYears >= 5;
        }
        return true;
    }
    
    // B5 - Driver Licence Restriction
    // Validates that the driver's license type is appropriate for the bus's fuel type
    public static boolean isValidLicenceForBus(String licenceType, String fuelType) {
        if (licenceType == null || fuelType == null) {
            return false;
        }
        if (fuelType.equalsIgnoreCase("Electricity") || fuelType.equalsIgnoreCase("Hybrid")) {
            return licenceType.equalsIgnoreCase("Heavy") || licenceType.equalsIgnoreCase("PublicTransport");
        }
        return true;
    }
    
    // Used before assigning a driver to a bus in the repository.
    // Validates the assignment of a driver to a bus based on age, experience, and license type
    public static boolean validateDriverBusAssignment(Driver driver, Bus bus) {
        if (driver == null || bus == null) {
            return false;
        }
        boolean ageCheck = isValidDriverAgeForBus(driver.getAge(), bus.getCapacity());
        boolean experienceCheck = isValidElectricBusExperience(driver.getExperienceYears(), bus.getFuelType());
        boolean licenceCheck = isValidLicenceForBus(driver.getLicenseType(), bus.getFuelType());
        return ageCheck && experienceCheck && licenceCheck;
    }
}
