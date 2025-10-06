package com.booking;

import com.booking.model.Booking;
import com.booking.model.Room;
import com.booking.model.User;
import com.booking.model.User.UserType;
import com.booking.service.BookingManager;

import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {
        System.out.println("╔═══════════════════════════════════════════════════════╗");
        System.out.println("║  SISTEMA DE RESERVAS DE SALAS - DEMO ANTI-SOLID     ║");
        System.out.println("║  ⚠️  Este código viola intencionalmente principios    ║");
        System.out.println("║      SOLID para fines educativos                      ║");
        System.out.println("╚═══════════════════════════════════════════════════════╝\n");

        BookingManager bookingManager = new BookingManager();

        // Crear usuarios de prueba
        User regularUser = new User("U001", "Juan Pérez", "juan@empresa.com",
                "Ventas", UserType.REGULAR);
        User managerUser = new User("U002", "María García", "maria@empresa.com",
                "Gerencia", UserType.MANAGER);
        User adminUser = new User("U003", "Carlos López", "carlos@empresa.com",
                "IT", UserType.ADMIN);

        // Mostrar salas disponibles
        System.out.println("📋 SALAS DISPONIBLES:");
        for (Room room : bookingManager.getRooms()) {
            System.out.println("  " + room);
        }
        System.out.println();

        // Crear reservas
        System.out.println("🔹 ESCENARIO 1: Usuario Regular hace una reserva");
        Booking booking1 = bookingManager.createBooking(
                regularUser,
                "R001",
                LocalDateTime.now().plusDays(1).withHour(10).withMinute(0),
                LocalDateTime.now().plusDays(1).withHour(12).withMinute(0),
                "Reunión de ventas Q1"
        );

        System.out.println("🔹 ESCENARIO 2: Manager hace una reserva (con descuento)");
        Booking booking2 = bookingManager.createBooking(
                managerUser,
                "R002",
                LocalDateTime.now().plusDays(2).withHour(14).withMinute(0),
                LocalDateTime.now().plusDays(2).withHour(16).withMinute(0),
                "Reunión estratégica"
        );

        System.out.println("🔹 ESCENARIO 3: Admin hace una reserva (gratis)");
        Booking booking3 = bookingManager.createBooking(
                adminUser,
                "R003",
                LocalDateTime.now().plusDays(3).withHour(9).withMinute(0),
                LocalDateTime.now().plusDays(3).withHour(11).withMinute(0),
                "Mantenimiento de servidores"
        );

        System.out.println("🔹 ESCENARIO 4: Intento de reserva en horario ocupado");
        bookingManager.createBooking(
                regularUser,
                "R001",
                LocalDateTime.now().plusDays(1).withHour(11).withMinute(0),
                LocalDateTime.now().plusDays(1).withHour(13).withMinute(0),
                "Otra reunión"
        );

        // Cancelar una reserva
        System.out.println("🔹 ESCENARIO 5: Usuario cancela su propia reserva");
        if (booking1 != null) {
            bookingManager.cancelBooking(booking1.getId(), regularUser);
        }

        // Generar reporte
        bookingManager.generateReport();

        System.out.println("\n╔═══════════════════════════════════════════════════════╗");
        System.out.println("║  🎯 DESAFÍO                                          ║");
        System.out.println("╠═══════════════════════════════════════════════════════╣");
        System.out.println("║  1. Identifica las violaciones de SRP en             ║");
        System.out.println("║     BookingManager                                    ║");
        System.out.println("║  2. Identifica la violación de OCP en el cálculo     ║");
        System.out.println("║     de precios                                        ║");
        System.out.println("║  3. Refactoriza el código aplicando SOLID            ║");
        System.out.println("║     correctamente                                     ║");
        System.out.println("╚═══════════════════════════════════════════════════════╝");
    }
}