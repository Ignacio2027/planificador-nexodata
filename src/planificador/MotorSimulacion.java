package planificador;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class MotorSimulacion {

    // Método para ejecutar FCFS, SJF o Round Robin
    public static ResultadoSimulacion simular(List<Proceso> listaOriginal, String algoritmo, int quantum, boolean conTraza) {
        // Clonar procesos para evitar modificar la lista original
        List<Proceso> procesos = new ArrayList<>();
        for (Proceso p : listaOriginal) {
            procesos.add(new Proceso(p.getNombre(), p.getLlegada(), p.getRafaga()));
        }

        int tiempoActual = 0;
        int terminados = 0;
        int totalProcesos = procesos.size();

        Proceso cpuActual = null;
        String gantt = "";
        int cambiosContexto = -1; // La primera carga no cuenta como cambio de contexto[cite: 6]
        String ultimoProcesoEnCPU = null;

        Queue<Proceso> colaListos = new LinkedList<>();
        List<String> trazaList = new ArrayList<>();

        while (terminados < totalProcesos) {
            // 1. Comprobar qué procesos llegan en el instante actual (t)
            for (Proceso p : procesos) {
                if (p.getLlegada() == tiempoActual && p.getEstado() == EstadoProceso.NUEVO) {
                    p.setEstado(EstadoProceso.LISTO);
                    colaListos.add(p);
                    if (conTraza) {
                        trazaList.add("t=" + tiempoActual + " " + p.getNombre() + " NUEVO -> LISTO (llega al sistema)");
                    }
                }
            }

            // 2. Comprobar si el proceso en la CPU ha terminado o agotado su quantum
            if (cpuActual != null) {
                if (cpuActual.getTiempoRestante() == 0) {
                    cpuActual.setEstado(EstadoProceso.TERMINADO);
                    cpuActual.setFin(tiempoActual);
                    terminados++;
                    if (conTraza) {
                        trazaList.add("t=" + tiempoActual + " " + cpuActual.getNombre() + " EJECUCION -> TERMINADO (completa su ráfaga)");
                    }
                    cpuActual = null;
                } else if (algoritmo.equals("rr") && cpuActual.getQuantumRestante() == 0) {
                    // Regla 3 y 4 de Round Robin: Si agota el quantum
                    // Verificamos si hay más gente en la cola antes de desalojar
                    if (!colaListos.isEmpty()) {
                        cpuActual.setEstado(EstadoProceso.LISTO);
                        colaListos.add(cpuActual);
                        if (conTraza) {
                            trazaList.add("t=" + tiempoActual + " " + cpuActual.getNombre() + " EJECUCION -> LISTO (agota el quantum)");
                        }
                        cpuActual = null;
                    } else {
                        // Regla 4: Quantum agotado sin nadie esperando -> Sigue en la CPU con nuevo quantum
                        cpuActual.setQuantumRestante(quantum);
                    }
                }
            }

            // 3. Si la CPU está libre, el algoritmo elige a quién meter
            if (cpuActual == null && !colaListos.isEmpty()) {
                if (algoritmo.equals("fcfs") || algoritmo.equals("rr")) {
                    cpuActual = colaListos.poll();
                } else if (algoritmo.equals("sjf")) {
                    // SJF sin desalojo: elige el proceso listo con menor ráfaga (y empates por llegada/orden)[cite: 6]
                    Proceso mejor = null;
                    for (Proceso p : colaListos) {
                        if (mejor == null || p.getRafaga() < mejor.getRafaga() ||
                                (p.getRafaga() == mejor.getRafaga() && p.getLlegada() < mejor.getLlegada())) {
                            mejor = p;
                        }
                    }
                    if (mejor != null) {
                        colaListos.remove(mejor);
                        cpuActual = mejor;
                    }
                }

                if (cpuActual != null) {
                    if (cpuActual.getTiempoEjecutadoPorPrimeraVez() == -1) {
                        cpuActual.setTiempoEjecutadoPorPrimeraVez(tiempoActual);
                    }
                    cpuActual.setEstado(EstadoProceso.EJECUCION);
                    if (algoritmo.equals("rr")) {
                        cpuActual.setQuantumRestante(quantum);
                    }
                    if (conTraza) {
                        trazaList.add("t=" + tiempoActual + " " + cpuActual.getNombre() + " LISTO -> EJECUCION (el planificador lo elige)");
                    }

                    // Contar cambios de contexto
                    if (ultimoProcesoEnCPU != null && !ultimoProcesoEnCPU.equals(cpuActual.getNombre())) {
                        cambiosContexto++;
                    } else if (ultimoProcesoEnCPU == null && cambiosContexto == -1) {
                        cambiosContexto = 0; // Inicialización
                    }
                    ultimoProcesoEnCPU = cpuActual.getNombre();
                }
            }

            // 4. Ejecutar una unidad de tiempo
            if (cpuActual != null) {
                gantt += cpuActual.getNombre();
                cpuActual.setTiempoRestante(cpuActual.getTiempoRestante() - 1);
                if (algoritmo.equals("rr")) {
                    cpuActual.setQuantumRestante(cpuActual.getQuantumRestante() - 1);
                }
            } else {
                gantt += "-"; // CPU ociosa[cite: 6]
                // Si la CPU estaba ociosa y vuelve a haber un cambio, se contabiliza si entra otro proceso después
                if (ultimoProcesoEnCPU != null) {
                    // Si pasa de proceso a ocioso y luego a otro, cuenta como cambio de contexto según el criterio
                    ultimoProcesoEnCPU = null;
                }
            }

            tiempoActual++;
        }

        return new ResultadoSimulacion(gantt, procesos, cambiosMath(cambiosContexto), trazaList);
    }

    private static int cambiosMath(int c) {
        return Math.max(0, c);
    }
}