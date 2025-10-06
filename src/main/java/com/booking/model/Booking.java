package com.booking.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Booking {
    private String id;
    private Room room;
    private User user;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String purpose;
    private BookingStatus status;

    public enum BookingStatus {
        PENDING,
        CONFIRMED,
        CANCELLED
    }

    public Booking(String id, Room room, User user, LocalDateTime startTime,
                   LocalDateTime endTime, String purpose) {
        this.id = id;
        this.room = room;
        this.user = user;
        this.startTime = startTime;
        this.endTime = endTime;
        this.purpose = purpose;
        this.status = BookingStatus.PENDING;
    }

    public String getId() {
        return id;
    }

    public Room getRoom() {
        return room;
    }

    public User getUser() {
        return user;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public String getPurpose() {
        return purpose;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return String.format("Booking{id='%s', room='%s', user='%s', time='%s - %s', status=%s}",
                id, room.getName(), user.getName(),
                startTime.format(formatter), endTime.format(formatter), status);
    }
}