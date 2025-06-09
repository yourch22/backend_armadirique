package com.api.muebleria.armadirique.modules.productoReport;

import java.util.List;
import java.util.Map;

import com.api.muebleria.armadirique.modules.producto.entity.Producto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractXlsxView;

@Component("/productos/listar.xlsx")
public class ListarProductoExcel extends AbstractXlsxView {

    @Override
    protected void buildExcelDocument(Map<String, Object> model, Workbook workbook,
                                      HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setHeader("Content-Disposition", "attachment; filename=\"listado-productos.xlsx\"");
        Sheet hoja = workbook.createSheet("Productos");

        Row filaTitulo = hoja.createRow(0);
        Cell celda = filaTitulo.createCell(0);
        celda.setCellValue("LISTADO DE STOCK DE PRODUCTOS");

        Row filaData = hoja.createRow(2);
        String[] columnas = {"ID","NOMBRES", "DESCRIPCION", "PRECIO", "STOCK",
                "IMAGEN_URL", "ESTADO", "ID CATEGORIA", "ID USUARIO", "NOMBRE USUARIO" };
        for (int i = 0; i < columnas.length; i++) {
            celda = filaData.createCell(i);
            celda.setCellValue(columnas[i]);
        }

        List<Producto> listaC = (List<Producto>) model.get("productos");

        int numFila=3;
        for (Producto producto : listaC) {
            filaData = hoja.createRow(numFila);

            filaData.createCell(0).setCellValue(producto.getProductoId());
            filaData.createCell(0).setCellValue(producto.getNombre());
            filaData.createCell(0).setCellValue(producto.getDescripcion());
            filaData.createCell(0).setCellValue(producto.getPrecio().toString());
            filaData.createCell(0).setCellValue(producto.getStock());
            filaData.createCell(0).setCellValue(producto.getImagenUrl());
            filaData.createCell(0).setCellValue(producto.isEstado());
            filaData.createCell(0).setCellValue(producto.getCategoria().getCategoriaId());
            filaData.createCell(0).setCellValue(producto.getUsuario().getUsuarioId());

            numFila ++;
        }
    }
}
