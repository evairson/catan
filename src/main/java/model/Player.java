package model;

import model.buildings.Building;
import model.buildings.Colony;
import model.resources.Resources;

import java.lang.Math;
import java.time.format.ResolverStyle;
import java.util.ArrayList;

public class Player {

    public enum Color {
        RED,
        WHITE,
        BLUE,
        ORANGE
    }

    private Color color;
    private Boolean turn;
    private int dice1;
    private int dice2;
    private String nom;
    // private Coordonnee cord;
    private ArrayList<Resources> resources;
    // private ArrayList<Card> cardsDev;
    private ArrayList<Building> buildings;

// Getter / Setter :  ---------------

    public Color getColor(){
        return color;
    }
    public void setColor(Color c){
        color = c;
    }

    public Boolean isTurn(){
        return turn;
    }

    public void setTurn(Boolean b){
        turn = b;
    }

    public int getDies(){
        return dice1 + dice2;
    }

    public ArrayList<Building> getBuildings(){
        return buildings;
    }

    public ArrayList<Resources> getResources(){
        return resources;
    }

// ------------------------------------

    public void throwDice1(){
        dice1 = (int)(Math.random()*7); // (max-min+1)*min
    }

    public void throwDice2(){
        dice2 = (int)(Math.random()*7);
    }

    public void throwDices(){
        throwDice1();
        throwDice2();
    }



}