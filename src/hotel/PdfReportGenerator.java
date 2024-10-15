package hotel;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
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
        
        PdfPTable table = new PdfPTable(7);
        table.setWidthPercentage(100);
        
        String[] headers = {"Cliente", "Habitación", "Tipo", "Precio", "Fecha Entrada", "Fecha Salida", "Estado"};
        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(header, FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(cell);
        }
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            
            String query = "SELECT CONCAT(c.nombre, ' ', c.apellido) AS cliente, " +
                           "h.nombre AS habitacion, h.tipo, h.precio, " +
                           "r.fecha_entrada, r.fecha_salida, " +
                           "CASE WHEN r.fecha_salida < CURDATE() THEN 'Finalizada' " +
                           "     WHEN r.fecha_entrada <= CURDATE() AND r.fecha_salida >= CURDATE() THEN 'En curso' " +
                           "     ELSE 'Futura' END AS estado " +
                           "FROM reservas r " +
                           "JOIN habitaciones h ON r.id_habitacion = h.id_habitacion " +
                           "JOIN reservas_clientes rc ON r.id_reserva = rc.id_reserva " +
                           "JOIN clientes c ON rc.id_cliente = c.id_cliente " +
                           "WHERE rc.es_titular = true " +
                           "ORDER BY r.fecha_entrada DESC";
            
            try (ResultSet rs = stmt.executeQuery(query)) {
                while (rs.next()) {
                    table.addCell(rs.getString("cliente"));
                    table.addCell(rs.getString("habitacion"));
                    table.addCell(rs.getString("tipo"));
                    table.addCell(rs.getString("precio"));
                    table.addCell(rs.getString("fecha_entrada"));
                    table.addCell(rs.getString("fecha_salida"));
                    table.addCell(rs.getString("estado"));
                }
            }
        }
        
        document.add(table);
        document.close();
        System.out.println("PDF del historial de reservas creado exitosamente en: " + ruta);
        JOptionPane.showMessageDialog(null, "PDF del historial de reservas exportado exitosamente al escritorio.");
        
    } catch (DocumentException | IOException | SQLException e) {
        e.printStackTrace();
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
            
            List<String> columnas = new ArrayList<>();
            List<List<String>> datos = new ArrayList<>();
            
            try (Connection conn = DatabaseConnection.getConnection();
                 Statement stmt = conn.createStatement()) {
                
                String query = "SELECT id_habitacion, nombre, tipo, precio " +
                               "FROM habitaciones " +
                               "WHERE disponibilidad = true";
                
                try (ResultSet rs = stmt.executeQuery(query)) {
                    ResultSetMetaData metaData = rs.getMetaData();
                    int columnCount = metaData.getColumnCount();
                    
                    for (int i = 1; i <= columnCount; i++) {
                        columnas.add(metaData.getColumnLabel(i));
                    }
                    
                    while (rs.next()) {
                        List<String> fila = new ArrayList<>();
                        for (int i = 1; i <= columnCount; i++) {
                            fila.add(rs.getString(i));
                        }
                        datos.add(fila);
                    }
                }
            }
            
            PdfPTable table = new PdfPTable(columnas.size());
            table.setWidthPercentage(100);
            
            for (String columna : columnas) {
                PdfPCell cell = new PdfPCell(new Phrase(columna, FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                table.addCell(cell);
            }
            
            for (List<String> fila : datos) {
                for (String valor : fila) {
                    table.addCell(valor);
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
        
        String[] headers = {"ID Habitación", "Nombre", "Tipo", "Precio", "Cliente", "Fecha Entrada", "Fecha Salida", "Personas"};
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
                    table.addCell(rs.getString("precio"));
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
        exportarHabitacionesDisponibles();
        exportarHabitacionesOcupadas();
    }
}