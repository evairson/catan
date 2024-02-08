package model.tiles;

public class Tile{
    private int q;
    private int r;

    public Tile(int q, int r){
        this.q = q;
        this.r = r;
    }

    public int getQ(){
        return q;
    }
    
    public int getR(){
        return r;
    }

    public boolean equals(Tile t){
        return (t.getQ() == q && t.getR() == r);
    }

    public String toString(){
        return "(" + q + ", " + r + ")";
    }
    
}