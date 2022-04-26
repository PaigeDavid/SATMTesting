package edu.peck.model;

import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ATMModelTest {
    ATMModel model = new ATMModel();
    Card card = new Card(1234, new BigDecimal("1000"));

    @Test
    public void testIsValidPin() {
        model.setValidPin(true);
        assertTrue(model.isValidPin());
    }

    @Test
    public void testValidatePinNumberValid() {
        model.validatePinNumber(card, 1234);
        assertTrue(model.isValidPin());
    }

    @Test
    public void testValidatePinNumberInvalid() {
        model.validatePinNumber(card, 6353);
        assertFalse(model.isValidPin());
    }

    @Test
    public void hasSufficientFunds() {
        model.setCurrentBalance(new BigDecimal("100"));
        assertTrue(model.hasSufficientFunds(new BigDecimal("10")));
    }

    @Test
    public void hasINSufficientFunds() {
        model.setCurrentBalance(new BigDecimal("100"));
        assertFalse(model.hasSufficientFunds(new BigDecimal("1000")));
    }

    @Test
    public void checkMultiplesOfTens() {
        assertTrue(model.checkMultiplesOfTens(new BigDecimal("100")));
    }

    @Test
    public void checkNotMultiplesOfTens() {
        assertFalse(model.checkMultiplesOfTens(new BigDecimal("5")));
    }

}
