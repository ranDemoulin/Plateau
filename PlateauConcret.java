package Plateau;

import Plateau.PlateauAbstrait;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.Stack;
import java.io.FileNotFoundException;
import java.security.InvalidParameterException;

public class PlateauConcret extends PlateauAbstrait {
	Stack<COUP> pil_passer;
	Stack<COUP> pil_futur;

	public static class COUP {
		public int val, x, y;

		public COUP(int val, int x, int y) {
			this.val = val;
			this.x = x;
			this.y = y;
		}

		public String save_String() { return val + " " + x + " " + y ; }

		public int get_val(){ return val; }

		public int get_x(){ return x; }

		public int get_y(){ return y; }
	}

	public PlateauConcret(int i, int j) {
		super(i,j);
		pil_passer = new Stack<COUP>();
		pil_futur = new Stack<COUP>();
	}

	public PlateauConcret(String fichier){
		// init class
		pil_passer = new Stack<COUP>();
		pil_futur = new Stack<COUP>();

		// Init var
		int val, x, y;
		Scanner sc_f;

		// Init fichier
		try {
			sc_f= new Scanner(new File(fichier));
		}catch (Exception E){
			System.out.println(fichier + " isn't accesible");
			return;
		}

		// Tant que le fichier n'est pas vide, vérifie que le fichier est comforme et joue si il trouve un coup
		try {
			x = sc_f.nextInt();
			y = sc_f.nextInt();
			cases = new int[x][y];
			while(sc_f.hasNext()) {
					val = sc_f.nextInt();
					x = sc_f.nextInt();
					y = sc_f.nextInt();
					pil_futur.push(new COUP(val, x, y));
				}
		}catch (Exception E) {
			System.out.println(fichier + " isn't a save file");
		}

		while(peutRefaire()){
			refais();
		}
		sc_f.close();
	}

	@Override
	public void joue(int valeur, int i, int j) {
		if (coupJouable(valeur, i, j)) {
			fixeValeurCase(valeur, i, j);
			pil_passer.push(new COUP(valeur, i, j));
			while(!pil_futur.empty()) {
				pil_futur.pop();
			}
		}else
			throw new InvalidParameterException("Joue " + valeur + " en (" + i + ", " + j + ")");
	}

	@Override
	public void efface(int ligne_min, int colonne_min, int ligne_max, int colonne_max) {
		pil_passer.push(new COUP(-1, -1, -1));
		for (int i=ligne_min; i<=ligne_max; i++) {
			for (int j = colonne_min; j <= colonne_max; j++) {
				if (cases[i][j] != 0){
					pil_passer.push(new COUP(cases[i][j], i, j));
				}
				fixeValeurCase(0, i, j);
			}
		}
		pil_passer.push(new COUP(-1, -1, -1));
	}

	@Override
	public boolean peutAnnuler() {
		if (pil_passer.empty()){
			return false;
		}else{
			return true;
		}
	}

	@Override
	public boolean peutRefaire() {
		if (pil_futur.empty()){
			return false;
		}else{
			return true;
		}
	}

	@Override
	public void annule() {
		COUP temp = pil_passer.pop();
		if (temp.get_val() == -1){
			pil_futur.push(temp);
			temp = pil_passer.pop();
			while(temp.get_val() != -1){
				pil_futur.push(temp);
				fixeValeurCase(temp.get_val(), temp.get_x(), temp.get_y());
				temp = pil_passer.pop();
			}
			pil_futur.push(temp);
		}else{
			pil_futur.push(temp);
			fixeValeurCase(0, temp.get_x(), temp.get_y());
		}
	}

	@Override
	public void refais() {
		COUP temp = pil_futur.pop();
		if (temp.get_val() == -1){
			pil_passer.push(temp);
			temp = pil_futur.pop();
			while(temp.get_val() != -1){
				pil_passer.push(temp);
				fixeValeurCase(0, temp.get_x(), temp.get_y());
				temp = pil_futur.pop();
			}
			pil_passer.push(temp);
		}else{
			pil_passer.push(temp);
			fixeValeurCase(temp.get_val(), temp.get_x(), temp.get_y());
		}

	}

	@Override
	public void sauve(String fichier) throws Exception {
		// Init fichier
		File f = new File(fichier);
		f.setReadable(true);
		f.setWritable(true);
		PrintWriter w_f = new PrintWriter(f);

		// Init var
		String sauv_data = "";
		COUP c;

		// on ecrit d'abort la taille du tableau
		w_f.println(nbLignes()+" "+nbColonnes()+" ");

//		// On vide la pile des coup dans pil_passer pour commencer par ecrire le dernier coup dans le fichier
//		while (!pil_futur.empty()) {
//			pil_passer.push(pil_futur.pop());
//		}

		//On creer le String de data de sauvegarde (i.e la liste des coup réaliser)
		while(!pil_passer.empty()){
			c = pil_passer.pop();
			sauv_data += c.save_String() + " ";
		}
		w_f.println(sauv_data);
		w_f.close();
	}
}