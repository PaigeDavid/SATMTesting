package edu.peck.model;

import java.math.BigDecimal;

public class Card {
    private int pin;
    private BigDecimal balance;

    public Card (int pin, BigDecimal balance){
        this.pin = pin;
        this.balance = balance;
    }

    public int getPin() {
        return pin;
    }

    public void setPin(int pin) {
        this.pin = pin;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
