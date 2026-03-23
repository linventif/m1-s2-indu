package fr.utc.miage.wallet;

public class Demo {
  public static void main(String[] args) {
    // Action
    Action actOVH = new Action("OVH", 100);
    Action actGoogle = new Action("GOOG", 150);
    System.out.println(actOVH);
    System.out.println(actGoogle);

    // Wallet
    Wallet wallet = new Wallet();
    wallet.addAction(actOVH, 5);
    wallet.addAction(actGoogle, 3);
    System.out.println(wallet);
    System.out.println("Total value: " + wallet.getTotalValue() + "€");
  }
}
