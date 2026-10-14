package planificador;

import java.util.List;

public class ResultadoSimulacion {
    private String gantt;
    private List<Proceso> procesos;
    private int cambiosContexto;
    private List<String> traza;

    public ResultadoSimulacion(String gantt, List<Proceso> procesos, int cambiosContexto, List<String> traza) {
        this.gantt = gantt;
        this.procesos = procesos;
        this.cambiosContexto = cambiosContexto;
        this.traza = traza;
    }

    public String getGantt() { return gantt; }
    public List<Proceso> getProcesos() { return procesos; }
    public int getCambiosContexto() { return cambiosContexto; }
    public List<String> getTraza() { return traza; }
}
