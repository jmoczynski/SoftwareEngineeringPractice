package edu.ithaca.dragon.bank;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SavingsAccountTest {

    @Test
    void constructorTest(){
        SavingsAccount savingsAccount1 = new SavingsAccount("a@b.com", 200, "s1"); // unit test valid equivalence class (valid email, middle case)
        assertEquals(200, savingsAccount1.getBalance());
        assertThrows(IllegalArgumentException.class, ()-> new SavingsAccount("a-@b.com", 200, "s2")); // integration test invalid equivalence class (invalid email, border case)
        assertThrows(IllegalArgumentException.class, ()-> new SavingsAccount("a@b.com", -100, "s3")); // integration test invalid equivalence class (invalid amount, middle case)
        assertThrows(IllegalArgumentException.class, ()-> new SavingsAccount("a@b.com", 0.001, "s4")); // integration test invalid equivalence class (invalid amount, border case)
        SavingsAccount savingsAccount2 = new SavingsAccount("a@b.com", 0.01, "s5"); // integration test valid equivalence class (valid amount, border case)
        assertEquals(0.01, savingsAccount2.getBalance());
    }

    @Test
    void createSavingsAccountTest(){
        CentralBank centralBank1 = new CentralBank();
        assertThrows(IllegalArgumentException.class, ()-> centralBank1.createAccount("a-@b.com", 200, "Savings")); // integration test invalid equivalence class (invalid email, border case)
        assertThrows(IllegalArgumentException.class, ()-> centralBank1.createAccount("a@b.com", 0.001, "Savings")); // integration test invalid equivalence class (invalid starting balance, border case)
        assertThrows(IllegalArgumentException.class, ()-> centralBank1.createAccount("a@b.com", 0.000001, "Savings")); // integration test invalid equivalence class (invalid starting balance, middle case)
        assertThrows(IllegalArgumentException.class, ()-> centralBank1.createAccount("a@b.com", 200, "savings")); // integration test invalid equivalence class (invalid account type, border case)
        assertThrows(IllegalArgumentException.class, ()-> centralBank1.createAccount("a@b.com", 200, "Saving")); // integration test invalid equivalence class (invalid account type, border case)
        assertThrows(IllegalArgumentException.class, ()-> centralBank1.createAccount("a@b.com", 200, "Funding")); // integration test invalid equivalence class (invalid account type, middle case)

        centralBank1.createAccount("a@b.com", 200, "Savings"); // integration test valid equivalence class (valid starting balance, middle case)
        assertEquals(200, centralBank1.getAccounts()[0].getBalance());
        centralBank1.createAccount("a@b.com", 0.01, "Savings"); // integration test valid equivalence class (valid starting balance, border case)
        assertEquals(0.01, centralBank1.getAccounts()[1].getBalance());
    }

    @Test
    void setInterestRateTest(){
        SavingsAccount account1 = new SavingsAccount("a@b.com", 100, "s1");
        assertThrows(IllegalArgumentException.class, ()-> account1.setInterestRate(-2)); // unit test invalid equivalence class (negative amount, middle case)
        assertThrows(IllegalArgumentException.class, ()->account1.setInterestRate(0)); // unit test invalid equivalence class (negative amount, border case)
        assertThrows(IllegalArgumentException.class, ()->account1.setInterestRate(155)); // unit test invalid equivalence class (amount greater than or equal to 100, middle case)
        assertThrows(IllegalArgumentException.class, ()->account1.setInterestRate(100)); // unit test invalid equivalence class (amount greater than or equal to 100, border case)
        assertThrows(IllegalArgumentException.class, ()->account1.setInterestRate(0.00001)); // unit test invalid equivalence class (amount has more than 2 decimal places, middle case)
        assertThrows(IllegalArgumentException.class, ()->account1.setInterestRate(0.001)); // unit test invalid equivalence class (amount has more than 2 decimal places, border case)
        account1.setInterestRate(50); // unit test valid equivalence class (valid amount, middle case)
        assertEquals(50, account1.getInterestRate());
        account1.setInterestRate(0.01); // unit test valid equivalence class (valid amount, border case)
        assertEquals(0.01, account1.getInterestRate());
        account1.setInterestRate(99.99); // unit test valid equivalence class (valid amount, border case)
        assertEquals(99.99, account1.getInterestRate());
    }

    @Test
    void depositTest(){
        SavingsAccount account1 = new SavingsAccount("a@b.com", 200, "s1");
        assertThrows(IllegalArgumentException.class, ()-> account1.deposit(-100)); // integration test invalid equivalence class (negative amount, middle case)
        assertThrows(IllegalArgumentException.class, ()-> account1.deposit(-0.01)); // integration test invalid equivalence class (negative amount, border case)
        assertThrows(IllegalArgumentException.class, ()-> account1.deposit(0.000001)); // integration test invalid equivalence class (too many decimal places, middle case)
        assertThrows(IllegalArgumentException.class, ()-> account1.deposit(0.001)); // integration test invalid equivalence class (too many decimal places, border case)
        account1.deposit(100); // integration test valid equivalence class (valid amount, middle case)
        assertEquals(300, account1.getBalance());
        account1.deposit(0.01); // integration test valid equivalence class (valid amount, border case)
        assertEquals(300.01, account1.getBalance());
    }

    @Test
    void applyCompoundInterestTest(){
        SavingsAccount account1 = new SavingsAccount("a@b.com", 1000, "s1");
        account1.setInterestRate(10);
        account1.applyCompoundInterest();
        assertEquals(1100, account1.getBalance()); // unit test valid equivalence class (valid starting amount, valid interest rate, # of times compounded border case)
        account1.applyCompoundInterest();
        assertEquals(1210, account1.getBalance()); // unit test valid equivalence class (valid starting amount, valid interest rate, # of times compounded middle case)
    }

    @Test
    void setDailyMaxTest() throws InsufficientFundsException{
        SavingsAccount account1 = new SavingsAccount("a@b.com", 200, "s1");
        assertThrows(IllegalArgumentException.class, ()-> account1.setDailyMax(-1)); // unit test invalid equivalence class (negative amount, middle case)
        assertThrows(IllegalArgumentException.class, ()-> account1.setDailyMax(-0.01)); // unit test invalid equivalence class (negative amount, border case)
        assertThrows(IllegalArgumentException.class, ()-> account1.setDailyMax(0.0001)); // unit test invalid equivalence class (more than 2 decimal places, middle case)
        assertThrows(IllegalArgumentException.class, ()-> account1.setDailyMax(0.001)); // unit test invalid equivalence class (more than 2 decimal places, border case)
        assertThrows(InsufficientFundsException.class, ()-> account1.setDailyMax(500)); // unit test invalid equivalence class (larger than original amount, middle case)
        assertThrows(InsufficientFundsException.class, ()-> account1.setDailyMax(200.01)); // unit test invalid equivalence class (larger than original amount, border case)
        account1.setDailyMax(1); // unit test valid equivalence class (positive amount, middle case)
        assertEquals(1, account1.getDailyMax());
        account1.setDailyMax(0.01); // unit test valid equivalence class (positive amount, border case)
        assertEquals(0.01, account1.getDailyMax());
        account1.setDailyMax(100.1); // unit test valid equivalence class (at most 2 decimal places, middle case)
        assertEquals(100.1, account1.getDailyMax());
        account1.setDailyMax(100.01); // unit test valid equivalence class (at most 2 decimal places, border case)
        assertEquals(100.01, account1.getDailyMax());
        account1.setDailyMax(100);  // unit test valid equivalence class (less than original amount, middle case)
        assertEquals(100, account1.getDailyMax());
        account1.setDailyMax(199.99);  // unit test valid equivalence class (less than original amount, border case)
        assertEquals(199.99, account1.getDailyMax());
    }

    @Test
    void withdrawTest() throws IllegalArgumentException, InsufficientFundsException{
        SavingsAccount account1 = new SavingsAccount("a@b.com", 200, "s1");
        assertThrows(IllegalArgumentException.class, ()-> account1.withdraw(-10)); // unit test invalid equivalence class (negative amount, middle case)
        assertThrows(IllegalArgumentException.class, ()-> account1.withdraw(-0.01)); // unit test invalid equivalence class (negative amount, border case)
        assertThrows(IllegalArgumentException.class, ()-> account1.withdraw(0.00001)); // unit test invalid equivalence class (decimal places more than 2, middle case)
        assertThrows(IllegalArgumentException.class, ()-> account1.withdraw(0.001)); // unit test invalid equivalence class (decimal places more than 2, border case)
        assertThrows(InsufficientFundsException.class, ()-> account1.withdraw(300)); // unit test invalid equivalence class (larger than account amount, middle case)
        assertThrows(InsufficientFundsException.class, ()-> account1.withdraw(200.01)); // unit test invalid equivalence class (larger than account amount, border case)
        SavingsAccount account2 = new SavingsAccount("a@b.com", 200, "s2");
        account2.setDailyMax(10);
        assertThrows(InsufficientFundsException.class, ()-> account2.withdraw(20)); // unit test invalid equivalence class (larger than remaining portion of daily max, middle case)
        assertThrows(InsufficientFundsException.class, ()-> account2.withdraw(10.01)); // unit test invalid equivalence class (larger than remaining portion of daily max, border case)
        SavingsAccount account3 = new SavingsAccount("a@b.com", 200, "s3");
        account3.setDailyMax(10);
        account3.withdraw(5); // unit test valid equivalence class (valid withdraw amount, middle case)
        assertEquals(195, account3.getBalance());
        assertEquals(5, account3.getWithdrawnToday());
        SavingsAccount account4 = new SavingsAccount("a@b.com", 200, "s4");
        account4.setDailyMax(10);
        account4.withdraw(10); // unit test valid equivalence class (valid withdraw amount, border case)
        assertEquals(190, account4.getBalance());
        assertEquals(10, account4.getWithdrawnToday());
        SavingsAccount account5 = new SavingsAccount("a@b.com", 200, "s5");
        account5.setDailyMax(10);
        account5.withdraw(0.01); // unit test valid equivalence class (valid withdraw amount, border case)
        assertEquals(199.99, account5.getBalance());
        assertEquals(0.01, account5.getWithdrawnToday());
        SavingsAccount account6 = new SavingsAccount("a@b.com", 200, "s6");
        account6.setDailyMax(10);
        account6.withdraw(5);
        assertEquals(195, account6.getBalance());
        assertEquals(5, account6.getWithdrawnToday());
        account6.withdraw(5);
        assertEquals(190, account6.getBalance());
        assertEquals(10, account6.getWithdrawnToday());
    }

    @Test
    void transferTest() throws IllegalArgumentException, InsufficientFundsException{
        SavingsAccount account1 = new SavingsAccount("a@b.com", 200, "s1");
        SavingsAccount account2 = new SavingsAccount("c@d.com", 200, "s2");
        account1.setDailyMax(10);
        account2.setDailyMax(10);
        assertThrows(IllegalArgumentException.class, ()-> account1.transfer(account2, -10)); // unit test invalid equivalence class (negative value, middle case)
        assertThrows(IllegalArgumentException.class, ()-> account1.transfer(account2, -0.01)); // unit test invalid equivalence class (negative value, border case)
        assertThrows(IllegalArgumentException.class, ()-> account2.transfer(account1, 0.00001)); // unit test invalid equivalence class (decimal place limit, middle case)
        assertThrows(IllegalArgumentException.class, ()-> account2.transfer(account1, 0.001)); // unit test invalid equivalence class (decimal place limit, border case)
        assertThrows(IllegalArgumentException.class, ()-> account1.transfer(account1, 10)); // unit test invalid equivalence class (invalid account to transfer to, middle case)
        assertThrows(InsufficientFundsException.class, ()-> account2.transfer(account1, 20)); // unit test invalid equivalence class (larger than limit, middle case)
        assertThrows(InsufficientFundsException.class, ()-> account2.transfer(account1, 10.01)); // unit test invalid equivalence class (larger than limit, border case)
        account1.transfer(account2, 5); // unit test valid equivalence class (valid amount, middle case)
        assertEquals(195, account1.getBalance());
        assertEquals(205, account2.getBalance());
        assertEquals(5, account1.getWithdrawnToday());
        account2.transfer(account1, 10); // unit test valid equivalence class (valid amount, border case)
        assertEquals(205, account1.getBalance());
        assertEquals(195, account2.getBalance());
        assertEquals(10, account2.getWithdrawnToday());
        account1.transfer(account2, 0.01); // unit test valid equivalence class (valid amount, border case)
        assertEquals(204.99, account1.getBalance());
        assertEquals(195.01, account2.getBalance());
        assertEquals(5.01, account1.getWithdrawnToday());
    }

}
