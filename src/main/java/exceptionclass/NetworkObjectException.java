package exceptionclass;

public class NetworkObjectException extends Exception {

    public static void messageError() {
        System.out.println("Le network objet ne répond à aucun des messages du playerClient");
    }
}
