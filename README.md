# Intelligent Bus Driver Guidance System
## Folder Structure

### src/main/java
This folder contains the main Java code for the system

- `Driver.java` stores the details for one driver such as driver ID, name, experience years, licence type, address, and birthdate.
- `DriverRepository.java` manages driver records. It can add, retrieve, update, and count drivers. checks the Driver rules D1 to D5
- `Bus.java` is for the bus details
- `BusRepository.java` is for managing bus records

### src/main/resources/data
This folder contains the normal data files used by the program.

- `driver-data.txt` stores driver records
- `bus-data.txt` stores bus records

Each driver is stored on one line using this format: driverID;name;experienceYears;licenseType;address;birthdate

### src/test/java
This folder has the JUnit test files.
- `DriverTest.java` tests the Driver rules and DriverRepository methods
- `BusTest.java` tests the Bus rules and BusRepository methods
- `DriverIntegrationTest.java` is for driver integration tests
- `BusIntegrationTest.java` is for bus integration tests

### src/test/resources/test-data
This folder has test-only data files.

- `driver-test-data.txt` is used by Driver unit tests
- `bus-test-data.txt` is used by Bus tests

These files are separate from the real data files so testing doesnt changthe main program data.

## Running Tests

to run all tests locally DO THIS:
```bash
mvn test

YAY
