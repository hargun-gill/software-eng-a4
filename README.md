# Intelligent Bus Driver Guidance System
## Folder Structure outlined below 

### src/main/java
This folder contains the main Java code for the system

 `Driver.java` stores the details for one driver such as driver ID, name, experience years, licence type, address, and birthdate. 
 
 `DriverRepository.java` manages driver records. It can add, retrieve, update, and count drivers. checks the Driver rules D1 to D5
 
 `Bus.java` stores the details for one bus such as bus ID, capacity, fuel level, and fuel type.
 
 `BusRepository.java` manages bus records. It can add, retrieve, update, and count buses. checks the Bus rules B1 to B5

### src/main/resources/data
This folder contains the normal data files used by the program.

 `driver-data.txt` stores driver records
 
 `bus-data.txt` stores bus records

Each driver is stored on one line using this format: driverID;name;experienceYears;licenseType;address;birthdate

Each bus is stored on one line using this format: busID,capacity,fuelLevel,fuelType

### src/test/java
This folder has the JUnit test files.

 `DriverTest.java` tests the Driver rules and DriverRepository methods
 
 `BusTest.java` tests the Bus rules and BusRepository methods
 
 `DriverIntegrationTest.java` is for driver integration tests
 
 `BusIntegrationTest.java` is for bus integration tests

### src/test/resources/test-data
This folder has test-only data files.

 `driver-test-data.txt` is used by Driver unit tests
 
 `driver-integration-data.txt` is used by Driver integration tests
 
 `bus-integration-data.txt` is used by Bus integration tests

These files are separate from the real data files so testing doesnt change the main program data.

## Running Tests

to run all tests locally DO THIS:
```bash
mvn test

YAY
