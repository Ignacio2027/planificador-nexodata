package planificador;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Uso: java planificador.Main <fichero.csv> <fcfs|sjf|rr|todos> [quantum] [--traza]");
            return;
        }

        String fichero = args[0];
        String algoritmo = args[1].toLowerCase();
        int quantum = 2; // Por defecto el quantum es 2 si no se indica[cite: 3]
        boolean conTraza = false;

        // Analizar argumentos adicionales (quantum o --traza)
        for (int i = 2; i < args.length; i++) {
            if (args[i].equals("--traza")) {
                conTraza = true;
            } else {
                try {
                    quantum = Integer.parseInt(args[i]);
                } catch (NumberFormatException ignored) {}
            }
        }

        // Cargar los procesos desde el CSV correspondiente
        List<Proceso> procesos = LectorCSV.leerFichero(fichero);
        if (procesos == null || procesos.isEmpty()) {
            System.out.println("No se han podido cargar procesos.");
            return;
        }

        // Ejecución según el tipo de algoritmo solicitado
        if (algoritmo.equals("fcfs") || algoritmo.equals("sjf") || algoritmo.equals("rr")) {
            ResultadoSimulacion res = MotorSimulacion.simular(procesos, algoritmo, quantum, conTraza);
            VisualizadorResultados.mostrar(obtenerTitulo(algoritmo, quantum), res, conTraza);
        } else if (algoritmo.equals("todos")) {
            // Ejecutar los tres algoritmos seguidos de forma independiente
            ResultadoSimulacion resFcfs = MotorSimulacion.simular(procesos, "fcfs", quantum, conTraza);
            VisualizadorResultados.mostrar("FCFS", resFcfs, conTraza);

            ResultadoSimulacion resSjf = MotorSimulacion.simular(procesos, "sjf", quantum, conTraza);
            VisualizadorResultados.mostrar("SJF sin desalojo", resSjf, conTraza);

            ResultadoSimulacion resRr = MotorSimulacion.simular(procesos, "rr", quantum, conTraza);
            VisualizadorResultados.mostrar("Round Robin (q=" + quantum + ")", resRr, conTraza);
        } else {
            System.out.println("Algoritmo no reconocido. Usa: fcfs, sjf, rr o todos.");
        }
    }

    private static String obtenerTitulo(String algoritmo, int quantum) {
        switch (algoritmo) {
            case "fcfs": return "FCFS";
            case "sjf": return "SJF sin desalojo";
            case "rr": return "Round Robin (q=" + quantum + ")";
            default: return algoritmo;
        }
    }
}