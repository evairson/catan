package exceptionclass;

public class ConstructBuildingException extends Exception {

    public static void messageError() {
        System.out.println("L'ID du building ne correspond à aucun ID du jeu");
    }
}
