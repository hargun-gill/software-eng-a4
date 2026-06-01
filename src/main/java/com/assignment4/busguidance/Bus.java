package com.assignment4.busguidance;

// Class; used to represent Bus object in system.
// Stores information including: bus ID, bus name, assigned route, and passenger capacity.
public class Bus {
    private int id;
    private String name;
    private String route;
    private int capacity;

    // Constructor; used to creates a new Bus object, and initialises all attributes.
    public Bus(int id, String name, String route, int capacity) {
        this.id = id;
        this.name = name;
        this.route = route;
        this.capacity = capacity;
    }

    //  @return bus ID
    public int getId() {
        return id;
    }

    // @return bus name
    public String getName() {
        return name;
    }

    // @return route
    public String getRoute() {
        return route;
    }

    // @return capacity
    public int getCapacity() {
        return capacity;
    }

    // Converts the Bus object into a text format (write to file)
    // @return formatted String representation
    @Override
    public String toString() {
        return id + "," + name + "," + route + "," + capacity;
    }

    // Converts a line of text back into a Bus object (read from file)
    // @return Bus object
    public static Bus fromString(String data) {
        // Split the line into separate values
        String[] parts = data.split(",");
        // Create and return a Bus object
        return new Bus(
                Integer.parseInt(parts[0]), // Bus ID
                parts[1],                   // Bus name
                parts[2],                   // Route
                Integer.parseInt(parts[3])  // Capacity
        );
    }
}
