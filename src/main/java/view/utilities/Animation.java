package view.utilities;

import model.geometry.Point;

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

    public void jPanelXRight(final int start, final int stop, final int delay,
                             final int increment, JPanel toAnimate) {
        if (toAnimate.getLocation().x != start) {
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
                SwingUtilities.invokeLater(() -> toAnimate.setLocation(finalCurrentPosition,
                        toAnimate.getLocation().y));

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

    public void jPanelXLeft(final int start, final int stop, final int delay,
                            final int increment, JPanel toAnimate) {
        if (toAnimate.getLocation().x != start) {
            return; // Si le panel n'est pas à la position de départ, on ne fait rien
        }

        new Thread(() -> {
            int currentPosition = start;
            while (currentPosition > stop) {
                currentPosition -= increment;
                if (currentPosition < stop) {
                    currentPosition = stop; // S'assurer de ne pas aller en dessous de stop
                }

                // Mise à jour de la position dans l'EDT (Event Dispatch Thread)
                int finalCurrentPosition = currentPosition;
                SwingUtilities.invokeLater(() -> toAnimate.setLocation(finalCurrentPosition,
                        toAnimate.getLocation().y));

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

    /**
     * Anime un JPanel le long d'une courbe de Bézier quadratique.
     *
     * @param p0         Le point de départ.
     * @param p1         Le point de contrôle.
     * @param p2         Le point d'arrivée.
     * @param delay      Le délai entre chaque étape de l'animation (en millisecondes).
     * @param steps      Le nombre d'étapes pour l'animation.
     * @param toAnimate  Le JPanel à animer.
     * @param onComplete Runnable à exécuter une fois l'anim completed :)
     */
    public void animateAlongBezierCurve(final Point p0, final Point p1, final Point p2,
                                        final int delay, final int steps,
                                        final JPanel toAnimate, Runnable onComplete) {
        new Thread(() -> {
            for (int i = 0; i <= steps; i++) {
                final double t = i / (double) steps;
                // Calcul de la position selon la courbe de Bézier quadratique
                int x = (int) ((1 - t) * (1 - t) * p0.getX()
                        + 2 * (1 - t) * t * p1.getX() + t * t * p2.getX());
                int y = (int) ((1 - t) * (1 - t) * p0.getY()
                        + 2 * (1 - t) * t * p1.getY() + t * t * p2.getY());

                // Mise à jour de la position du JPanel
                SwingUtilities.invokeLater(() -> toAnimate.setLocation(x, y));

                try {
                    Thread.sleep(delay);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.out.println("Animation interrupted: " + e.getMessage());
                    return;
                }
            }
            SwingUtilities.invokeLater(onComplete);
        }).start();
    }

}
