package edu.peck.model;

import java.math.BigDecimal;

public class ATMModel {

    private boolean validPin;
    private boolean sufficientFunds;
    private boolean multipleOfTen;

    private BigDecimal currentBalance;
    private BigDecimal depositAmount = BigDecimal.ZERO;
    private BigDecimal withdrawalAmount = BigDecimal.ZERO;
    private BigDecimal totalCurrencyToDispense = BigDecimal.valueOf(10000);

    private int incorrectPinEntries = 0;

    public void validatePinNumber(Card card, int pinNumber) {
        int validPinNum = card.getPin();
        validPin = pinNumber == validPinNum;
        setValidPin(validPin);
        if(validPin) {
            setCurrentBalance(card.getBalance());
        }
    }

    public void addIncorrectPinEntry() {
        incorrectPinEntries++;
    }

    public boolean hasSufficientFunds(BigDecimal withdrawalAmount){
        return getCurrentBalance().compareTo(withdrawalAmount) >= 0;
    }

    public boolean isValidPin() {
        return validPin;
    }

    public void setValidPin(boolean validPin) {
        this.validPin = validPin;
    }

    public BigDecimal getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(BigDecimal currentBalance) {
        this.currentBalance = currentBalance;
    }

    public void addDepositToBalance(BigDecimal depositNumber) {
        currentBalance = currentBalance.add(depositNumber);
    }

    public BigDecimal getDepositAmount() {
        return depositAmount;
    }

    public void setDepositAmount(BigDecimal depositAmount) {
        this.depositAmount = depositAmount;
    }

    public BigDecimal getWithdrawalAmount() {
        return withdrawalAmount;
    }

    public void setWithdrawalAmount(BigDecimal withdrawalAmount) {
        this.withdrawalAmount = withdrawalAmount;
    }

    public void subtractWithdrawalFromBalance(BigDecimal withdrawalAmount){ currentBalance = currentBalance.subtract(withdrawalAmount);}

    public void setTotalCurrencyToDispense(BigDecimal totalCurrencyToDispense) {
        this.totalCurrencyToDispense = totalCurrencyToDispense;
    }

    public void updateTotalCurrencyToDispense(BigDecimal withdrawnAmount) {
        this.totalCurrencyToDispense = this.totalCurrencyToDispense.subtract(withdrawnAmount);
    }

    public boolean validateCashInMachine(BigDecimal withdrawalAmount) {
        return withdrawalAmount.compareTo(totalCurrencyToDispense) <= 0;
    }

    public boolean checkMultiplesOfTens(BigDecimal withdrawalAmount) {
        String value = withdrawalAmount.toString();
        int intValue = Integer.parseInt(value);
        return intValue % 10 == 0;
    }

    public void setSufficientFunds(boolean bool){
        sufficientFunds = bool;
    }

    public void setMultipleOfTen(boolean multipleOfTen) {
        this.multipleOfTen = multipleOfTen;
    }

    public int getIncorrectPinEntries() {
        return incorrectPinEntries;
    }

    public void setIncorrectPinEntries(int incorrectPinEntries) {
        this.incorrectPinEntries = incorrectPinEntries;
    }
}

