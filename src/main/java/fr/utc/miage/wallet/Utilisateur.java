package fr.utc.miage.wallet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Utilisateur {

	private final List<Action> actions;

	public Utilisateur() {
		this.actions = new ArrayList<>();
	}

	public void addAction(Action action) {
		actions.add(action);
	}

	public boolean deleteAction(Action action) {
		return actions.remove(action);
	}

	public List<Action> getActions() {
		return Collections.unmodifiableList(actions);
	}

}
