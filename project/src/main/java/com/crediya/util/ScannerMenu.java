package com.crediya.util;

import com.crediya.domain.errors.ErrorType;
import com.crediya.domain.errors.OperacionCanceladaExcepcion;

import java.util.Scanner;

public class ScannerMenu {
    private final Scanner scan;
    private static final String COMANDO_CANCELAR = "C";

    public ScannerMenu() {
        this.scan = new Scanner(System.in);
    }

    private void verificarCancelacion(String entrada) {
        if (entrada.trim().equalsIgnoreCase(COMANDO_CANCELAR)) {
            throw new OperacionCanceladaExcepcion();
        }
    }

    public int leerEntero(String msg) {
        System.out.print(msg + " ");
        try {
            String valorIngresado = scan.nextLine().trim();
            if (valorIngresado.isEmpty()) return -1;
            return Integer.parseInt(valorIngresado);
        } catch (NumberFormatException e) {
            System.out.println("Por favor ingrese un número entero válido.");
            return -1;
        }
    }

    public int leerEnteroValido(String msg, String regex, ErrorType errorType) {
        String entrada;

        do {
            System.out.println(msg + " (o escriba 'C' para cancelar)");
            entrada = scan.nextLine().trim();
            if (entrada.isEmpty()) {
                System.out.println("El valor no puede estar vacío.");
                continue;
            }

            verificarCancelacion(entrada);

            if (!entrada.matches(regex)){
                System.out.println("Error " + errorType.getReason());
            }
        } while (entrada.isEmpty() || !entrada.matches(regex));

        return Integer.parseInt(entrada);
    }

    public double leerDecimalValido(String msg, String regex, ErrorType errorType) {
        String entrada;

        do {
            System.out.println(msg + " (o escriba 'C' para cancelar)");
            entrada = scan.nextLine().trim();

            if (entrada.isEmpty()) {
                System.out.println("El valor no puede estar vacío.");
                continue;
            }

            verificarCancelacion(entrada);

            entrada = entrada.replace(",", ".");

            if (!entrada.matches(regex)){
                System.out.println("Error" + errorType.getReason() + " (Formato incorrecto)");
            }

        } while (entrada.isEmpty() || !entrada.matches(regex));

        try {
            return Double.parseDouble(entrada);
        } catch (NumberFormatException e) {
            System.out.println("Error crítico convirtiendo número.");
            return 0.0;
        }
    }

    public String leerTexto(String msg) {
        System.out.print(msg + " ");
        return scan.nextLine().trim();
    }

    public String leerTextoValido(String msg, String regex, ErrorType errorType) {
        String entrada;

        do {
            System.out.println(msg + " (o escriba 'C' para cancelar)");
            entrada = scan.nextLine().trim();

            if (entrada.isEmpty()) {
                System.out.println("El campo no puede estar vacío.");
                continue;
            }

            verificarCancelacion(entrada);

            if (!entrada.matches(regex)){
                System.out.println("Error" + errorType.getReason());
            }
        } while (entrada.isEmpty() || !entrada.matches(regex));

        return entrada;
    }

    public void cerrar() {
        scan.close();
    }
}