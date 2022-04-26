package edu.peck.controller;


import edu.peck.model.ATMModel;
import edu.peck.model.Card;
import edu.peck.view.ATMView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;

public class ATMController {
    public static Card card;
    private final ATMView atmView;
    private final ATMModel atmModel;
    public static boolean dividedByTen = true;
    //public static boolean sufficientFunds = true;

    public ATMController(ATMView atmView, ATMModel atmModel, Card card) {
        this.atmView = atmView;
        this.atmModel = atmModel;
        this.card = card;

        this.atmView.validatePinNumber(new PinListener());
        this.atmView.retryPinEntry(new PinRetryListener());
        this.atmView.validateDepositNumber(new DepositListener());
        this.atmView.validateWithdrawalNumber(new WithdrawalListener());
    }

    public class PinListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent arg0) {
            try{
                // Sets atmModel validPin to true or false
                atmModel.validatePinNumber(card, atmView.getPinNumberField());
                if(atmModel.getIncorrectPinEntries() < 2) {
                    if(atmModel.isValidPin()){
                        atmView.setValidPin(atmModel.isValidPin());
                        atmView.setCurrentBalance(atmModel.getCurrentBalance());
                        atmView.setInitialBalance(atmModel.getCurrentBalance());
                    }
                } else {
                    atmView.setTooManyInvalidPinEntries(true);
                }

            } catch(NumberFormatException ex) {
                atmView.displayErrorMessage("You need to enter One integer", "ScreenIntegerError");
            }
        }
    }

    public class PinRetryListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent arg0) {
            atmModel.addIncorrectPinEntry();
        }
    }

    public class DepositListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent arg0) {
            try{
                atmModel.setDepositAmount(atmView.getAmountField());
                BigDecimal depositAmount = atmModel.getDepositAmount();
                atmModel.addDepositToBalance(depositAmount);
                atmView.setCurrentBalance(atmModel.getCurrentBalance());

                String[] transaction = new String[2];
                transaction[0] = "Deposit: ";
                transaction[1] = atmView.getAmountField().toString();

                atmView.addToArray(transaction);
            } catch(NumberFormatException ex) {
                atmView.displayErrorMessage("You need to enter a dollar value", "ScreenDollarError");
            }
        }
    }

    public class WithdrawalListener implements ActionListener {
        public void actionPerformed(ActionEvent arg0) {
            setWithdrawalVariables();
            try {
                //String print;
                atmModel.setWithdrawalAmount(atmView.getAmountField());
                boolean multipleOfTens = atmModel.checkMultiplesOfTens(atmModel.getWithdrawalAmount());
                dividedByTen = multipleOfTens;
                atmModel.setMultipleOfTen(multipleOfTens);

                /*result == 1 if Current Balance is greater , 0 if they're equal , -1 if Withdrawal is great*/
                int result = atmModel.getCurrentBalance().compareTo(atmModel.getWithdrawalAmount());

                BigDecimal funds = atmView.getAmountField();
                boolean sufficientFunds = atmModel.hasSufficientFunds(funds);
                atmModel.setSufficientFunds(sufficientFunds);
                if(atmModel.validateCashInMachine(atmModel.getWithdrawalAmount())) {
                    atmView.setEnoughFundsInMachine(true);
                    if (multipleOfTens && result >= 0) {
                        String[] transaction = new String[2];
                        transaction[0] = "Withdrawal: ";
                        transaction[1] = atmView.getAmountField().toString();
                        atmView.addToArray(transaction);

                        atmModel.subtractWithdrawalFromBalance(atmModel.getWithdrawalAmount());
                        atmView.setCurrentBalance(atmModel.getCurrentBalance());
                        atmModel.updateTotalCurrencyToDispense(atmModel.getWithdrawalAmount());
                    }
                } else {
                    atmView.setEnoughFundsInMachine(false);
                }

            } catch (NumberFormatException ex) {
                atmView.displayErrorMessage("You need to enter a dollar value", "ScreenDollarValueError");
            }

        }
    }

    public void setWithdrawalVariables(){
        // Multiple of 10
        atmModel.setWithdrawalAmount(atmView.getAmountField());
        boolean multipleOfTens = atmModel.checkMultiplesOfTens(atmModel.getWithdrawalAmount());
        dividedByTen = multipleOfTens;
        atmModel.setMultipleOfTen(multipleOfTens);
        atmView.setMultipleOfTens(multipleOfTens);

        // Sufficient Funds
        boolean sufficientFunds = atmModel.hasSufficientFunds(atmView.getAmountField());
        atmModel.setSufficientFunds(sufficientFunds);
        atmView.setSufficientFunds(sufficientFunds);
    }



}
