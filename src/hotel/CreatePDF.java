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

public class CreatePDF {
    
    public static void exportarTablaPDF(String nombreTabla) {
        Document document = new Document();
        try {
            String ruta = System.getProperty("user.home") + File.separator + "Desktop" + File.separator + nombreTabla + ".pdf";
            File file = new File(ruta);
            file.getParentFile().mkdirs();
            
            PdfWriter.getInstance(document, new FileOutputStream(ruta));
            document.open();
            
            // Título del documento
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLACK);
            Paragraph title = new Paragraph("Datos de la tabla " + nombreTabla, titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph(" ")); // Espacio después del título
            
            // Obtener datos de la tabla
            List<String> columnas = new ArrayList<>();
            List<List<String>> datos = new ArrayList<>();
            
            try (Connection conn = DatabaseConnection.getConnection();
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT * FROM " + nombreTabla)) {
                
                ResultSetMetaData metaData = rs.getMetaData();
                int columnCount = metaData.getColumnCount();
                
                // Obtener nombres de las columnas
                for (int i = 1; i <= columnCount; i++) {
                    columnas.add(metaData.getColumnName(i));
                }
                
                // Obtener datos
                while (rs.next()) {
                    List<String> fila = new ArrayList<>();
                    for (int i = 1; i <= columnCount; i++) {
                        fila.add(rs.getString(i));
                    }
                    datos.add(fila);
                }
            }
            
            // Crear tabla PDF
            PdfPTable table = new PdfPTable(columnas.size());
            table.setWidthPercentage(100);
            
            // Añadir encabezados
            for (String columna : columnas) {
                PdfPCell cell = new PdfPCell(new Phrase(columna, FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                table.addCell(cell);
            }
            
            // Añadir datos
            for (List<String> fila : datos) {
                for (String valor : fila) {
                    table.addCell(valor);
                }
            }
            
            document.add(table);
            document.close();
            System.out.println("PDF creado exitosamente en: " + ruta);
            
        } catch (DocumentException | IOException | SQLException e) {
            e.printStackTrace();
        }
    }
    
    // Método main para pruebas
    public static void main(String[] args) {
        exportarTablaPDF("nombre_de_tu_tabla");
    }
}