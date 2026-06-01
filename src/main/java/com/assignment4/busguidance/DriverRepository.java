package com.assignment4.busguidance;

/**
 * Reference used: https://itsourcecode.com/free-projects/java-projects/transport-management-system-project-in-java-with-source-code/
 * https://www.diffblue.com/resources/java-unit-testing-a-complete-guide-for-developers/
 * https://github.com/robsonagapito/unit-testing-java
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
    public boolean addDriver(Driver driver) {
        if (!isValidDriver(driver)) {
            return false;
        }

        if (retrieveDriver(driver.getDriverID()) != null) {
            return false;
        }

        drivers.add(driver);
        saveDrivers();
        return true;
    }

    /**
     * Retrieves a driver by their driverID (returns the Driver object if it can be found- otherwise it returns null)
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
    public boolean updateDriver(String existingDriverID, Driver updatedDriver) {
        Driver existingDriver = retrieveDriver(existingDriverID);

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

        // D4
        if (existingDriver.getExperienceYears() > 10 &&
                !existingDriver.getLicenseType().equals(updatedDriver.getLicenseType())) {
            return false;
        }

        // allowed fields are updated ONLY
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
     */
    public boolean isValidDriver(Driver driver) {
        if (driver == null) {
            return false;
        }

        if (driver.getName() == null || driver.getName().trim().isEmpty()) {
            return false;
        }

        if (driver.getExperienceYears() < 0) {
            return false;
        }

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
        if (driverID == null || driverID.length() != 10) {
            return false;
        }

        char firstChar = driverID.charAt(0);
        char secondChar = driverID.charAt(1);

        if (firstChar < '2' || firstChar > '9') {
            return false;
        }

        if (secondChar < '2' || secondChar > '9') {
            return false;
        }

        String middlePart = driverID.substring(2, 8);
        int specialCharacterCount = 0;

        for (int i = 0; i < middlePart.length(); i++) {
            char currentChar = middlePart.charAt(i);

            if (!Character.isLetterOrDigit(currentChar)) {
                specialCharacterCount++;
            }
        }

        if (specialCharacterCount < 2) {
            return false;
        }

        String lastTwoChars = driverID.substring(8, 10);

        return lastTwoChars.matches("[A-Z]{2}");
    }

    /**
     * D2: checks address format: Street Number|Street Name|City|State|Country
     */
    public boolean isValidAddress(String address) {
        if (address == null) {
            return false;
        }

        String[] addressParts = address.split("\\|", -1);

        if (addressParts.length != 5) {
            return false;
        }

        for (String part : addressParts) {
            if (part.trim().isEmpty()) {
                return false;
            }
        }

        // Street number is number only.
        return addressParts[0].matches("\\d+");
    }

    /**
     * D3: Checks whether the birthdate follows DD-MM-YYYY (no impossible dates allowed)
     */
    public boolean isValidBirthdate(String birthdate) {
        if (birthdate == null) {
            return false;
        }

        DateTimeFormatter formatter = DateTimeFormatter
                .ofPattern("dd-MM-uuuu")
                .withResolverStyle(ResolverStyle.STRICT);

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
        if (licenseType == null) {
            return false;
        }

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
        List<Driver> loadedDrivers = new ArrayList<>();

        try {
            File file = new File(filePath);

            if (!file.exists() || file.length() == 0) {
                return loadedDrivers;
            }

            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();

                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] parts = line.split(";", -1);

                if (parts.length == 6) {
                    Driver driver = new Driver(
                            parts[0],
                            parts[1],
                            Integer.parseInt(parts[2]),
                            parts[3],
                            parts[4],
                            parts[5]
                    );

                    loadedDrivers.add(driver);
                }
            }

            scanner.close();

        } catch (Exception e) {
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
            File file = new File(filePath);

            if (file.getParentFile() != null) {
                file.getParentFile().mkdirs();
            }

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
            System.out.println("Driver data could not be saved.");
        }
    }
}