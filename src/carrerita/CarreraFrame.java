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

}