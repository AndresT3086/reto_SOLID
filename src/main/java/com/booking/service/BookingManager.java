package com.booking.service;

import com.booking.model.Booking;
import com.booking.model.Booking.BookingStatus;
import com.booking.model.Room;
import com.booking.model.User;
import com.booking.model.User.UserType;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BookingManager {
    private List<Booking> bookings = new ArrayList<>();
    private List<Room> rooms = new ArrayList<>();

    public BookingManager() {
        initializeRooms();
    }

    private void initializeRooms() {
        rooms.add(new Room("R001", "Sala Ejecutiva", 10, true, true));
        rooms.add(new Room("R002", "Sala de Juntas", 6, true, false));
        rooms.add(new Room("R003", "Sala Pequeña", 4, false, true));
    }

    public Booking createBooking(User user, String roomId, LocalDateTime startTime,
                                 LocalDateTime endTime, String purpose) {
        System.out.println("=== INICIANDO PROCESO DE RESERVA ===");

        System.out.println("Validando datos de entrada...");
        if (user == null || roomId == null || startTime == null || endTime == null) {
            System.err.println("ERROR: Datos de entrada inválidos");
            return null;
        }

        if (endTime.isBefore(startTime)) {
            System.err.println("ERROR: La hora de fin debe ser posterior a la hora de inicio");
            return null;
        }

        System.out.println("Buscando sala...");
        Room room = null;
        for (Room r : rooms) {
            if (r.getId().equals(roomId)) {
                room = r;
                break;
            }
        }

        if (room == null) {
            System.err.println("ERROR: Sala no encontrada");
            return null;
        }

        System.out.println("Verificando disponibilidad...");
        for (Booking existingBooking : bookings) {
            if (existingBooking.getRoom().getId().equals(roomId) &&
                    existingBooking.getStatus() != BookingStatus.CANCELLED) {

                boolean overlaps = !(endTime.isBefore(existingBooking.getStartTime()) ||
                        startTime.isAfter(existingBooking.getEndTime()));

                if (overlaps) {
                    System.err.println("ERROR: La sala ya está reservada en ese horario");
                    return null;
                }
            }
        }


        System.out.println("Calculando precio...");
        double pricePerHour = 0;
        switch (user.getType()) {
            case REGULAR:
                pricePerHour = 50.0;
                break;
            case MANAGER:
                pricePerHour = 30.0; // 40% descuento
                break;
            case ADMIN:
                pricePerHour = 0.0; // Gratis
                break;
        }

        long hours = java.time.Duration.between(startTime, endTime).toHours();
        double totalPrice = pricePerHour * hours;
        System.out.println("Precio calculado: $" + totalPrice);

        System.out.println("Creando reserva...");
        String bookingId = "B" + UUID.randomUUID().toString().substring(0, 8);
        Booking booking = new Booking(bookingId, room, user, startTime, endTime, purpose);
        booking.setStatus(BookingStatus.CONFIRMED);
        bookings.add(booking);

        System.out.println("Enviando notificación...");
        sendEmailNotification(user.getEmail(),
                "Reserva Confirmada",
                formatBookingConfirmation(booking, totalPrice));

        logActivity("BOOKING_CREATED", user.getId(), bookingId);

        System.out.println("=== RESERVA COMPLETADA ===\n");
        return booking;
    }

    private void sendEmailNotification(String to, String subject, String body) {
        System.out.println("📧 Enviando email a: " + to);
        System.out.println("Asunto: " + subject);
        System.out.println("Cuerpo:\n" + body);
        System.out.println();
    }

    private String formatBookingConfirmation(Booking booking, double price) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return String.format("""
            Estimado/a %s,

            Su reserva ha sido confirmada:
            - Sala: %s
            - Fecha/Hora: %s - %s
            - Propósito: %s
            - Precio: $%.2f

            Gracias por usar nuestro sistema.
            """,
                booking.getUser().getName(),
                booking.getRoom().getName(),
                booking.getStartTime().format(formatter),
                booking.getEndTime().format(formatter),
                booking.getPurpose(),
                price
        );
    }

    private void logActivity(String action, String userId, String resourceId) {
        System.out.println(String.format("[LOG] %s - Action: %s, User: %s, Resource: %s",
                LocalDateTime.now(), action, userId, resourceId));
    }

    public boolean cancelBooking(String bookingId, User user) {
        System.out.println("=== CANCELANDO RESERVA ===");

        for (Booking booking : bookings) {
            if (booking.getId().equals(bookingId)) {

                // Validación de permisos
                if (!booking.getUser().getId().equals(user.getId()) &&
                        user.getType() != UserType.ADMIN) {
                    System.err.println("ERROR: No tiene permisos para cancelar esta reserva");
                    return false;
                }

                booking.setStatus(BookingStatus.CANCELLED);

                // Notificación
                sendEmailNotification(booking.getUser().getEmail(),
                        "Reserva Cancelada",
                        "Su reserva " + bookingId + " ha sido cancelada.");

                // Logging
                logActivity("BOOKING_CANCELLED", user.getId(), bookingId);

                System.out.println("Reserva cancelada exitosamente\n");
                return true;
            }
        }

        System.err.println("ERROR: Reserva no encontrada\n");
        return false;
    }

    public String generateReport() {
        System.out.println("=== GENERANDO REPORTE ===");

        StringBuilder report = new StringBuilder();
        report.append("\n╔════════════════════════════════════════════════╗\n");
        report.append("║         REPORTE DE RESERVAS DE SALAS          ║\n");
        report.append("╚════════════════════════════════════════════════╝\n\n");

        int confirmed = 0;
        int cancelled = 0;
        int pending = 0;

        for (Booking booking : bookings) {
            switch (booking.getStatus()) {
                case CONFIRMED: confirmed++; break;
                case CANCELLED: cancelled++; break;
                case PENDING: pending++; break;
            }
        }

        report.append(String.format("Total de Reservas: %d\n", bookings.size()));
        report.append(String.format("  - Confirmadas: %d\n", confirmed));
        report.append(String.format("  - Canceladas: %d\n", cancelled));
        report.append(String.format("  - Pendientes: %d\n\n", pending));

        report.append("Detalle de Reservas:\n");
        report.append("─────────────────────────────────────────────────\n");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        for (Booking booking : bookings) {
            report.append(String.format("ID: %s | Sala: %s | Usuario: %s\n",
                    booking.getId(), booking.getRoom().getName(), booking.getUser().getName()));
            report.append(String.format("Fecha: %s - %s | Estado: %s\n",
                    booking.getStartTime().format(formatter),
                    booking.getEndTime().format(formatter),
                    booking.getStatus()));
            report.append("─────────────────────────────────────────────────\n");
        }

        System.out.println(report.toString());
        return report.toString();
    }

    public List<Booking> getBookings() {
        return new ArrayList<>(bookings);
    }

    public List<Room> getRooms() {
        return new ArrayList<>(rooms);
    }
}