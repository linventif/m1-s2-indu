package fr.utc.miage.wallet;

import org.junit.jupiter.api.Test;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UtilisateurTest {
  private final String NAME = "Doe";
  private final String FIRST_NAME = "John";
  private final Date BIRTHDAY = Date.valueOf("2000-01-01");
  private final Wallet WALLET = new Wallet();
  private final List<Action> ACTION_LIST = new ArrayList<>();

  @Test
  void testGetActionList() {
    Utilisateur utilisateur = new Utilisateur(NAME, FIRST_NAME, BIRTHDAY, WALLET, ACTION_LIST);
    List<Action> result = utilisateur.getActionList();
    assertEquals(ACTION_LIST, result);
  }

  @Test
  void testGetBirthday() {
    Utilisateur utilisateur = new Utilisateur(NAME, FIRST_NAME, BIRTHDAY, WALLET, ACTION_LIST);
    Date result = utilisateur.getBirthday();
    assertEquals(BIRTHDAY, result);
  }

  @Test
  void testGetFirstName() {
    Utilisateur utilisateur = new Utilisateur(NAME, FIRST_NAME, BIRTHDAY, WALLET, ACTION_LIST);
    String result = utilisateur.getFirstName();
    assertEquals(FIRST_NAME, result);
  }

  @Test
  void testGetName() {
    Utilisateur utilisateur = new Utilisateur(NAME, FIRST_NAME, BIRTHDAY, WALLET, ACTION_LIST);
    String result = utilisateur.getName();
    assertEquals(NAME, result);
  }

  @Test
  void testGetWallet() {
    Utilisateur utilisateur = new Utilisateur(NAME, FIRST_NAME, BIRTHDAY, WALLET, ACTION_LIST);
    Wallet result = utilisateur.getWallet();
    assertEquals(WALLET, result);
  }

  @Test
  void testSetActionList() {
    Utilisateur utilisateur = new Utilisateur(NAME, FIRST_NAME, BIRTHDAY, WALLET, ACTION_LIST);
    List<Action> newActionList = new ArrayList<>();
    utilisateur.setActionList(newActionList);
    assertEquals(newActionList, utilisateur.getActionList());
  }

  @Test
  void testSetWallet() {
    Utilisateur utilisateur = new Utilisateur(NAME, FIRST_NAME, BIRTHDAY, WALLET, ACTION_LIST);
    Wallet newWallet = new Wallet();
    utilisateur.setWallet(newWallet);
    assertEquals(newWallet, utilisateur.getWallet());
  }

  @Test
    void testConstructorWithValidParametresShouldWork(){
        assertDoesNotThrow(()->{new Utilisateur(FIRST_NAME, NAME, BIRTHDAY, null, ACTION_LIST)});   

    }

  @Test
    void testConstructorWithInvalidParametresShouldThrowException(){
        Date invalidBirthday = Date.valueOf("2010-01-01");
        assertThrows(IllegalArgumentException.class, ()->{new Utilisateur(FIRST_NAME, NAME, invalidBirthday, null, ACTION_LIST)});  

    }
}
