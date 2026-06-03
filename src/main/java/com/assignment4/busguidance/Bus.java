package com.assignment4.busguidance;

// Class; used to represent Bus object in system.
// Stores information including: bus ID, capacity, fuel level, and fuel type.
public class Bus {
    private String busID;
    private int capacity;
    private double fuelLevel;
    private String fuelType; // Diesel, Hybrid, Electricity

    // Constructor; used to creates a new Bus object, and initialises all attributes.
    // Example: new Bus("BUS001", 50, 85.5, "Diesel");
    public Bus(String busID, int capacity, double fuelLevel, String fuelType) {
        this.busID = busID;
        this.capacity = capacity;
        this.fuelLevel = fuelLevel;
        this.fuelType = fuelType;
    }

    // returns bus ID
    public String getBusID() {
        return busID;
    }

    // returns capacity
    public int getCapacity() {
        return capacity;
    }

    // updates capacity
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    // returns fuel level
    public double getFuelLevel() {
        return fuelLevel;
    }

    // updates fuel level
    public void setFuelLevel(double fuelLevel) {
        this.fuelLevel = fuelLevel;
    }

    // return fuel type
    public String getFuelType() {
        return fuelType;
    }

    // updates fuel type
    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }
}
