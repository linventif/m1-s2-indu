package fr.utc.miage.wallet;

import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings({ "java:S106", "java:S1192" })
public final class Demo {
  public static void main(final String[] args) {
    Action actionOvh = new Action("OVH Demo", 10.0, ActionCategory.INDUSTRIAL);
    Action actionGoogle = new Action("Google Demo", 20.0, ActionCategory.OTHER);

    Map<String, Float> composition = new HashMap<>();
    composition.put(actionOvh.getLabel(), 0.4f);
    composition.put(actionGoogle.getLabel(), 0.6f);
    Action actionTechFund = new Action("Tech Fund Demo", 17.0, composition);

    Utilisateur utilisateur = new Utilisateur("John", "Doe", Date.valueOf("2000-01-01"));

    System.out.println();
    System.out.println("========================================");
    System.out.println("DEMONSTRATION WALLET");
    System.out.println("========================================");
    System.out.println("Actions disponibles:");
    System.out.println(" - " + actionOvh);
    System.out.println(" - " + actionGoogle);
    System.out.println(" - " + actionTechFund.toStringC());
    System.out.println();
    System.out.println("[Etat initial]");
    System.out.println("Utilisateur: " + utilisateur.getFirstName() + " " + utilisateur.getName());
    System.out.println("Cash: " + utilisateur.getCashAmount() + "EUR");
    System.out.println("Portefeuille: " + utilisateur.getWallet().getActions());
    System.out.println("Valeur totale: " + utilisateur.getWallet().getTotalValue() + "EUR");
    System.out.println();

    utilisateur.buyAction(actionOvh, 5);
    utilisateur.buyAction(actionGoogle, 2);
    utilisateur.buyAction(actionTechFund, 1);

    System.out.println("[Apres achats]");
    System.out.println("Utilisateur: " + utilisateur.getFirstName() + " " + utilisateur.getName());
    System.out.println("Cash: " + utilisateur.getCashAmount() + "EUR");
    System.out.println("Portefeuille: " + utilisateur.getWallet().getActions());
    System.out.println("Valeur totale: " + utilisateur.getWallet().getTotalValue() + "EUR");
    System.out.println();

    utilisateur.sellAction(actionGoogle, 1);

    System.out.println("[Apres vente d'une action Google Demo]");
    System.out.println("Utilisateur: " + utilisateur.getFirstName() + " " + utilisateur.getName());
    System.out.println("Cash: " + utilisateur.getCashAmount() + "EUR");
    System.out.println("Portefeuille: " + utilisateur.getWallet().getActions());
    System.out.println("Valeur totale: " + utilisateur.getWallet().getTotalValue() + "EUR");
    System.out.println();
    System.out.println("Actions industrielles connues: " + Action.getActionsByCategory(ActionCategory.INDUSTRIAL));
  }
}
