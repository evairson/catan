package view.utilities;

import javax.swing.*;

public class Animation {

    public Animation() {
    }

    public void jPanelYUp(final int start, final int stop, final int delay,
                          final int increment, JPanel toAnimate) {
        if (toAnimate.getLocation().y != start) {
            return; // Si le panel n'est pas à la position de départ, on ne fait rien
        }

        new Thread(() -> {
            int currentPosition = start;
            while (currentPosition > stop) {
                currentPosition -= increment;
                if (currentPosition < stop) {
                    currentPosition = stop; // S'assurer de ne pas dépasser stop
                }

                // Mise à jour de la position dans l'EDT (Event Dispatch Thread)
                int finalCurrentPosition = currentPosition;
                SwingUtilities.invokeLater(() -> toAnimate.setLocation(toAnimate.getLocation().x,
                        finalCurrentPosition));

                try {
                    Thread.sleep(delay); // Attendre avant de répéter
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // Rétablir le statut d'interruption
                    System.out.println("Animation interrupted: " + e.getMessage());
                    return;
                }
            }
        }).start();
    }

    public void jPanelYDown(final int start, final int stop, final int delay, final int increment,
                            JPanel toAnimate) {
        if (toAnimate.getLocation().y != start) {
            return; // Si le panel n'est pas à la position de départ, on ne fait rien
        }

        new Thread(() -> {
            int currentPosition = start;
            while (currentPosition < stop) {
                currentPosition += increment;
                if (currentPosition > stop) {
                    currentPosition = stop; // S'assurer de ne pas dépasser stop
                }

                // Mise à jour de la position dans l'EDT (Event Dispatch Thread)
                int finalCurrentPosition = currentPosition;
                SwingUtilities.invokeLater(() -> toAnimate.setLocation(toAnimate.getLocation().x,
                        finalCurrentPosition));

                try {
                    Thread.sleep(delay); // Attendre avant de répéter
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // Rétablir le statut d'interruption
                    System.out.println("Animation interrupted: " + e.getMessage());
                    return;
                }
            }
        }).start();
    }

}
