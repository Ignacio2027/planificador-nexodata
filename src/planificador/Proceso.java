package planificador;

public class Proceso {
    private String nombre;
    private int llegada;
    private int rafaga;
    private int tiempoRestante;
    private EstadoProceso estado;

    // Atributos para métricas
    private int fin = 0;
    private int tiempoEjecutadoPorPrimeraVez = -1;
    private int quantumRestante = 0;

    public Proceso(String nombre, int llegada, int rafaga) {
        this.nombre = nombre;
        this.llegada = llegada;
        this.rafaga = rafaga;
        this.tiempoRestante = rafaga;
        this.estado = EstadoProceso.NUEVO;
    }

    // Getters y Setters
    public String getNombre() { return nombre; }
    public int getLlegada() { return llegada; }
    public int getRafaga() { return rafaga; }

    public int getTiempoRestante() { return tiempoRestante; }
    public void setTiempoRestante(int tiempoRestante) { this.tiempoRestante = tiempoRestante; }

    public EstadoProceso getEstado() { return estado; }
    public void setEstado(EstadoProceso estado) { this.estado = estado; }

    public int getFin() { return fin; }
    public void setFin(int fin) { this.fin = fin; }

    public int getTiempoEjecutadoPorPrimeraVez() { return tiempoEjecutadoPorPrimeraVez; }
    public void setTiempoEjecutadoPorPrimeraVez(int t) { this.tiempoEjecutadoPorPrimeraVez = t; }

    public int getQuantumRestante() { return quantumRestante; }
    public void setQuantumRestante(int q) { this.quantumRestante = q; }

    // Fórmulas de métricas del enunciado
    public int getRetorno() {
        return fin - llegada;
    }

    public int getEspera() {
        return getRetorno() - rafaga;
    }

    public int getRespuesta() {
        return tiempoEjecutadoPorPrimeraVez - llegada;
    }
}