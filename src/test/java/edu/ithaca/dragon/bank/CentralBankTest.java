package edu.ithaca.dragon.bank;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CentralBankTest {
    @Test
    void depositTest(){
        BankAccount account = new BankAccount("a@b.com", 200);
        BasicAPI atm = new CentralBank();
        String acctId = account.acctId;

        assertThrows(IllegalArgumentException.class, ()-> atm.deposit(acctId, -100)); // invalid middle case (value)
        assertThrows(IllegalArgumentException.class, ()-> atm.deposit(acctId,-1)); // invalid border case (value)
        assertThrows(IllegalArgumentException.class, ()-> atm.deposit(acctId,100.001)); // invalid border case (decimal place limit)
        assertThrows(IllegalArgumentException.class, ()-> atm.deposit(acctId,100.00001)); // invalid middle case (decimal place limit)
        assertThrows(IllegalArgumentException.class, ()-> atm.deposit(acctId, -100.001)); // invalid case (middle value, border decimal place limit)
        assertThrows(IllegalArgumentException.class, ()-> atm.deposit(acctId, -100.00001)); // invalid middle case (value, decimal place limit)


    }
}
