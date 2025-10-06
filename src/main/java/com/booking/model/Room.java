package com.booking.model;

public class Room {
    private String id;
    private String name;
    private int capacity;
    private boolean hasProjector;
    private boolean hasWhiteboard;

    public Room(String id, String name, int capacity, boolean hasProjector, boolean hasWhiteboard) {
        this.id = id;
        this.name = name;
        this.capacity = capacity;
        this.hasProjector = hasProjector;
        this.hasWhiteboard = hasWhiteboard;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getCapacity() {
        return capacity;
    }

    public boolean hasProjector() {
        return hasProjector;
    }

    public boolean hasWhiteboard() {
        return hasWhiteboard;
    }

    @Override
    public String toString() {
        return String.format("Room{id='%s', name='%s', capacity=%d, projector=%s, whiteboard=%s}",
                id, name, capacity, hasProjector, hasWhiteboard);
    }
}
