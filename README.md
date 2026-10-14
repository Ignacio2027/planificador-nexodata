# Simulador de Planificación de CPU - NexoData

Este proyecto implementa un simulador en Java de un planificador de corto plazo para evaluar distintas políticas de asignación de CPU en los procesos por lotes nocturnos de NexoData.

## 1. Qué hace el proyecto
El programa procesa una lista de procesos lejana desde un fichero de texto con formato estructurado, registrando el instante de llegada y la ráfaga de CPU requerida. A partir de estos datos, simula el funcionamiento interno de tres algoritmos clásicos de planificación: FCFS (First-Come, First-Served), SJF (Shortest Job First sin desalojo) y Round Robin mediante un quantum configurable. Como salida por consola, genera el diagrama de Gantt detallado unidad a unidad, una tabla completa con las métricas de rendimiento de cada proceso —incluyendo instante de fin, tiempo de retorno, tiempo de espera y tiempo de respuesta—, las medias globales correspondientes, el número total de cambios de contexto y una traza opcional de las transiciones de estado de los procesos.

## 2. Cómo compilarlo y ejecutarlo

### Desde IntelliJ IDEA
Para compilar y ejecutar la aplicación desde el entorno de desarrollo IntelliJ IDEA, abra el proyecto seleccionando la carpeta raíz mediante la opción de apertura de directorios. Asegúrese de que el proyecto tiene configurada una versión de Java JDK 17 o superior en la estructura del proyecto. Si el directorio de código fuente no se reconoce automáticamente, haga clic derecho sobre la carpeta src y márquela como raíz de fuentes. A continuación, abra la clase principal situada en el paquete de planificación, configure los argumentos del programa en las opciones de ejecución con los parámetros deseados (por ejemplo, especificando la ruta del fichero de datos, el algoritmo y el quantum) y pulse el botón de ejecución.

### Desde la Terminal
También puede compilar y ejecutar el proyecto directamente desde la línea de comandos utilizando las herramientas estándar de Java. Para ello, compile los ficheros fuente ejecutando la compilación sobre los archivos del paquete y lance el simulador indicando la ruta del fichero CSV, el algoritmo deseado, el valor del quantum y opcionalmente el modificador de traza:

# Compilar las clases del proyecto
javac src/planificador/*.java

# Ejecutar el algoritmo FCFS con un fichero de datos
java -cp src planificador.Main datos/nocturno.csv fcfs

# Ejecutar Round Robin con quantum 2 y traza de estados activa
java -cp src planificador.Main datos/ejemplo_clase.csv rr 2 --traza

# Ejecutar todos los algoritmos secuencialmente sobre un conjunto de datos
java -cp src planificador.Main datos/nocturno.csv todos 4

## 3. Diseño
El diseño del software se ha estructurado en el paquete planificador dividiendo las responsabilidades de manera modular y limpia:

EstadoProceso.java: Define un tipo enumerado que modela los diferentes estados posibles en el ciclo de vida del proceso dentro del sistema operativo, abarcando desde la creación hasta su finalización.

Proceso.java: Representa la entidad individual de cada trabajo, almacenando sus propiedades esenciales como el nombre, el instante de llegada, la ráfaga total y el tiempo restante de CPU, además de calcular de forma dinámica las métricas de retorno, espera y respuesta.

LectorCSV.java: Se encarga de la lectura y parseo de los ficheros de datos, omitiendo automáticamente las líneas vacías y los comentarios iniciados por caracteres especiales, a la vez que realiza validaciones rigurosas para rechazar entradas mal formadas o valores numéricos inválidos como llegadas negativas o ráfagas nulas.

MotorSimulacion.java: Núcleo algorítmico que unifica el bucle temporal de simulación unidad a unidad. Evita la duplicación de código evaluando en un orden estricto las llegadas de procesos, las interrupciones por finalización o fin de quantum, la selección del siguiente proceso según el algoritmo activo y el avance de los contadores, cumpliendo estrictamente con las reglas de Round Robin relativas a la prioridad de llegada y los quantums sin procesos en espera.

VisualizadorResultados.java: Modela la presentación visual por consola estructurando el diagrama de Gantt, la tabla de métricas tabuladas con dos decimales de precisión en sus medias, el recuento de cambios de contexto y la traza detallada de cambios de estado.

Main.java: Actúa como el punto de entrada de la aplicación, procesando los argumentos de la línea de comandos e invocando las simulaciones correspondientes sin alterar la interfaz de ejecución exigida.

## 4. Verificación (tarea 4)

### 4.1 verificacion.csv resuelto a mano
El proceso de verificación se ha llevado a cabo resolviendo manualmente el fichero de pruebas de verificación antes de contrastarlo con la salida del programa.Resolución Manual de verificacion.csvFCFS: Al aplicarlo de forma secuencial, el diagrama de Gantt refleja que el proceso P1 se ejecuta de forma íntegra desde el instante 0 hasta el 6, seguido de P2 hasta el 10, P3 hasta el 12, P4 hasta el 17 y P5 hasta el 18. Esto genera un total de 4 cambios de contexto efectivos y una espera media de 6.40 unidades.Round Robin ($q=2$): Mediante la simulación manual paso a paso se controló rigurosamente la cola de listos en cada instante de decisión del planificador, teniendo en cuenta que al coincidir una nueva llegada con el agotamiento de un quantum, el proceso entrante se sitúa en la cola de listos por delante del proceso desalojado. El diagrama resultante avanza alternando los turnos de los procesos en bloques de dos unidades hasta completar sus ráfagas, arrojando 8 cambios de contexto y una espera media de 6.60 unidades.

<img width="480" height="640" alt="IMG_7231" src="https://github.com/user-attachments/assets/e6357108-489d-4e2c-904e-7e29ab995255" />

### 4.2 Comparación con el programa
[¿Coincide? Si no coincidió en algún momento: quién se equivocaba, qué corregiste y enlace al commit.]

### 4.3 hueco.csv
[Qué ocurre entre los instantes 2 y 5 y cómo lo refleja tu diagrama.]

## 5. Análisis y recomendación a NexoData (tarea 5)

| nocturno.csv | Retorno medio | Espera media | Respuesta media | Cambios de contexto |
FCFS	              5.33            5.33	          9.33	             5
SJF                 3.83            3.83            7.83               5
Round Robin         6.83            1.17            10.83              16
Round Robin         6.00            2.50            10.00              11
Round Robin         5.67            4.33            9.67               7

### Recomendación
Para la ejecución de los trabajos nocturnos por lotes en los servidores de NexoData, se recomienda utilizar el algoritmo SJF sin desalojo, ya que al tratarse de tareas automáticas sin usuarios interactivos esperando una respuesta visual inmediata, el objetivo primordial es maximizar la eficiencia global del sistema y reducir al mínimo la espera media acumulada
