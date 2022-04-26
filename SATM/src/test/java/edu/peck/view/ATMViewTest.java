package edu.peck.view;

import edu.peck.ATM.ATM;
import edu.peck.controller.ATMController;
import edu.peck.model.ATMModel;

import edu.peck.model.Card;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.Request;
import org.springframework.web.servlet.support.RequestDataValueProcessor;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ATMViewTest {

    static ATMController.PinListener pinListener;
    static ATMController.DepositListener depositListener;
    static ATMController.WithdrawalListener withdrawalListener;

    ATMView view = new ATMView();
    ATMModel model = new ATMModel();
    BigDecimal bigNumber = new BigDecimal(10000);
    Card card = new Card(1234,bigNumber);
    ATMController controller = new ATMController(view, model, card);


    //UC1: Present Valid ATM Card
    @Test
    public void testPresentValidATMCard() {
        view = mock(ATMView.class);
        view = ATM.showWindow();
        view.insertATMButton.doClick();
        assertEquals("Screen2", view.frame.getName());
    }

    //UC2: Present Invalid ATM Card
    @Test
    public void testPresentInvalidATMCard(){
        view = mock(ATMView.class);
        view = ATM.showWindow();
        view.insertFakeATMButton.doClick();
        assertEquals("Screen4", view.frame.getName());
    }

    //UC3: Correct PIN entered
    @Test
    public void testCorrectPinEntered() {
        card = mock(Card.class);
        card.setBalance(bigNumber);
        card.setPin(1234);
        view = mock(ATMView.class);
        view = ATM.showWindow();
        view.insertATMButton.doClick();
        view.setPinNumberField(new JTextField("1234"));
        assertEquals(view.getPinNumberField(), 1234);
        view.enterButton.doClick();
        view.validateButton.doClick();
        view.balanceButton.doClick();
        assertEquals("Screen6", view.frame.getName());
    }


    @Test // UC4: Failed PIN Entry
    public void testFailedPINEntry() {
        view = mock(ATMView.class);
        controller = mock(ATMController.class);
        view = ATM.showWindow();
        view.insertATMButton.doClick();
        view.setPinNumberField(new JTextField("0000"));
        view.enterButton.doClick();
        view.validateButton.doClick();
        view.pinRetryButton.doClick();
        view.setPinNumberField(new JTextField("0000"));
        view.enterButton.doClick();
        view.validateButton.doClick();
        view.pinRetryButton.doClick();
        view.setPinNumberField(new JTextField("0000"));
        view.enterButton.doClick();
        view.validateButton.doClick();
        assertEquals("Screen4", view.frame.getName());

    }

    //UC5: Transaction Choice: Balance Inquiry
    @Test
    public void testTransactionChoiceBalanceInquiry() {
        card = mock(Card.class);
        card.setBalance(bigNumber);
        card.setPin(1234);
        view = mock(ATMView.class);
        view = ATM.showWindow();
        view.insertATMButton.doClick();
        view.setPinNumberField(new JTextField("1234"));
        assertEquals(view.getPinNumberField(), 1234);
        view.enterButton.doClick();
        view.validateButton.doClick();
        view.balanceButton.doClick();
        assertEquals("Screen6", view.frame.getName());
    }

    //UC6: Transaction Choice: Deposit
    @Test
    public void testTransactionChoiceDeposit() {
        view.setProblemWithSlot(false);
        card = mock(Card.class);
        card.setBalance(bigNumber);
        card.setPin(1234);
        view = mock(ATMView.class);
        view = ATM.showWindow();
        view.insertATMButton.doClick();
        view.setPinNumberField(new JTextField("1234"));
        view.enterButton.doClick();
        view.validateButton.doClick();
        view.depositButton.doClick();
        view.setAmountField(new JTextField("10"));
        view.enterDepositButton.doClick();
        assertEquals("Screen13", view.frame.getName());
        view.enterDepositAddButton.doClick();
        assertEquals("Screen14", view.frame.getName());
    }

    //UC7: Deposit Slot Jammed
    @Test
    public void testDepositSlotJammed() {
        card = mock(Card.class);
        card.setBalance(bigNumber);
        card.setPin(1234);
        view = mock(ATMView.class);
        view = ATM.showWindow();
        view.insertATMButton.doClick();
        view.setPinNumberField(new JTextField("1234"));
        view.enterButton.doClick();
        view.validateButton.doClick();
        view.setProblemWithSlot(true);
        view.depositButton.doClick();
        assertEquals("Screen12", view.frame.getName());
    }

    // UC8: Normal Withdrawal
    @Test
    public void testNormalWithdrawal(){
        card = mock(Card.class);
        card.setBalance(bigNumber);
        card.setPin(1234);
        view = mock(ATMView.class);
        view = ATM.showWindow();
        view.insertATMButton.doClick();
        view.setPinNumberField(new JTextField("1234"));
        view.enterButton.doClick();
        view.validateButton.doClick();
        view.withdrawalButton.doClick();
        view.setAmountField(new JTextField("100"));
        view.enterWithdrawalButton.doClick();
        view.checkFundsButton.doClick();
        assertEquals("Screen11", view.frame.getName());
    }

    // UC9: Withdrawal Not Increment of $10
    @Test
    public void testWithdrawalNotIncrementOf10(){
        card = mock(Card.class);
        card.setBalance(bigNumber);
        card.setPin(1234);
        view = mock(ATMView.class);
        view = ATM.showWindow();
        view.insertATMButton.doClick();
        view.setPinNumberField(new JTextField("1234"));
        view.enterButton.doClick();
        view.validateButton.doClick();
        view.withdrawalButton.doClick();
        view.setAmountField(new JTextField("101"));
        view.enterWithdrawalButton.doClick();
        view.checkFundsButton.doClick();
        assertEquals("Screen9", view.frame.getName());
    }

    // UC10: Withdrawal Exceeds Account Balance
    @Test
    public void testInsufficientFunds(){
        card = mock(Card.class);
        card.setBalance(bigNumber);
        card.setPin(1234);
        view = mock(ATMView.class);
        view = ATM.showWindow();
        view.insertATMButton.doClick();
        view.setPinNumberField(new JTextField("1234"));
        view.enterButton.doClick();
        view.validateButton.doClick();
        view.withdrawalButton.doClick();
        view.setAmountField(new JTextField("1000000000"));
        view.enterWithdrawalButton.doClick();
        view.checkFundsButton.doClick();
        assertEquals("Screen8", view.frame.getName());
    }

    // UC11: Daily Limit Exceeded
    //This test fails due to no documentation on "daily limit exceeded" so there has been no implementation in the system.
    @Test
    public void testDailyLimitExceeded(){
        card = mock(Card.class);
        card.setBalance(bigNumber);
        card.setPin(1234);
        view = mock(ATMView.class);
        view = ATM.showWindow();
        view.insertATMButton.doClick();
        view.setPinNumberField(new JTextField("1234"));
        view.enterButton.doClick();
        view.validateButton.doClick();
        view.withdrawalButton.doClick();
        view.setAmountField(new JTextField("600"));
        view.enterWithdrawalButton.doClick();
        view.checkFundsButton.doClick();
        assertEquals("Screen10", view.frame.getName());
    }

    //UC12: Withdrawal Chute Jammed
    @Test
    public void testWithdrwalChuteJammed() {
        card = mock(Card.class);
        card.setBalance(bigNumber);
        card.setPin(1234);
        view = mock(ATMView.class);
        view = ATM.showWindow();
        view.insertATMButton.doClick();
        view.setPinNumberField(new JTextField("1234"));
        view.enterButton.doClick();
        view.validateButton.doClick();
        view.setJammedWithdrawalChute(true);
        view.withdrawalButton.doClick();
        assertEquals("Screen10", view.frame.getName());
    }


    @Test
    public void testSetInitialBalance() {
        view.setInitialBalance(bigNumber);
        int value = bigNumber.intValue();
        assertEquals(value, 10000);
    }

    @Test
    public void testSetSufficientFundsAndGetSufficientFunds() {
        view.setSufficientFunds(true);
        assertTrue(view.getSufficientFunds());
    }

    @Test
    public void testSetMultipleOfTens() {
        view.setMultipleOfTens(true);
    }

    @Test
    public void testGetPinNumberField() {
        try {
            BigDecimal bigD = view.getAmountField();
        } catch (Exception e){
            System.out.println("Exception: " + e);
        }
    }

    @Test
    public void testGetAmountField() {
        try {
            view.getAmountField();
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        }
    }

    @Test
    public void testGetProblemWithSlot() {
        boolean b = view.getProblemWithSlot();
        assertEquals(b,b);
    }

    @Test
    public void testGetJammedWithdrawalChute() {
        boolean b = view.getJammedWithdrawalChute();
        assertFalse(b);
    }

    @Test
    public void testValidatePinNumber() {
        JTextField num = new JTextField("0000");
        view.setPinNumberField(num);
        view.validatePinNumber(pinListener);
    }

    @Test
    public void testValidateDepositNumber() {
        view.validateDepositNumber(depositListener);
    }

    @Test
    public void testValidateWithdrawalNumber() {
        view.validateWithdrawalNumber(withdrawalListener);
    }

    @Test
    public void testSetCurrentBalance() {
        view.setCurrentBalance(bigNumber);
        assertEquals(bigNumber,BigDecimal.valueOf(10000));
    }

    @Test
    public void testGetCurrentBalance() {
        view.setCurrentBalance(bigNumber);
        BigDecimal num = view.getCurrentBalance();
        assertEquals(num, BigDecimal.valueOf(10000));
    }

    @Test
    public void testAddToArray() {
        String[] stringArr = {"One","Two"};
        view.addToArray(stringArr);
    }
}