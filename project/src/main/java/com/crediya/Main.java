package com.crediya;

import com.crediya.models.Cliente;
import com.crediya.repository.ClienteRepository;
import com.crediya.data.repositories.ClienteRepositoryImpl;


public class Main {
    public static void main(String[] args) {
        System.out.println("🧪 INICIANDO PRUEBA DE ARQUITECTURA DE CAPAS (CLIENTES)...");
        System.out.println("---------------------------------------------------------");

        // 1. Instanciamos el Repositorio Nuevo (La implementación oculta la magia)
        ClienteRepository clienteRepo = new ClienteRepositoryImpl();

        // 2. Creamos un Cliente Falso (Modelo de Negocio)
        // Usa un documento que NO exista en tu base de datos para probar
        String docPrueba = "TEST-999";
        Cliente nuevoCliente = new Cliente("Cliente De Prueba", docPrueba, "test@email.com", "3001234567");

        // 3. Probamos GUARDAR (El flujo: Modelo -> Mapper -> Entity -> SQL)
        System.out.println("1. Intentando guardar cliente...");
        clienteRepo.registrar(nuevoCliente);

        // 4. Probamos BUSCAR (El flujo: SQL -> Entity -> Mapper -> Modelo)
        System.out.println("\n2. Intentando recuperar cliente de la BD...");
        Cliente recuperado = clienteRepo.buscarPorDocumento(docPrueba);

        if (recuperado != null) {
            System.out.println("   ✅ ¡ÉXITO! Cliente encontrado:");
            System.out.println("      ID (Generado por DB): " + recuperado.getId());
            System.out.println("      Nombre: " + recuperado.getNombre());
            System.out.println("      Teléfono: " + recuperado.getTelefono());
            System.out.println("      (Si ves esto, el Mapper funcionó en ambas direcciones)");
        } else {
            System.out.println("   ❌ ERROR: El cliente no se encontró. Algo falló en la lectura.");
        }

        System.out.println("\n---------------------------------------------------------");
        System.out.println("🏁 FIN DE LA PRUEBA");
    }
}