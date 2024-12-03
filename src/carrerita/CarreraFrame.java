package carrerita;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

// Clase principal que extiende JFrame
public class CarreraFrame extends JFrame {

    public static boolean winnerReported = false; // La usaremos para controlar si ya se reportó el ganador y parar la carrera
    private static final Map<String, Integer> distances = new HashMap<>(); // Para almacenar las distancias finales

    // Constantes para definir la configuración de la pista y los coches
    private static final int CIRCUIT_LENGTH = 800; // Longitud de la pista en píxeles
    private static final int CAR_WIDTH = 100; // Ancho de los coches
    private static final int CAR_HEIGHT = 50; // Altura de los coches
    private static final int GAP = 80; // Espacio vertical entre coches
    private static final int MARGIN = 50; // Espacio adicional alrededor de la ventana

    // Componentes para la interfaz gráfica
    private JLabel car1Label;
    private JLabel car2Label;
    private JLabel car3Label;
    private JLabel car4Label;

    private RacePanel racePanel; // Panel personalizado para mostrar los rastros de los coches
    private JLabel backgroundLabel; // Fondo de la ventana

    public CarreraFrame() {
        setTitle("Carrerita con hilos");

        // Variables de ancho y alto de la ventana en función de la longitud de la pista
        int windowWidth = CIRCUIT_LENGTH + MARGIN;
        int windowHeight = (4 * GAP) + MARGIN;

        // Configuración de la ventana principal con las variables anteriores
        setSize(windowWidth, windowHeight);
        setLayout(null); // Ponemos el setLayout en null para poder posicionar manualmente los elementos de la ventana
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Cerrar la aplicación al cerrar la ventana
        setResizable(false); // Aqui bloqueamos el redimensionamiento de la ventana

        // Variable Jlabel con la imagen de fondo y del mismo tamaño de la ventana
        backgroundLabel = new JLabel(loadImage("/carrerita/background.jpg", windowWidth, windowHeight));
        backgroundLabel.setBounds(0, 0, windowWidth, windowHeight); // Con setBounds posicionamos el fondo en la ventana
        add(backgroundLabel); // finalmente, con add lo añadimos a la ventana

        // Crear el panel para los rastros y hacerlo transparente para mostrar el fondo
        racePanel = new RacePanel();
        racePanel.setBounds(0, 0, CIRCUIT_LENGTH, windowHeight);
        backgroundLabel.add(racePanel); // Agregar el panel al fondo

        // Creamos variables Jlabels con las imagenes de los coches
        car1Label = new JLabel(loadImage("/carrerita/car.png", CAR_WIDTH, CAR_HEIGHT));
        car2Label = new JLabel(loadImage("/carrerita/car.png", CAR_WIDTH, CAR_HEIGHT));
        car3Label = new JLabel(loadImage("/carrerita/car.png", CAR_WIDTH, CAR_HEIGHT));
        car4Label = new JLabel(loadImage("/carrerita/car.png", CAR_WIDTH, CAR_HEIGHT));

        // Posición inicial de los coches
        car1Label.setBounds(0, 30, CAR_WIDTH, CAR_HEIGHT);
        car2Label.setBounds(0, 30 + GAP, CAR_WIDTH, CAR_HEIGHT);
        car3Label.setBounds(0, 30 + 2 * GAP, CAR_WIDTH, CAR_HEIGHT);
        car4Label.setBounds(0, 30 + 3 * GAP, CAR_WIDTH, CAR_HEIGHT);

        // Agregar las etiquetas de los coches al fondo
        backgroundLabel.add(car1Label);
        backgroundLabel.add(car2Label);
        backgroundLabel.add(car3Label);
        backgroundLabel.add(car4Label);

        // Crear instancias de la clase Car para representar cada coche con su respectiva lógica
        Car car1 = new Car("Coche 1", car1Label, CIRCUIT_LENGTH, racePanel, 30);
        Car car2 = new Car("Coche 2", car2Label, CIRCUIT_LENGTH, racePanel, 30 + GAP);
        Car car3 = new Car("Coche 3", car3Label, CIRCUIT_LENGTH, racePanel, 30 + 2 * GAP);
        Car car4 = new Car("Coche 4", car4Label, CIRCUIT_LENGTH, racePanel, 30 + 3 * GAP);

        // Iniciar los hilos para cada coche (cada coche avanza en su propio hilo)
        new Thread(car1).start();
        new Thread(car2).start();
        new Thread(car3).start();
        new Thread(car4).start();
    }

    // Método para cargar una imagen desde el archivo especificado y redimensionarla
    private ImageIcon loadImage(String path, int width, int height) {
        ImageIcon icon = new ImageIcon(getClass().getResource(path)); // Cargar la imagen desde el recurso
        return new ImageIcon(icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH)); // Redimensionar la imagen
    }

    public static synchronized void reportDistances(String carName, int distance) {
        distances.put(carName, distance); // Guardar distancia

        if (distances.size() == 4) { // Cuando todos los coches han registrado su distancia
            StringBuilder result = new StringBuilder("Distancias finales:\n");
            distances.forEach((name, dist) -> result.append(name).append(": ").append(dist).append(" metros\n"));
            JOptionPane.showMessageDialog(null, result.toString());
        }
    }

    // Método que agregará un punto por cada posición recorrida por cada coche, pilla por parametro el indice del coche y las coordenadas
    public void addTrail(int carIndex, int x, int y) {
        List<Point> trail = trails.get(carIndex);
        List<Color> colors = trailColors.get(carIndex);

        trail.add(new Point(x, y)); // Agregamos un nuevo punto al rastro
        float hue = (float) Math.random(); // Generar un color aleatorio
        colors.add(Color.getHSBColor(hue, 1.0f, 1.0f)); // Aquí se lo asignamos a un punto

        if (trail.size() > 100) { // Limitamos el tamaño del rastro eliminando los puntos antiguos
            trail.remove(0);
            colors.remove(0);
        }

        repaint(); // Actualizar el panel para mostrar los cambios
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // Llamada a la superclase paintComponent de Jpanel ya existente en javax.swing y como parametro se le pasa "g", el objeto de la clase Graphics de java.awt que le pasamos por parametro a la declaración del método
        Graphics2D g2d = (Graphics2D) g; // Casteamos el objeto Graphics a Graphics2D, la cual es una subclase de Graphics
        g2d.setStroke(new BasicStroke(3)); // Configuramos el grosor del ratro con setStroke

        for (int i = 0; i < trails.size(); i++) { // Iteramos sobre las listas de rastro y color de rastro de cada coche
            List<Point> trail = trails.get(i);
            List<Color> colors = trailColors.get(i);

            for (int j = 1; j < trail.size(); j++) { // Iteramos sobre cada uno de los puntos e cada rastro
                Point p1 = trail.get(j - 1); // Guardamos un punto
                Point p2 = trail.get(j); // Guardamos el punto anterior
                g2d.setColor(colors.get(j)); // Usar el color correspondiente a cada segmento
                g2d.drawLine(p1.x, p1.y, p2.x, p2.y); // Dibujar la línea del rastro dibujando una linea entre cada punto
            }
        }
    }
}


}