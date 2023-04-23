package Plateau;

import java.util.Random;
import java.util.Scanner;

public abstract class TestPlateau {
	PlateauAbstrait p;
	String fichierSortie;
	protected abstract PlateauAbstrait nouveauPlateau(int i, int j);

	protected PlateauAbstrait nouveauPlateau(String fichier) throws Exception {
		return null;
	}

	void execute(String s) {
		String [] parts = s.split("\\s+");
		int [] args = new int[parts.length - 1];

		try {
			for (int i = 0; i < parts.length - 1; i++)
				args[i] = Integer.parseInt(parts[i + 1]);

			switch (parts[0]) {
				case "joue":
					p.joue(args[0], args[1], args[2]);
					break;
				case "valeur":
					p.valeurCase(args[0], args[1]);
					break;
				case "efface":
					p.efface(args[0], args[1], args[2], args[3]);
					break;
				case "annule":
					if (p.peutAnnuler())
						p.annule();
					else
						System.out.println("Impossible d'annuler");
					break;
				case "refais":
					if (p.peutRefaire())
						p.refais();
					else
						System.out.println("Impossible de refaire");
					break;
				case "print":
					System.out.println(p);
					break;
				default:
					throw new UnsupportedOperationException(parts[0]);
			}
		} catch (Exception e) {
			System.err.println("Commande invalide : " + s + " (" + e + ")");
		}
	}

	public void run(String [] args) {
		int width=5, height=5;
		int indiceOption = 0;
		boolean finOptions = false;

		while (!finOptions && indiceOption < (args.length - 1)) {
			switch (args[indiceOption]) {
				case "-i":
					try {
						p = nouveauPlateau(args[indiceOption + 1]);
						indiceOption += 2;
					} catch (Exception e) {
						System.err.println("Impossible de lire le plateau : " + e);
						System.exit(2);
					}
					break;
				case "-o":
					fichierSortie = args[indiceOption + 1];
					indiceOption += 2;
					break;
				case "-w":
					width = Integer.parseInt(args[indiceOption+1]);
					indiceOption += 2;
					break;
				case "-h":
					height = Integer.parseInt(args[indiceOption+1]);
					indiceOption += 2;
					break;
				default:
					finOptions = true;
			}
		}
		if (indiceOption  >= args.length) {
			System.err.println("Utilisation :\n" +
					"TestPlateau [ -i inputfile ] [ -o outputfile ] [ -w width ] [ -h height ] interactive|random\n" +
					"Ce programme permet de tester l'utilisation d'un plateau avec historique soit en prennant ses commandes depuis l'entrée standard (mode interactive),\n" +
					"soit en les tirant au hasard (mode random). Si l'option '-i' est donnée, l'état initial du plateau est lu depuis le fichier 'inputfile'. Si l'option\n" +
					"'-o' est donnée, l'état final du plateau est écrit dans le fichier 'outputfile'. Lorsque le plateau n'est pas lu depuis un fichier, sa largeur et sa\n" +
					"hauteur sont donnés par width et height (5 et 5 par défaut). Les commandes sont les suivantes :\n" +
					"- joue valeur ligne colonne\n" +
					"- valeur ligne colonne\n" +
					"- efface ligne_min colonne_min ligne_max colonne_max\n" +
					"- annule\n" +
					"- refais\n" +
					"- print");
			System.exit(1);
		}
		if (p == null)
			p = nouveauPlateau(height, width);
		switch (args[indiceOption]) {
			case "interactive":
				Scanner s = new Scanner(System.in);
				while (s.hasNextLine()) {
					execute(s.nextLine());
				}
				break;
			case "random":
				Random r;
				indiceOption++;
				try {
					if (indiceOption < args.length)
						r = new Random(Integer.parseInt(args[indiceOption]));
					else
						r = new Random(42);
					for (int i = 0; i < r.nextInt(100) + 100; i++) {
						switch (r.nextInt(6)) {
							case 0:
							case 1:
								int v = r.nextInt(2) + 1;
								int l = r.nextInt(p.nbLignes());
								int c = r.nextInt(p.nbColonnes());
								System.out.println("joue " + v + " " + l + " " + c);
								p.joue(v, l, c);
								break;
							case 2:
								int t1 = r.nextInt(p.nbLignes());
								int t2 = r.nextInt(p.nbLignes());
								int t3 = r.nextInt(p.nbColonnes());
								int t4 = r.nextInt(p.nbColonnes());
								int l1 = Math.min(t1, t2);
								int l2 = Math.max(t1, t2);
								int c1 = Math.min(t3, t4);
								int c2 = Math.max(t3, t4);
								System.out.println("efface " + l1 + " " + c1 + " " + l2 + " " + c2);
								p.efface(l1, c1, l2, c2);
								break;
							case 3:
								System.out.println(p);
								break;
							case 4:
								if (p.peutAnnuler()) {
									System.out.println("annule");
									p.annule();
								}
								break;
							case 5:
								if (p.peutRefaire()) {
									System.out.println("refais");
									p.refais();
								}
								break;
						}
					}
				} catch (Exception e) {
					System.err.println("Bug interne dans le jeu aléatoire : " + e);
					System.exit(4);
				}
		}
		if (fichierSortie != null) {
			try {
				p.sauve(fichierSortie);
			} catch (Exception e) {
				System.err.println("Impossible de sauver le plateau dans le fichier " + fichierSortie + " : " + e);
				System.exit(3);
			}
		}
	}
}
