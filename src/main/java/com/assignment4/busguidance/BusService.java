package com.assignment4.busguidance;

// This class provides validation methods for Bus and Driver objects based on specified rules
public class BusValidator {
  // B1 - Bus ID Rules
  // Validates that the bus ID is an 8-digit number
  public static boolean isValidBusID(String busID) {
    if (busID == null) {
      return false;
    }
    return busID.matches("\d{8}");
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

  // Used before adding a bus to the repository.
  // Validates the bus object based on its ID and other properties
  public static boolean validateBus(Bus bus) {
    if (bus == null) {
      return false;
    }
    return isValidBusID(bus.getBusID());
  }
  }
