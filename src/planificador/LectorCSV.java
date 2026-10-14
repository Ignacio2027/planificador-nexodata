package planificador;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LectorCSV {

    public static List<Proceso> leerFichero(String rutaFichero) {
        List<Proceso> lista = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(rutaFichero))) {
            String linea;
            int numLinea = 0;

            while ((linea = br.readLine()) != null) {
                numLinea++;
                linea = linea.trim();

                // Ignorar líneas vacías o comentarios
                if (linea.isEmpty() || linea.startsWith("#")) {
                    continue;
                }

                String[] partes = linea.split(";");
                if (partes.length != 3) {
                    System.err.println("Error en la línea " + numLinea + ": formato mal formado.");
                    System.exit(1);
                }

                String nombre = partes[0].trim();
                int llegada, rafaga;

                try {
                    llegada = Integer.parseInt(partes[1].trim());
                    rafaga = Integer.parseInt(partes[2].trim());
                } catch (NumberFormatException e) {
                    System.err.println("Error en la línea " + numLinea + ": llegada o ráfaga no numérica.");
                    System.exit(1);
                    return null;
                }

                // Criterio C1: Rechazar ráfagas <= 0 o llegadas negativas
                if (llegada < 0 || rafaga <= 0) {
                    System.err.println("Error en la línea " + numLinea + ": valor no válido (llegada < 0 o ráfaga <= 0).");
                    System.exit(1);
                }

                lista.add(new Proceso(nombre, llegada, rafaga));
            }
        } catch (IOException e) {
            System.err.println("No se encuentra el fichero: " + rutaFichero);
            System.exit(1);
        }

        return lista;
    }
}
