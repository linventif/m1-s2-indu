package fr.utc.miage.wallet;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.jupiter.api.Test;

class WalletTest {

  private static final String CORRECT_CODE = "Main";
  // Transaction
  
  public Wallet getCorrectWallet(){
    return new Wallet(CORRECT_CODE);
  }

  @Test
  void testConstructor(){
    assertDoesNotThrow(() -> new Wallet(CORRECT_CODE),
    "Constructor should not throw exception");
  }

  @Test
  void testGetters(){
    Wallet wallet = new Wallet(CORRECT_CODE);
    assertEquals(CORRECT_CODE, wallet.getCode(), "getCode() KO");
    assertEquals(new ArrayList<>(), wallet.getTransactions(), "getTransactions() KO");
    assertEquals(new HashMap<>(), wallet.getListAction(), "getListAction() KO");
  }

  @Test
  void testAddTransaction(){
    Wallet wallet = new Wallet(CORRECT_CODE);
    wallet.addTransaction(TransactionTest.getCorrectTransaction());
  }


}
