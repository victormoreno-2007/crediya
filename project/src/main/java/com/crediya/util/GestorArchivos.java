package com.crediya.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class GestorArchivos {
    private static final String PATH_REPORTES = "reportes_crediya";

    public GestorArchivos() {

    }

    private void CrearCarpeta() {
        Path path = Paths.get(PATH_REPORTES);
        if (!Files.exists(path)) {
            try {
                Files.createDirectory(path);
            } catch (IOException e) {
                System.out.println("Error al crear la carpeta de los archivos .txt " + e.getMessage());
            }
        }
    }

    public boolean GuardarReportes(String nombreArchivo, List<String> datos) {

        File dirFile = new File(PATH_REPORTES);
        if (!dirFile.exists()) {
            dirFile.mkdir();
        }
        File file = new File(PATH_REPORTES, nombreArchivo);

        try (FileWriter fw = new FileWriter(file);
             PrintWriter pw = new PrintWriter(fw)) {

            pw.println(" ----------- REPORTE CREDIYA -------------");
            pw.println("------------------------------------------");

            System.out.println("archivo creado exitosamente " + file.getName());
            return true;

        } catch (IOException e) {
            System.out.println("Error al crear el archvio " + e.getMessage());
            return false;
        }
    }

}
