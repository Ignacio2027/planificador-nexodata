
package planificador;

public class Proceso {

    private String nombre;
    private int llegada;
    private int rafaga;
    private int tiempoRestante;
    private EstadoProceso estado;

    private int instanteFin;
    private int primeraEjecucion;

    public Proceso(String nombre, int llegada, int rafaga) {
        this.nombre = nombre;
        this.llegada = llegada;
        this.rafaga = rafaga;
        this.tiempoRestante = rafaga;
        this.estado = EstadoProceso.NUEVO;
        this.instanteFin = -1;
        this.primeraEjecucion = -1;
    }

    public String getNombre() {
        return nombre;
    }

    public int getLlegada() {
        return llegada;
    }

    public int getRafaga() {
        return rafaga;
    }

    public int getTiempoRestante() {
        return tiempoRestante;
    }

    public EstadoProceso getEstado() {
        return estado;
    }

    public int getInstanteFin() {
        return instanteFin;
    }

    public int getPrimeraEjecucion() {
        return primeraEjecucion;
    }

    public void setTiempoRestante(int tiempoRestante) {
        this.tiempoRestante = tiempoRestante;
    }

    public void setEstado(EstadoProceso estado) {
        this.estado = estado;
    }

    public void setInstanteFin(int instanteFin) {
        this.instanteFin = instanteFin;
    }

    public void setPrimeraEjecucion(int primeraEjecucion) {
        this.primeraEjecucion = primeraEjecucion;
    }

    public Proceso copia() {
        return new Proceso(nombre, llegada, rafaga);
    }

    public int getRetorno() {
        return instanteFin - llegada;
    }

    public int getEspera() {
        return getRetorno() - rafaga;
    }

    public int getRespuesta() {
        return primeraEjecucion - llegada;
    }

}

