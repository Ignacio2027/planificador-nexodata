package planificador;

import java.util.ArrayList;
import java.util.List;

public abstract class Simulador {
    protected List<Proceso> procesosOriginales;
    protected List<Proceso> procesos;
    protected boolean conTraza;

    public Simulador(List<Proceso> procesos, boolean conTraza) {
        this.procesosOriginales = procesos;
        this.conTraza = conTraza;
        // Hacemos una copia profunda para no alterar los datos originales si se ejecutan varios algoritmos
        this.procesos = new ArrayList<>();
        for (Proceso p : procesos) {
            this.procesos.add(new Proceso(p.getNombre(), p.getLlegada(), p.getRafaga()));
        }
    }

    public abstract void ejecutar();
}
