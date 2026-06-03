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
}
