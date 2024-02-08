// import javax.swing.*;

// public class Main {

//     public static void main(String[] args) {
//         SwingUtilities.invokeLater(() -> {
//             System.out.println("launching game");
//             Game game = new Game();
//         });

//         GameBoard board=new GameBoard();
//         System.out.println(board);
//     }
// }
import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Créer un Layout avec votre orientation, origine et taille préférées
            Layout layout = new Layout(Orientation.POINTY, new Point(400, 400), new Point(50, 50));

            // Créer un GameBoard avec le Layout
            GameBoard gameBoard = new GameBoard(layout);

            // Initialiser le tableau de jeu
            gameBoard.initialiseBoard();

            JFrame frame = new JFrame("Test Game Board");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 800);
            frame.setLocationRelativeTo(null);

            // Créer un JPanel personnalisé pour dessiner le jeu
            JPanel panel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    drawGameBoard(g, gameBoard);
                    drawEdges(g, gameBoard);
                }
            };

            frame.add(panel);
            frame.setVisible(true);
        });
    }

    private static void drawGameBoard(Graphics g, GameBoard gameBoard) {
        Graphics2D g2d = (Graphics2D) g;

        // Dessiner le contenu du tableau de jeu (hexagones, etc.)
        // Par exemple :
        // g2d.setColor(Color.BLUE);
        // for (Tile tile : gameBoard.getBoard().values()) {
        // // Dessiner chaque tuile
        // }

        // Exemple :
        // for (Map.Entry<CubeCoordinates, Tile> entry :
        // gameBoard.getBoard().entrySet()) {
        // CubeCoordinates cubeCoord = entry.getKey();
        // Tile tile = entry.getValue();
        // // Dessiner la tuile avec ses coordonnées cubeCoord et ses attributs depuis
        // l'objet tile
        // }
    }

    private static void drawEdges(Graphics g, GameBoard gameBoard) {
        // Dessiner les arêtes du tableau de jeu
        gameBoard.drawEdges(g);
    }
}
