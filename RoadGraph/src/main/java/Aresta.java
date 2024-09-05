
public class Aresta {
	GNodo A;
	GNodo B;
	float size = 0;
	
	public Aresta(GNodo A,GNodo B) {
		this.A = A;
		this.B = B;
		
		double dlat = A.lat-B.lat;
		double dlon = A.lon-B.lon;
		
		double dsize = Math.sqrt(dlat*dlat + dlon*dlon);
		size = (float)dsize*1000000;
	}
}
