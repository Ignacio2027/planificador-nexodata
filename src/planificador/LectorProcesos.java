
package planificador;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class LectorProcesos {

    public static List<Proceso> leer(String nombreFichero)
            throws IOException {

        List<Proceso> procesos = new ArrayList<>();
        List<String> lineas = Files.readAllLines(
                Path.of(nombreFichero),
                StandardCharsets.UTF_8
        );

        int numeroLinea = 0;

        for (String linea : lineas) {
            numeroLinea++;

            linea = linea.trim();

            // Ignorar líneas vacías y comentarios.
            if (linea.isEmpty() || linea.startsWith("#")) {
                continue;
            }

            String[] datos = linea.split(";", -1);

            // Comprobar que hay tres campos.
            if (datos.length != 3) {
                throw new IllegalArgumentException(
                        "Error en la línea " + numeroLinea
                                + ": deben aparecer nombre;llegada;ráfaga."
                );
            }

            String nombre = datos[0].trim();

            if (nombre.isEmpty()) {
                throw new IllegalArgumentException(
                        "Error en la línea " + numeroLinea
                                + ": el nombre no puede estar vacío."
                );
            }

            try {
                int llegada = Integer.parseInt(datos[1].trim());
                int rafaga = Integer.parseInt(datos[2].trim());

                if (llegada < 0) {
                    throw new IllegalArgumentException(
                            "Error en la línea " + numeroLinea
                                    + ": la llegada no puede ser negativa."
                    );
                }

                if (rafaga <= 0) {
                    throw new IllegalArgumentException(
                            "Error en la línea " + numeroLinea
                                    + ": la ráfaga debe ser mayor que cero."
                    );
                }

                procesos.add(new Proceso(nombre, llegada, rafaga));

            } catch (NumberFormatException e) {
                throw new IllegalArgumentException(
                        "Error en la línea " + numeroLinea
                                + ": la llegada y la ráfaga deben ser números enteros."
                );
            }
        }

        return procesos;
    }
    
}

