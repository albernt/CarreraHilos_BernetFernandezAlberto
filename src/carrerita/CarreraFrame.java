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


}