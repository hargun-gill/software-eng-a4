package com.assignment4.busguidance;

/**
 * Reference used: https://itsourcecode.com/free-projects/java-projects/transport-management-system-project-in-java-with-source-code/
 * https://www.diffblue.com/resources/java-unit-testing-a-complete-guide-for-developers/
 * https://github.com/robsonagapito/unit-testing-java
*/

/*
 * These imports are from the java standard library, used for file handling, date checking, lists, and reading TXT 
 */

import java.io.File;
import java.io.FileWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * DriverRepository manages all Driver records.
 * - adds drivers
 * - retrieves drivers
 * - updates drivers
 * - counts drivers
 * - validating Driver rules D1 to d5
 * - saves/loads drivers using a txt file
 */

/*
 * This class manages multiple Driver objects and applies rules defined
 # also saves/loads Driver records using a TXT file.
 */
public class DriverRepository {
    private String filePath;
    private List<Driver> drivers;

    /**
     * When the repo is created it loads any present driver data from the txt file into the drivers list
     */
    public DriverRepository(String filePath) {
        this.filePath = filePath;
        this.drivers = loadDrivers();
    }

    /**
     * Adds a new driver and it can only be added if: all driver details are valid and the driverID is not already used
     */

    /*
     * It returns true if the driver is accepted and saved
     * It returns false if the driver is invalid or if the driverID already exists
     */

    public boolean addDriver(Driver driver) {
        /*
         * First check the full Driver object using isValidDriver()
         * This checks rules: valid ID, address, birthdate, license type, etc.
         */
        if (!isValidDriver(driver)) {
            return false;
        }
        /*
         * requires driverID to be unique.
         * retrieveDriver() searches for an existing driver with same ID
         * If it's not null that means the ID already exists, so the new driver is rejected
         */
        if (retrieveDriver(driver.getDriverID()) != null) {
            return false;
        }
        /*
         * If validation passes and the ID is unique,  driver added to the list.
         */
        drivers.add(driver);
        saveDrivers();
        return true;
    }

    /**
     * Retrieves a driver by their driverID (returns the Driver object if it can be found- otherwise it returns null)
     */


    /*
     * retrieveDriver() searches through the drivers list for a matching driverID
     * If a match -> the matching Driver object is returned
     * If no match -> null is returned.
     */
    public Driver retrieveDriver(String driverID) {
        for (Driver driver : drivers) {
            if (driver.getDriverID().equals(driverID)) {
                return driver;
            }
        }

        return null;
    }

    /**
     * Updates a present driver.
     * D4: If a driver has more than 10 years of experience, their licenseType cant be changed.
     * D5: driverID and name cant be changed during update
     */


    /*
     * existingDriverID is used to find the current saved driver
     * updatedDriver contains the new details; returns true if the update is accepted OR
     * returns false if the update DOES NOT follow validation rule (any of them)
     */
    public boolean updateDriver(String existingDriverID, Driver updatedDriver) {
        /*
         * First find the driver currently stored in the repository
         */
        Driver existingDriver = retrieveDriver(existingDriverID);
        /*
         * If no driver exists with that ID there is nothing to update
         */
        if (existingDriver == null) {
            return false;
        }

        if (!isValidDriver(updatedDriver)) {
            return false;
        }

        // D5
        if (!existingDriver.getDriverID().equals(updatedDriver.getDriverID())) {
            return false;
        }

        // D5
        if (!existingDriver.getName().equals(updatedDriver.getName())) {
            return false;
        }
        
        /* 
         * D4 says if a driver has more than 10 years of experience,their licenseType cant be changed.
         * The check uses the existing driver's experienceYears because the restriction
         * is based on the driver's current stored exp
         */
        // D4
        if (existingDriver.getExperienceYears() > 10 &&
                !existingDriver.getLicenseType().equals(updatedDriver.getLicenseType())) {
            return false;
        }
        /* If all checks pass- updating only the fields that are allowed to change.
         * driverID and name are not updated because D5 requires them immutable 
         */
    
        existingDriver.setExperienceYears(updatedDriver.getExperienceYears());
        existingDriver.setLicenseType(updatedDriver.getLicenseType());
        existingDriver.setAddress(updatedDriver.getAddress());
        existingDriver.setBirthdate(updatedDriver.getBirthdate());

        saveDrivers();
        return true;
    }

    /**
     * Driver count
     */
    public int countDrivers() {
        return drivers.size();
    }

    /**
     * Checks whether a Driver object is valid: D1 driverID, D2 address, D3 birthdate
     * checks: name, experienceYears, and licenseType
     *  * It calls the smaller validation methods:
     * - isValidDriverID()
     * - isValidAddress()
     * - isValidBirthdate()
     */
    public boolean isValidDriver(Driver driver) {
        if (driver == null) {
            return false;
        }
        /*
         * Name can't be null or empty.
         * trim() removes spaces from the start/end before checking if it's empty
         */
        if (driver.getName() == null || driver.getName().trim().isEmpty()) {
            return false;
        }

        /*
         * Experience years should not be negative.
         */
        if (driver.getExperienceYears() < 0) {
            return false;
        }

        /*
         * Check that the license type is one of the accepted assignment values.
         */
        if (!isValidLicenseType(driver.getLicenseType())) {
            return false;
        }

        return isValidDriverID(driver.getDriverID())
                && isValidAddress(driver.getAddress())
                && isValidBirthdate(driver.getBirthdate());
    }

    /**
     * D1: checks whether driverID follows the proper format:
     * - exactly 10 chars
     * - first two chars are digits from 2 to 9
     * - characters 3 to 8 have at least two special chars
     * - last two chars should beuppercase letters
     */
    public boolean isValidDriverID(String driverID) {
        /*
         * Reject null IDs and IDs that aren't exactly 10 characters long.
         */      
        if (driverID == null || driverID.length() != 10) {
            return false;
        }

        /*
         * Get the first and second characters of the ID.
         * charAt(0) is the first character.
         */
        char firstChar = driverID.charAt(0);
        char secondChar = driverID.charAt(1);
        /*
         * Second character must also be between '2' and '9'.
         */
        if (firstChar < '2' || firstChar > '9') {
            return false;
        }
        /*
         * string(2, 8) gets characters from index 2 up to index 7
         */
        if (secondChar < '2' || secondChar > '9') {
            return false;
        }

        String middlePart = driverID.substring(2, 8);
        int specialCharacterCount = 0;
        /*
         * Loop through each character in the middle part;
         * If a character isn't letter and not a digit- it as a special character
         */
        for (int i = 0; i < middlePart.length(); i++) {
            char currentChar = middlePart.charAt(i);

            if (!Character.isLetterOrDigit(currentChar)) {
                specialCharacterCount++;
            }
        }
        /*
         * D1 requires at least two special characters in the middle section.
         */
        if (specialCharacterCount < 2) {
            return false;
        }

        String lastTwoChars = driverID.substring(8, 10);
        /*
         * [A-Z]{2} means exactly two uppercase letters from A to Z.
         */
        return lastTwoChars.matches("[A-Z]{2}");
    }

    /**
     * D2: checks address format: Street Number|Street Name|City|State|Country
     */
    public boolean isValidAddress(String address) {

        /*
         * Null address is invalid.
         */

        if (address == null) {
            return false;
        }

        String[] addressParts = address.split("\\|", -1);

        if (addressParts.length != 5) {
            return false;
        }

        /*
         * Each address part should contain text
         * This rejects things like this, for example:
         * 12|Winterfell Road||North|Westeros
         */
        for (String part : addressParts) {
            if (part.trim().isEmpty()) {
                return false;
            }
        }

        // Street number is number only.
        /*
         * The first part is the street number.
         * \\d+ means one or more digits.
         */
        return addressParts[0].matches("\\d+");
    }

    /**
     * D3: Checks whether the birthdate follows DD-MM-YYYY (no impossible dates allowed)
     */

    /*
     * isValidBirthdate() implements D3.
     *
     * It checks:
     * - the birthdate is not null
     * - the format follows DD-MM-YYYY
     * - the date is a real calendar date
     */
    public boolean isValidBirthdate(String birthdate) {
        /*
         * Null birthdate is invalid.
         */
        if (birthdate == null) {
            return false;
        }

        /*
         * DateTimeFormatter defines the needed format;
         * dd = day, MM = month, uuuu = year.
         *
         * ResolverStyle.STRICT makes program reject impossible dates
         */
        DateTimeFormatter formatter = DateTimeFormatter
                .ofPattern("dd-MM-uuuu")
                .withResolverStyle(ResolverStyle.STRICT);

        /*
         * Try parsing the birthdate.
         * If parsing works, the birthdate is valid- otherwise falseis returned
         */
        try {
            LocalDate.parse(birthdate, formatter);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Checks that the license type is allowed val
     */
    private boolean isValidLicenseType(String licenseType) {
        /*
         * Null license type is invalid.
         */
        if (licenseType == null) {
            return false;
        }
        /*
         * Return true only if licenseType matches one of these allowed values.
         */
        return licenseType.equals("Light")
                || licenseType.equals("Medium")
                || licenseType.equals("Heavy")
                || licenseType.equals("PublicTransport");
    }

    /**
     * Gets driver records from the TXT file.
     *
     * Each line in the TXT file stores one driver in this order:
     * driverID;name;experienceYears;licenseType;address;birthdate
     */
    private List<Driver> loadDrivers() {
        /*
         * Start with an empty list. If the file is missing or empty, this list is returned.
         */
        List<Driver> loadedDrivers = new ArrayList<>();

        try {
            File file = new File(filePath);
            /*
             * If the file does not exist or has no data- return the empty list.
             */
            if (!file.exists() || file.length() == 0) {
                return loadedDrivers;
            }
            /*
             * Scanner reads the TXT file line by line.
             */
            Scanner scanner = new Scanner(file);
            /*
             * Continue reading while the file still has another line
             */
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                /*
                 * Skip blank lines so we dont get errors 
                 */
                if (line.trim().isEmpty()) {
                    continue;
                }
                /*
                 * Each line is split into fields using semicolon
                 */
                String[] parts = line.split(";", -1);
                /*
                 * Only create a Driver if the line has exactly 6 fields.
                 */
                if (parts.length == 6) {
                    Driver driver = new Driver(
                            parts[0],
                            parts[1],
                            Integer.parseInt(parts[2]),
                            parts[3],
                            parts[4],
                            parts[5]
                    );
                    /*
                     * adding the created Driver object to the loaded list
                     */
                    loadedDrivers.add(driver);
                }
            }
            /*
             * closing the scanner after reading the file
             */
            scanner.close();

        } catch (Exception e) {
            /*
             * If anything errors/mistakes while loading- it return an empty list
             */          
            return new ArrayList<>();
        }

        return loadedDrivers;
    }

    /**
     * Saves the current driver list to the TXT file.
     *
     * Each driver is saved on one line using this format: driverID;name;experienceYears;licenseType;address;birthdate
     */
    private void saveDrivers() {
        try {
            /*
             * creating a File object for the file path
             */
            File file = new File(filePath);
            /*
             * If the parent folder doesn't exist, create it.
             * test data folder needs to be created
             */
            if (file.getParentFile() != null) {
                file.getParentFile().mkdirs();
            }
            /*
             * fileWriter writes text into the TXT file- try-with-resources automatically closes the writer when its done
             */
            try (FileWriter writer = new FileWriter(file)) {
                for (Driver driver : drivers) {
                    writer.write(driver.getDriverID() + ";"
                            + driver.getName() + ";"
                            + driver.getExperienceYears() + ";"
                            + driver.getLicenseType() + ";"
                            + driver.getAddress() + ";"
                            + driver.getBirthdate() + "\n");
                }
            }

        } catch (Exception e) {
            /*
             * Print a simple message if saving cant occur
             * errors accounted for 
             */
            System.out.println("Driver data could not be saved.");
        }
    }
}