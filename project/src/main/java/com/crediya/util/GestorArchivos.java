package com.crediya.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class GestorArchivos {
    private static final String PATH_REPORTES = "reportes_crediya";

    public GestorArchivos() {
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

            for (String linea : datos) {
                pw.println(linea);
            }


            System.out.println("Archivo creado exitosamente: " + file.getName());
            return true;

        } catch (IOException e) {
            System.out.println("Error al crear el archivo: " + e.getMessage());
            return false;
        }
    }
    public boolean anexarRegistro(String nombreArchivo, String lineaDatos) {
        File dirFile = new File(PATH_REPORTES);
        if (!dirFile.exists()) {
            dirFile.mkdir();
        }

        File file = new File(PATH_REPORTES, nombreArchivo);

        try (FileWriter fw = new FileWriter(file, true);
             PrintWriter pw = new PrintWriter(fw)) {

            pw.println(lineaDatos);
            return true;

        } catch (IOException e) {
            System.out.println("Error al respaldar en archivo plano: " + e.getMessage());
            return false;
        }
    }
}