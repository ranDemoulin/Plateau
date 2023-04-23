package Plateau;

import java.security.InvalidParameterException;
import java.util.Arrays;

public abstract class PlateauAbstrait {
	protected int[][] cases;

	public PlateauAbstrait(int i, int j) {
		cases = new int[i][j];
	}

	// Constructeur vide à l'usage des classes filles qui peuvent construire
	// le plateau (y compris le tableau de cases) différemment
	protected PlateauAbstrait() {
	}

	// Constructeur copie à plat pour avoir un plateau qui travaille
	// sur le même tableau de cases que le plateau passé en paramètre
	protected PlateauAbstrait(PlateauAbstrait p) {
		cases = p.cases;
	}

	boolean in(int valeur, int min, int max) {
		return (valeur >= min) && (valeur <= max);
	}

	boolean coupJouable(int valeur, int i, int j) {
		return in(valeur, 1, 2) && in(i, 0, cases.length) && in(j, 0, cases[0].length);
	}

	public void joue(int valeur, int i, int j) {
		if (coupJouable(valeur, i, j))
			fixeValeurCase(valeur, i, j);
		else
			throw new InvalidParameterException("Joue " + valeur + " en (" + i + ", " + j + ")");
	}

	public void efface(int ligne_min, int colonne_min, int ligne_max, int colonne_max) {
		for (int i=ligne_min; i<=ligne_max; i++)
			for (int j=colonne_min; j<=colonne_max; j++)
				fixeValeurCase(0, i, j);
	}

	public int valeurCase(int i, int j) {
		return cases[i][j];
	}

	public void fixeValeurCase(int v, int i, int j) {
		cases[i][j] = v;
	}

	public boolean peutAnnuler() {
		return false;
	}

	public boolean peutRefaire() {
		return false;
	}

	public void annule() {
	}

	public void refais() {
	}

	public void sauve(String fichier) throws Exception {
	}

	public int nbLignes() {
		return cases.length;
	}

	public int nbColonnes() {
		return cases[0].length;
	}

	@Override
	public String toString() {
		String result = "Plateau:\n[";
		String sep = "";
		for (int i=0; i<cases.length; i++) {
			result += sep + Arrays.toString(cases[i]);
			sep = "\n ";
		}
		result += 	"]\nEtat:" +
				"\n- peut annuler : " + peutAnnuler() +
				"\n- peut refaire : " + peutRefaire();
		return result;
	}
}
