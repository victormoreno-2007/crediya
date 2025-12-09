package com.crediya.service;

public class MotorPrestamos {
    
    // Calcular la cuota fija mensual 

    public double calcularCuota( double monto, double interesAnual, double  cuotas){
        
        double i = (interesAnual / 1000.0)/12.0;

        if( i == 0){

            return monto/cuotas;
        }
        
        return monto * (i * Math.pow(1+ i, cuotas))/ (Math.pow(1 + i, cuotas)-1);
    }

    public double calcularMontoTotal(double cuota, int cuotas){
        return cuota * cuotas;
    }
}
