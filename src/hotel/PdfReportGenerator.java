package hotel;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;
import javax.swing.*;

public class PdfReportGenerator {
    
    public static void exportarHistorialReservas() {
    Document document = new Document();
    try {
        String ruta = System.getProperty("user.home") + File.separator + "Desktop" + File.separator + "HistorialReservas.pdf";
        File file = new File(ruta);
        file.getParentFile().mkdirs();
        
        PdfWriter.getInstance(document, new FileOutputStream(ruta));
        document.open();
        
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLACK);
        Paragraph title = new Paragraph("Historial de Reservas", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
        document.add(new Paragraph(" "));
        
        PdfPTable table = new PdfPTable(8);
        table.setWidthPercentage(100);
        
        String[] headers = {"ID Reserva", "Clientes", "Habitación", "Disponibilidad", "Fecha Entrada", "Fecha Salida", "Monto", "Estado"};
        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(header, FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(cell);
        }
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            
            String query = "SELECT r.id_reserva, " +
                           "GROUP_CONCAT(DISTINCT CONCAT(c.nombre, ' ', c.apellido) ORDER BY rc.es_titular DESC SEPARATOR ', ') AS clientes, " +
                           "h.nombre AS nombre_habitacion, r.id_habitacion, h.disponibilidad, r.fecha_entrada, r.fecha_salida, r.monto " +
                           "FROM reservas r " +
                           "LEFT JOIN reservas_clientes rc ON r.id_reserva = rc.id_reserva " +
                           "LEFT JOIN clientes c ON rc.id_cliente = c.id_cliente " +
                           "LEFT JOIN habitaciones h ON r.id_habitacion = h.id_habitacion " +
                           "GROUP BY r.id_reserva, h.nombre, r.id_habitacion, h.disponibilidad, r.fecha_entrada, r.fecha_salida, r.monto";
            
            try (ResultSet rs = stmt.executeQuery(query)) {
                while (rs.next()) {
                    table.addCell(rs.getString("id_reserva"));
                    table.addCell(rs.getString("clientes"));
                    table.addCell(rs.getString("nombre_habitacion") + " (ID: " + rs.getString("id_habitacion") + ")");
                    table.addCell(rs.getBoolean("disponibilidad") ? "Disponible" : "Ocupada");
                    table.addCell(rs.getString("fecha_entrada"));
                    table.addCell(rs.getString("fecha_salida"));
                    table.addCell("$" + rs.getString("monto"));
                    
                    // Determinar el estado de la reserva
                    Date fechaSalida = rs.getDate("fecha_salida");
                    Date fechaActual = new Date(System.currentTimeMillis());
                    String estado = fechaSalida.before(fechaActual) ? "Finalizada" : "Activa";
                    table.addCell(estado);
                }
            }
        }
        
        document.add(table);
        document.close();
        System.out.println("PDF del historial de reservas creado exitosamente en: " + ruta);
        JOptionPane.showMessageDialog(null, "PDF exportado exitosamente al escritorio.");
        
    } catch (DocumentException | IOException | SQLException e) {
        e.printStackTrace();
    }
}
    
    public static void exportarReservasServicios() {
    Document document = new Document();
    try {
        String ruta = System.getProperty("user.home") + File.separator + "Desktop" + File.separator + "ReservasServicios.pdf";
        File file = new File(ruta);
        file.getParentFile().mkdirs();
        
        PdfWriter.getInstance(document, new FileOutputStream(ruta));
        document.open();
        
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLACK);
        Paragraph title = new Paragraph("Reservas y Servicios", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
        document.add(new Paragraph(" "));
        
        PdfPTable table = new PdfPTable(7);
        table.setWidthPercentage(100);
        
        String[] headers = {"ID Reserva", "ID", "Habitación", "Fecha Entrada", "Fecha Salida", "Cliente", "Servicio"};
        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(header, FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(cell);
        }
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            
            String query = "SELECT rs.id_reserva_servicio, r.id_reserva, h.nombre AS habitacion, r.fecha_entrada, r.fecha_salida, " +
                           "CONCAT(c.nombre, ' ', c.apellido) AS cliente, s.nombre AS servicio " +
                           "FROM reservas_servicios rs " +
                           "JOIN reservas r ON rs.id_reserva = r.id_reserva " +
                           "JOIN habitaciones h ON r.id_habitacion = h.id_habitacion " +
                           "JOIN reservas_clientes rc ON r.id_reserva = rc.id_reserva " +
                           "JOIN clientes c ON rc.id_cliente = c.id_cliente " +
                           "JOIN servicios s ON rs.id_servicio = s.id_servicio " +
                           "WHERE rc.es_titular = 1";
            
            try (ResultSet rs = stmt.executeQuery(query)) {
                while (rs.next()) {
                    table.addCell(rs.getString("id_reserva"));
                    table.addCell(rs.getString("id_reserva_servicio"));
                    table.addCell(rs.getString("habitacion"));
                    table.addCell(rs.getString("fecha_entrada"));
                    table.addCell(rs.getString("fecha_salida"));
                    table.addCell(rs.getString("cliente"));
                    table.addCell(rs.getString("servicio"));
                }
            }
        }
        
        document.add(table);
        document.close();
        System.out.println("PDF de Reservas y Servicios creado exitosamente en: " + ruta);
        JOptionPane.showMessageDialog(null, "PDF exportado exitosamente al escritorio.");
        
    } catch (DocumentException | IOException | SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Error al exportar el PDF: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}
    
    public static void exportarHabitacionesDisponibles() {
    Document document = new Document();
    try {
        String ruta = System.getProperty("user.home") + File.separator + "Desktop" + File.separator + "HabitacionesDisponibles.pdf";
        File file = new File(ruta);
        file.getParentFile().mkdirs();
        
        PdfWriter.getInstance(document, new FileOutputStream(ruta));
        document.open();
        
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLACK);
        Paragraph title = new Paragraph("Habitaciones Disponibles", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
        document.add(new Paragraph(" "));
        
        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        
        // Definir los encabezados personalizados
        String[] headers = {"ID de la habitación", "Nombre", "Tipo", "Precio"};
        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(header, FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(cell);
        }
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            
            String query = "SELECT id_habitacion, nombre, tipo, precio " +
                           "FROM habitaciones " +
                           "WHERE disponibilidad = true";
            
            try (ResultSet rs = stmt.executeQuery(query)) {
                while (rs.next()) {
                    table.addCell(rs.getString("id_habitacion"));
                    table.addCell(rs.getString("nombre"));
                    table.addCell(rs.getString("tipo"));
                    table.addCell(rs.getString("precio") + ("$"));
                }
            }
        }
        
        document.add(table);
        document.close();
        System.out.println("PDF de habitaciones disponibles creado exitosamente en: " + ruta);
        JOptionPane.showMessageDialog(null, "PDF exportado exitosamente al escritorio.");
        
    } catch (DocumentException | IOException | SQLException e) {
        e.printStackTrace();
    }
}

    public static void exportarHabitacionesOcupadas() {
    Document document = new Document();
    try {
        String ruta = System.getProperty("user.home") + File.separator + "Desktop" + File.separator + "HabitacionesOcupadas.pdf";
        File file = new File(ruta);
        file.getParentFile().mkdirs();
        
        PdfWriter.getInstance(document, new FileOutputStream(ruta));
        document.open();
        
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLACK);
        Paragraph title = new Paragraph("Habitaciones Ocupadas", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
        document.add(new Paragraph(" "));
        
        PdfPTable table = new PdfPTable(8);
        table.setWidthPercentage(100);
        
        String[] headers = {"ID de la habitación", "Nombre", "Tipo", "Precio", "Cliente", "Fecha Entrada", "Fecha Salida", "Personas"};
        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(header, FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(cell);
        }
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            
            String query = "SELECT h.id_habitacion, h.nombre, h.tipo, h.precio, " +
                           "CONCAT(c.nombre, ' ', c.apellido) AS cliente, " +
                           "r.fecha_entrada, r.fecha_salida, r.numero_personas " +
                           "FROM habitaciones h " +
                           "JOIN reservas r ON h.id_habitacion = r.id_habitacion " +
                           "JOIN reservas_clientes rc ON r.id_reserva = rc.id_reserva " +
                           "JOIN clientes c ON rc.id_cliente = c.id_cliente " +
                           "WHERE h.disponibilidad = false AND rc.es_titular = true";
            
            try (ResultSet rs = stmt.executeQuery(query)) {
                while (rs.next()) {
                    table.addCell(rs.getString("id_habitacion"));
                    table.addCell(rs.getString("nombre"));
                    table.addCell(rs.getString("tipo"));
                    table.addCell(rs.getString("precio") + ("$"));
                    table.addCell(rs.getString("cliente"));
                    table.addCell(rs.getString("fecha_entrada"));
                    table.addCell(rs.getString("fecha_salida"));
                    table.addCell(rs.getString("numero_personas"));
                }
            }
        }
        
        document.add(table);
        document.close();
        System.out.println("PDF de habitaciones ocupadas creado exitosamente en: " + ruta);
        JOptionPane.showMessageDialog(null, "PDF de habitaciones ocupadas exportado exitosamente al escritorio.");
        
    } catch (DocumentException | IOException | SQLException e) {
        e.printStackTrace();
    }
}
    
    public static void main(String[] args) {
        exportarHistorialReservas();
        exportarReservasServicios();
        exportarHabitacionesDisponibles();
        exportarHabitacionesOcupadas();
    }
}