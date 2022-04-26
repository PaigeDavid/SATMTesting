package edu.peck.controller;

import edu.peck.model.ATMModel;
import edu.peck.model.Card;
import edu.peck.view.ATMView;

import java.math.BigDecimal;

import org.junit.Test;

import javax.swing.*;

import static org.junit.Assert.*;

public class ATMControllerTest {
    ATMController controller;
    ATMView view;
    ATMModel model;
    Card card;

    @Test
    public void testConstructorBaseValues() {
        try {
            view = new ATMView();
            model = new ATMModel();
            card = new Card(1234, BigDecimal.valueOf(1000));
            controller = new ATMController(view, model, card);
        } catch (Exception e) {
            fail(e.getMessage());
        }

    }

    @Test
    public void testSetWithdrawalVariablesMultipleAndSufficient() {
        view = new ATMView();
        model = new ATMModel();
        card = new Card(1234, BigDecimal.valueOf(1000));
        JTextField amount = new JTextField("100");
        view.setAmountField(amount);
        model.setWithdrawalAmount(new BigDecimal(amount.getText()));
        model.setCurrentBalance(card.getBalance());
        model.setTotalCurrencyToDispense(BigDecimal.valueOf(1000));
        controller = new ATMController(view, model, card);

        controller.setWithdrawalVariables();
        assertTrue(view.getMultipleOfTens());
        assertTrue(view.getSufficientFunds());
    }

    @Test
    public void testSetWithdrawalVariablesNotMultiple() {
        view = new ATMView();
        model = new ATMModel();
        card = new Card(1234, BigDecimal.valueOf(1000));
        JTextField amount = new JTextField("5");
        view.setAmountField(amount);
        model.setWithdrawalAmount(new BigDecimal(amount.getText()));
        model.setCurrentBalance(card.getBalance());
        model.setTotalCurrencyToDispense(BigDecimal.valueOf(1000));
        controller = new ATMController(view, model, card);

        controller.setWithdrawalVariables();
        assertFalse(view.getMultipleOfTens());
    }

    @Test
    public void testSetWithdrawalVariablesMultipleNotSufficient() {
        view = new ATMView();
        model = new ATMModel();
        card = new Card(1234, BigDecimal.valueOf(100));
        JTextField amount = new JTextField("1000");
        view.setAmountField(amount);
        model.setWithdrawalAmount(new BigDecimal(amount.getText()));
        model.setCurrentBalance(card.getBalance());
        model.setTotalCurrencyToDispense(BigDecimal.valueOf(1000));
        controller = new ATMController(view, model, card);

        controller.setWithdrawalVariables();
        assertTrue(view.getMultipleOfTens());
        assertFalse(view.getSufficientFunds());
    }

    @Test
    public void testSetWithdrawalVariablesNotMultipleNotSufficient() {
        view = new ATMView();
        model = new ATMModel();
        card = new Card(1234, BigDecimal.valueOf(100));
        JTextField amount = new JTextField("1005");
        view.setAmountField(amount);
        model.setWithdrawalAmount(new BigDecimal(amount.getText()));
        model.setCurrentBalance(card.getBalance());
        model.setTotalCurrencyToDispense(BigDecimal.valueOf(1000));
        controller = new ATMController(view, model, card);

        controller.setWithdrawalVariables();
        assertFalse(view.getMultipleOfTens());
        assertFalse(view.getSufficientFunds());
    }

}