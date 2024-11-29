
public class MapIndividuo {
	int dna[] = new int[10000];
	int score = 0;
	public MapIndividuo() {
	}
	public void randomiza() {
		for(int i = 0; i < 10000;i++) {
			dna[i] = GamePanel.rnd.nextInt(2);
		}
	}
}
