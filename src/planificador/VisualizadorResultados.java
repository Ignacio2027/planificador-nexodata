package planificador;

import java.util.List;

public class VisualizadorResultados {

    public static void mostrar(String titulo, ResultadoSimulacion res, boolean conTraza) {
        System.out.println("=== " + titulo + " ===");
        System.out.println("CPU " + res.getGantt());

        System.out.printf("%-10s %-8s %-8s %-6s %-8s %-8s %-10s%n",
                "Proceso", "Llegada", "Ráfaga", "Fin", "Retorno", "Espera", "Respuesta");

        double sumaRetorno = 0;
        double sumaEspera = 0;
        double sumaRespuesta = 0;
        int total = res.getProcesos().size();

        for (Proceso p : res.getProcesos()) {
            System.out.printf("%-10s %-8d %-8d %-6d %-8d %-8d %-10d%n",
                    p.getNombre(), p.getLlegada(), p.getRafaga(),
                    p.getFin(), p.getRetorno(), p.getEspera(), p.getRespuesta());

            sumaRetorno += p.getRetorno();
            sumaEspera += p.getEspera();
            sumaRespuesta += p.getRespuesta();
        }

        System.out.printf("Medias: retorno %.2f | espera %.2f | respuesta %.2f%n",
                (sumaRetorno / total), (sumaEspera / total), (sumaRespuesta / total));

        System.out.println("Cambios de contexto: " + res.getCambiosContexto());

        if (conTraza && res.getTraza() != null && !res.getTraza().isEmpty()) {
            System.out.println("Traza de estados:");
            for (String t : res.getTraza()) {
                System.out.println(t);
            }
        }
        System.out.println();
    }
}
