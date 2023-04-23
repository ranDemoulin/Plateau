package Plateau;

import Plateau.PlateauAbstrait;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.Stack;
import java.security.InvalidParameterException;

public class PlateauConcret extends PlateauAbstrait {
	Stack<COUP> pil_passer;
	Stack<COUP> pil_futur;

	public class COUP {
		public int val, x, y;

		public COUP(int val, int x, int y) {
			this.val = val;
			this.x = x;
			this.y = y;
		}

		@Override
		public String toString() { return val + " " + x + " " + y ; }
		public int get_val(){ return val; }
		public int get_x(){ return x; }
		public int get_y(){ return y; }
	}

	public PlateauConcret(int i, int j) {
		super(i,j);
	}

	public PlateauConcret(String fichier) {
	}

	@Override
	public void joue(int valeur, int i, int j) {
		if (coupJouable(valeur, i, j)) {
			fixeValeurCase(valeur, i, j);
			pil_passer.push(new COUP(valeur, i, j));
			while(!pil_futur.empty())
				pil_futur.pop();
		}else
			throw new InvalidParameterException("Joue " + valeur + " en (" + i + ", " + j + ")");
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
		pil_futur.push(temp);
		fixeValeurCase(0, temp.get_x(), temp.get_y());
	}

	@Override
	public void refais() {
		COUP temp = pil_futur.pop();
		pil_passer.push(temp);
		fixeValeurCase(temp.get_val(), temp.get_x(), temp.get_y());
	}

	@Override
	public void sauve(String fichier) throws Exception {
		// Init fichier
		File f = new File(fichier);
		FileWriter w_f = new FileWriter(f);

		String sauv_data = "";
		COUP c;

		while (!pil_passer.empty()) {
			pil_futur.push(pil_passer.pop());// On vide la pile des coup dans pil_futur
		}                                     // pour commencer par ecrire le premier coup dans le fichier
		while(!pil_futur.empty()){
			c = pil_futur.pop();
			sauv_data += c.toString();
		}
		w_f.write();
	}
}
