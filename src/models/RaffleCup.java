package models;

public class RaffleCup {
    private Die[] dice = new Die[5];

    public RaffleCup() {
        //TODO: Create an instance of RaffleCup.
        for (int i = 0; i < dice.length; i++) {
            dice[i] = new Die();
        }
    }

    public void throwDice() {
        //TODO: implement throwDice method.
        for (Die die : dice) {
            die.roll();
        }
    }

    public Die[] getDice() {
        return dice;
    }
}
