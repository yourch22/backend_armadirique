package com.api.muebleria.armadirique.modules.productoReport;

import com.api.muebleria.armadirique.modules.producto.entity.Producto;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

@Service
public class ListarProductoExcel {

    private List<Producto> productos;

    public ListarProductoExcel(List<Producto> productos) {
        this.productos = productos;
    }

    public void export(HttpServletResponse response) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Productos");

        Row header = sheet.createRow(0);
        String[] columnas = {"ID", "NOMBRE", "DESCRIPCIÓN", "PRECIO", "STOCK", "IMAGEN_URL", "ESTADO", "ID CATEGORIA", "ID USUARIO", "NOMBRE USUARIO"};

        for (int i = 0; i < columnas.length; i++) {
            Cell cell = header.createCell(i);
            cell.setCellValue(columnas[i]);
        }

        int fila = 1;
        for (Producto p : productos) {
            Row row = sheet.createRow(fila++);

            row.createCell(0).setCellValue(p.getProductoId());
            row.createCell(1).setCellValue(p.getNombre());
            row.createCell(2).setCellValue(p.getDescripcion());
            row.createCell(3).setCellValue(p.getPrecio().doubleValue());
            row.createCell(4).setCellValue(p.getStock());
            row.createCell(5).setCellValue(p.getImagenUrl());
            row.createCell(6).setCellValue(p.isEstado());
            row.createCell(7).setCellValue(p.getCategoria().getCategoriaId());
            // row.createCell(8).setCellValue(p.getUsuario().getUsuarioId());
            // row.createCell(9).setCellValue(p.getUsuario().getNombre()); // Asegúrate que exista getNombre()
            if (p.getUsuario() != null) {
                row.createCell(8).setCellValue(p.getUsuario().getUsuarioId());
                row.createCell(9).setCellValue(p.getUsuario().getNombre());
            } else {
                row.createCell(8).setCellValue("N/A");
                row.createCell(9).setCellValue("N/A");
            }
        }

        // Estilo opcional: auto-ajustar columnas
        for (int i = 0; i < columnas.length; i++) {
            sheet.autoSizeColumn(i);
        }

        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }
}
