package edu.peck.ATM;

import edu.peck.controller.ATMController;
import edu.peck.model.ATMModel;
import edu.peck.model.Card;
import edu.peck.view.ATMView;

import java.math.BigDecimal;

public class ATM {

    public static void main(String[] args) {
        showWindow();
    }

    public static ATMView showWindow() {
        ATMView atmView = new ATMView();
        ATMModel atmModel = new ATMModel();
        Card card = new Card(1234,  BigDecimal.valueOf(1000));
        ATMController atmController = new ATMController(atmView, atmModel, card);

        atmView.frame.setVisible(true);
        return atmView;
    }
}
