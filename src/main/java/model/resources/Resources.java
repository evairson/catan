package model.resources;

public abstract class Resources {
    private int amount;

    public Resources(int amount) {
        this.amount = amount;
    }
    public Resources(){
        this.amount = 0;
    }

    /**
     * Adds an amount to the current amount of the resource
     *
     * @param addAmount the amount added
     * @return true if the operation is successful
     */
    public boolean addAmount(int addAmount){
        //A voir si on veut mettre une limite au stacking de ressources
        this.amount += addAmount;
        return true;
    }

    /**
     * Removes an amount to the current amount of the resource
     *
     * @param payAmount the amount removed
     * @return true if the original amount is enough, false if the operation would put it in the negative
     */
    public boolean payAmount(int payAmount){
        if(payAmount > this.amount){
            return false;
        }
        this.amount -= payAmount;
        return true;
    }

    /**
     * Same as the payAmount function, but ignoring the potential debt of the player and putting it if needed to 0
     *
     * @param payAmount the amount removed
     */
    public void hardPay(int payAmount){
        if(!payAmount(payAmount)){
            this.amount = 0;
        }
    }

    /**
     * Same as debtPay function but creates a potential debt
     *
     * @param payAmount the amount removed once again
     */
    public void debtPay(int payAmount){
        this.amount -= payAmount;
    }

    /**
     * Halves the current amount, rouding it to the upper integer if needed
     */
    public void divide2(){
        if(this.amount % 2 == 0){
            this.amount /= 2;
        }
        else{
            this.amount /= 2;
            this.amount -= 1; //Division par 2 à l'entier supérieur
        }
    }

    public int getAmount() {
        return amount;
    }
}
