package com.crediya.util;

import com.crediya.domain.errors.ErrorType;

import java.util.Scanner;

public class ScannerMenu {
    private final Scanner scan;

    public ScannerMenu() {
        this.scan = new Scanner(System.in);
    }


    public int leerEntero(String msg) {
        System.out.print(msg + " ");
        try {
            String valorIngresado = scan.nextLine();
            return Integer.parseInt(valorIngresado);
        } catch (NumberFormatException e) {
            System.out.println(" Por favor ingrese un número entero válido.");
            return -1;
        }
    }

    public int leerEnteroValido(String msg, String regex, ErrorType errorType) {
        String entrada;

        do {
            System.out.println(msg + "");
            entrada = scan.nextLine();

            if (!entrada.matches(regex)){
                System.out.println(errorType.getReason());
            }
        } while (!entrada.matches(regex));

        return Integer.parseInt(entrada);
    }



    public double leerDecimalValido(String msg, String regex, ErrorType errorType) {
          String entrada;

          do {
              System.out.println( msg + "");
              entrada = scan.nextLine();

              if (!entrada.matches(regex)){
                  System.out.println(errorType.getReason());
              }

          } while (!entrada.matches(regex));
          return Double.parseDouble(entrada);
    }


    public String leerTexto(String msg) {
        System.out.print(msg + " ");
        return scan.nextLine();
    }

    public String leerTextoValido(String msg, String regex, ErrorType errorType) {
        String entrada;

        do {
            System.out.println(msg + "");
            entrada = scan.nextLine();

            if (!entrada.matches(regex)){
                System.out.println(errorType.getReason());
            }
        } while (!entrada.matches(regex));

        return entrada;
    }

    public void cerrar() {
        scan.close();
    }
}