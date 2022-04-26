package edu.peck.view;

import edu.peck.controller.ATMController;
import edu.peck.model.Card;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;

public class ATMView extends JFrame implements ActionListener {
    public final JButton insertATMButton = new JButton("Insert ATM Card");
    public final JButton insertFakeATMButton = new JButton("Insert Fake ATM Card");
    public final JButton pinRetryButton = new JButton("Retry");
    public JFrame frame = new JFrame();
    private boolean isValidPin;
    private boolean sufficientFunds;
    private boolean enoughFundsInMachine;
    public static JPanel previousPanel;
    private JTextField pinNumberField = new JTextField(10);
    private JTextField amountField = new JTextField(10);
    public final JButton enterButton = new JButton("Enter");
    public final JButton validateButton = new JButton("Validate");
    public final JButton checkFundsButton = new JButton("Check funds");
    JFrame validateFrame = new JFrame();
    JFrame fundFrame = new JFrame();
    public final JButton enterDepositButton = new JButton("Enter Deposit");
    public final JButton enterDepositAddButton = new JButton("Add deposit envelope");
    public final JButton enterWithdrawalButton = new JButton("Enter Withdrawal");
    public final JButton balanceButton = new JButton("Balance");
    public final JButton depositButton = new JButton("Deposit");
    public final JButton withdrawalButton = new JButton("Withdrawal");
    public final JButton backButton = new JButton("Back");
    public final JButton yesButton = new JButton("Yes");
    public final JButton noButton = new JButton("No");

    private BigDecimal currentBalance;
    private boolean tooManyInvalidPinEntries = false;
    private boolean problemWithSlot = false;
    private boolean jammedWithdrawalChute = false;
    public void setInitialBalance(BigDecimal initialBalance) {
        this.initialBalance = initialBalance;
    }

    private BigDecimal initialBalance;
    private boolean multipleOfTens;
    private final Card card = ATMController.card;
    private final ArrayList<String[]> TransactionTracker = new ArrayList<>();

    public ATMView() {
        JPanel atmWindow = new JPanel();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 300);

        createWelcomeScreen(atmWindow); //Screen 1
    }

    //Screen 1
    private void createWelcomeScreen(JPanel atmWindow) {
        JLabel welcomeLabel = new JLabel("<html>Welcome to<br/><br/>" +
                "Rock Solid Federal Credit Union<br/><br/>" +
                "Please Insert your ATM card<br/></html>");

        welcomeLabel.setHorizontalAlignment(JLabel.CENTER);
        welcomeLabel.setVerticalAlignment(JLabel.CENTER);
        atmWindow.add(welcomeLabel, BorderLayout.CENTER);
        atmWindow.add(insertATMButton);
        atmWindow.add(insertFakeATMButton);
        insertATMButton.addActionListener(this);
        insertFakeATMButton.addActionListener(this);
        frame.add(atmWindow);
    }

    private JFrame createNewWindow(JPanel newPanel, String newLabel, String name) {
        frame = new JFrame();
        JLabel label = new JLabel(newLabel);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 300);
        frame.setVisible(true);
        frame.setName(name);

        newPanel.add(label);

        return frame;
    }

    public void actionPerformed(ActionEvent e) {
        // Back Button
        if (e.getSource() == backButton) {
            frame.dispose();

            JFrame newFrame = createNewWindow(previousPanel, "", "Back");
            newFrame.add(previousPanel);
        }

        //Screen 2
        if (e.getSource() == insertATMButton || e.getSource() == pinRetryButton) {
            frame.dispose();

            JPanel newPanel = new JPanel();
            JFrame newFrame = createNewWindow(newPanel, "Please enter your PIN:", "Screen2");
            newPanel.add(pinNumberField);
            newPanel.add(enterButton);

            enterButton.addActionListener(this);

            newFrame.add(newPanel);
        }

        if (e.getSource() == enterButton) {
            frame.dispose();
            JPanel newPanel = new JPanel();
            JFrame newFrame = createNewWindow(newPanel, "Validating if PIN matches account.", "Validating");
            newPanel.add(validateButton);
            validateButton.addActionListener(this);

            newFrame.add(newPanel);
        }

        //Screen 3
        if(e.getSource() == validateButton && !isValidPin && !tooManyInvalidPinEntries) {
            frame.dispose();

            JPanel newPanel = new JPanel();
            JFrame newFrame = createNewWindow(newPanel, "Your PIN is incorrect. Please try again.", "Screen3");
            newPanel.add(pinRetryButton);

            pinRetryButton.addActionListener(this);

            newFrame.add(newPanel);
        }

        //Screen 4
        if (e.getSource() == insertFakeATMButton || (e.getSource() == validateButton && tooManyInvalidPinEntries)) {
            frame.dispose();

            JPanel newPanel = new JPanel();
            JFrame newFrame = createNewWindow(newPanel, "Invalid ATM Card. It will be retained.", "Screen4");
            newFrame.add(newPanel);
        }


        //Screen 5
        if((e.getSource() == validateButton && isValidPin) || e.getSource() == yesButton) {
            validateFrame.dispose();
            frame.dispose();

            JPanel newPanel = new JPanel();
            String newLabel = ("Select Transaction:");
            JFrame newFrame = createNewWindow(newPanel, newLabel, "Screen5");
            newPanel.add(balanceButton);
            newPanel.add(depositButton);
            newPanel.add(withdrawalButton);
            balanceButton.addActionListener(this);
            depositButton.addActionListener(this);
            withdrawalButton.addActionListener(this);

            newFrame.add(newPanel);

            previousPanel = newPanel;
        }

        //Screen 6
        if (e.getSource() == balanceButton) {
            //previousFrame = frame;
            frame.dispose();

            JPanel newPanel = new JPanel();
            JFrame newFrame = createNewWindow(newPanel, "Balance is: $" + currentBalance + "     Another transaction?     ", "Screen6");
            newPanel.add(yesButton);
            newPanel.add((noButton));
            noButton.addActionListener(this);
            yesButton.addActionListener(this);

            newFrame.add(newPanel);
        }

        // Screen 7 - Looking to make Withdrawal or Deposit
        if ((e.getSource() == withdrawalButton && !getJammedWithdrawalChute()) || (e.getSource() == depositButton && !getProblemWithSlot())) {
            frame.dispose();

            JPanel newPanel = new JPanel();
            newPanel.add(amountField);
            newPanel.add(enterWithdrawalButton);
            newPanel.add(enterDepositButton);
            newPanel.add(backButton);
            JFrame newFrame = createNewWindow(newPanel,"<html>Deposit or Withdrawal: <br/>" +
                    "Withdrawals must be multiples of $10.</html>", "Screen7");


            enterWithdrawalButton.addActionListener(this);
            backButton.addActionListener(this);
            enterDepositButton.addActionListener(this);

            newFrame.add(newPanel);
        }

        if (e.getSource() == enterWithdrawalButton) {
            displayFundCheckingMessage("Checking funds in account");

        }

        //Screen 8 - Insufficient Funds
        if (e.getSource() == checkFundsButton && !getSufficientFunds()) {
            fundFrame.dispose();
            frame.dispose();
            JPanel newPanel = new JPanel();
            newPanel.add(amountField);
            newPanel.add(enterWithdrawalButton);
            newPanel.add(backButton);
            JFrame newFrame = createNewWindow(newPanel, "Insufficient Funds! Please enter a new amount:", "Screen8");

            enterWithdrawalButton.addActionListener(this);
            backButton.addActionListener(this);

            newFrame.add(newPanel);


        }
        //Screen 9 - Not Multiple of 10
        if ((e.getSource() == checkFundsButton && !getMultipleOfTens())) {
            fundFrame.dispose();
            frame.dispose();
            JPanel newPanel = new JPanel();
            JFrame newFrame = createNewWindow(newPanel, "<html>Machine can only dispense $10 notes <br> Would you like to make another transaction?</html>", "Screen9");

            newPanel.add(yesButton);
            newPanel.add((noButton));
            noButton.addActionListener(this);
            yesButton.addActionListener(this);

            newFrame.add(newPanel);
        }
        //Screen 10
        else if (e.getSource() == withdrawalButton && getJammedWithdrawalChute()) {
            frame.dispose();

            JPanel newPanel = new JPanel();
            JFrame newFrame = createNewWindow(newPanel, "Temporarily unable to process withdrawals" +
                    " <html></br>Another transaction?</html>", "Screen10");
            newPanel.add(yesButton);
            newPanel.add((noButton));
            noButton.addActionListener(this);
            yesButton.addActionListener(this);

            newFrame.add(newPanel);
        }
        //Screen 11
        if (e.getSource() == checkFundsButton && getMultipleOfTens() && getSufficientFunds() && getEnoughFundsInMachine()) //Add extra checks here
        {
            fundFrame.dispose();
            frame.dispose();
            JPanel newPanel = new JPanel();
            JFrame newFrame = createNewWindow(newPanel, "<html>Your balance is being updated. Please take cash from dispenser."
                    + "</br> Would you like to make another transaction?</html>", "Screen11");

            newPanel.add(yesButton);
            newPanel.add((noButton));
            noButton.addActionListener(this);
            yesButton.addActionListener(this);
            newFrame.add(newPanel);
        }

        //Screen 12
        else if (e.getSource() == depositButton && getProblemWithSlot()) {
            frame.dispose();

            JPanel newPanel = new JPanel();
            JFrame newFrame = createNewWindow(newPanel, "<html>Temporarily unable to process deposits " +
                    "</br>Another transaction?</html>", "Screen12");
            newPanel.add(amountField);
            newPanel.add(enterDepositButton);
            newPanel.add(enterWithdrawalButton);
            newPanel.add(backButton);
            backButton.addActionListener(this);
            enterDepositButton.addActionListener(this);
            enterWithdrawalButton.addActionListener(this);
            newFrame.add(newPanel);

        }
        //Screen 13
        if ((e.getSource() == enterDepositButton && !getProblemWithSlot())) {
            frame.dispose();

            JPanel newPanel = new JPanel();
            JFrame newFrame = createNewWindow(newPanel, "Please insert deposit into deposit slot.", "Screen13");

            newPanel.add(enterDepositAddButton);
            newPanel.add(backButton);
            backButton.addActionListener(this);
            enterDepositAddButton.addActionListener(this);
            newFrame.add(newPanel);
        }

        // Screen 14
        if (e.getSource() == enterDepositAddButton) {
            frame.dispose();

            JPanel newPanel = new JPanel();
            JFrame newFrame = createNewWindow(newPanel, "Your new balance is $" + currentBalance + ". Another transaction?", "Screen14");
            newPanel.add(yesButton);
            newPanel.add((noButton));
            noButton.addActionListener(this);
            yesButton.addActionListener(this);
            newFrame.add(newPanel);
        }

        if (e.getSource() == noButton){
            frame.dispose();

            JPanel newPanel = new JPanel();
            JFrame newFrame = createNewWindow(newPanel, "<html>Please take your receipt and ATM card. Thank you."
                + "<br>" + "</html>", "Screen15");
            System.out.println("Initial Balance: " + initialBalance + "\nTransactions: " + Arrays.deepToString(TransactionTracker.toArray()) +
                    "\nFinal Balance: $" +currentBalance);
            newFrame.add(newPanel);


        }

    }

    public void setSufficientFunds(boolean bool){
        sufficientFunds = bool;
    }

    public boolean getSufficientFunds(){
        return sufficientFunds;
    }

    public boolean getMultipleOfTens() {
        return multipleOfTens;
    }

    public void setEnoughFundsInMachine(boolean enoughFundsInMachine) { this.enoughFundsInMachine = enoughFundsInMachine; }

    public boolean getEnoughFundsInMachine() { return enoughFundsInMachine; }

    public void setMultipleOfTens(boolean multipleOfTens) {
        this.multipleOfTens = multipleOfTens;
    }

    public int getPinNumberField() {
        return Integer.parseInt(pinNumberField.getText());
    }

    public void setPinNumberField(JTextField pinNumberField)  { this.pinNumberField = pinNumberField; }

    public BigDecimal getAmountField() {
        return new BigDecimal(amountField.getText());
    }

    public void setAmountField(JTextField amountField) {
        this.amountField = amountField;
    }

    public void setValidPin(boolean isValidPin) {
        this.isValidPin = isValidPin;
    }

    public boolean getValidPin() {
        return isValidPin;
    }

    public boolean getProblemWithSlot() {
        return problemWithSlot;
    }

    public void setProblemWithSlot(boolean problemWithSlot) {
        this.problemWithSlot = problemWithSlot;
    }

    public boolean getJammedWithdrawalChute() {
        return jammedWithdrawalChute;
    }

    public void setJammedWithdrawalChute(boolean jammedWithdrawalChute) {
        this.jammedWithdrawalChute = jammedWithdrawalChute;
    }

    public void validatePinNumber(ATMController.PinListener listenForEnterButton) {
        enterButton.addActionListener(listenForEnterButton);
    }

    public void retryPinEntry(ATMController.PinRetryListener listenForPinRetryButton) {
        pinRetryButton.addActionListener(listenForPinRetryButton);
    }

    public void validateDepositNumber(ATMController.DepositListener listenForDepositEnterButton) {
        enterDepositButton.addActionListener(listenForDepositEnterButton);
    }

    public void validateWithdrawalNumber(ATMController.WithdrawalListener listenForWithdrawalEnterButton) {
        enterWithdrawalButton.addActionListener(listenForWithdrawalEnterButton);
    }

    public void displayErrorMessage(String errorMessage, String frameName) {
        frame.setName(frameName);
        JOptionPane.showMessageDialog(frame, errorMessage);
    }

    public void displayFundCheckingMessage(String fundMessage) {
        JPanel newPanel = new JPanel();
        JLabel label = new JLabel(fundMessage);
        fundFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        fundFrame.setSize(400, 200);
        fundFrame.setVisible(true);
        newPanel.add(checkFundsButton);
        checkFundsButton.addActionListener(this);

        newPanel.add(label);
        fundFrame.add(newPanel);
    }

    public void setCurrentBalance(BigDecimal currentBalance) {
        this.currentBalance = currentBalance;
    }

    public BigDecimal getCurrentBalance() {
        return currentBalance;
    }

    public void addToArray(String[] addThis){
        TransactionTracker.add(addThis);
    }

    public void setTooManyInvalidPinEntries(boolean tooManyInvalidPinEntires) {
        this.tooManyInvalidPinEntries = tooManyInvalidPinEntires;
    }
}
