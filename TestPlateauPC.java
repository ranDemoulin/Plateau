package Plateau;

public class TestPlateauPC extends TestPlateau {
	@Override
	protected PlateauAbstrait nouveauPlateau(int i, int j) {
		return new PlateauConcret(i, j);
	}

	@Override
	protected PlateauAbstrait nouveauPlateau(String fichier) throws Exception {
		return new PlateauConcret(fichier);
	}

	public static void main(String [] args){
		new TestPlateauPC().run(args);
	}
}
