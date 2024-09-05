import java.util.LinkedList;

public class GNodo {
	long id;
	double lat;
	double lon;
	boolean ativo = false;
	
	LinkedList<Aresta> listaArestas = new LinkedList<>();
}


