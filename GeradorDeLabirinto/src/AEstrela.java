import java.util.ArrayList;
import java.util.HashSet;


public class AEstrela {
	Mapa mapa;
	
	ArrayList<nodo> nodosAbertos = new ArrayList<nodo>();
	//ArrayList<nodo> nodosFechados = new ArrayList<nodo>();
	HashSet<Integer> nodosFechados = new HashSet<Integer>();
	nodo ultimoAberto = null;
	
	int objetivox = 0;
	int objetivoy = 0;
	
	boolean achoufinal = true;
	boolean iniciouAestrela = false;
	nodo selecionado = null;
	
	public AEstrela(Mapa _mapa) {
		// TODO Auto-generated constructor stub
		mapa = _mapa;
	}
	
	public int[] StartAestrela(int x,int y,int objx,int objy,int nestados){
		objetivox = objx;
		objetivoy = objy;
		
		synchronized (nodosAbertos) {
			nodosAbertos.clear();			
		}
		synchronized(nodosFechados){
			nodosFechados.clear();
		}
		
		selecionado = new nodo(null, x, y, 0);
		
		boolean nodofinal = false;
		
		for(int i = 0; i < nestados;i++){
			if(abreNodo(selecionado)){
				selecionado = ultimoAberto;//nodosFechados.get(nodosFechados.size()-1);
				nodofinal = true;
				break;
			}
			double menor = 99999999;
			double menoheuristica = 99999999;
			int menoridx = -1;
			
			for(int z = 0; z < nodosAbertos.size();z++){
				nodo nodo2 = nodosAbertos.get(z);
				double soma = nodo2.energia+nodo2.euristica;
				
				//System.out.println(" z "+z+" "+soma);
				
				if(soma<menor){
					menor = soma;
					menoridx = z;
					menoheuristica = nodo2.euristica;
				}else if(soma==menor){
					if(nodo2.euristica<menoheuristica){
						menor = soma;
						menoridx = z;
						menoheuristica = nodo2.euristica;
					}
				}
			}
			
			//System.out.println("menoridx "+menoridx+" test "+nodosAbertos.size());
			
			if(menoridx==-1){
				return null;
			}
			
			selecionado = nodosAbertos.get(menoridx);
			nodosAbertos.remove(menoridx);
		}
		
		ArrayList<nodo> caminho = new ArrayList<nodo>();
		
		nodo onodo = selecionado;
		
		caminho.add(onodo);
		
		while(onodo.pai!=null){
			onodo = onodo.pai;
			caminho.add(0,onodo);
		}
		
		int ocaminho[] = new int[caminho.size()*2];
		for(int i =  0; i < caminho.size();i++){
			nodo nd = caminho.get(i);
			ocaminho[i*2] = nd.x;
			ocaminho[(i*2)+1] = nd.y;
		}
		
		
		if(nodofinal==false){
			achoufinal = false;
			iniciouAestrela = true;
		}
		
		
		
		return ocaminho;
	}
	
	public int[] continuapath(){
		
		for(int i = 0; i < 20;i++){
			if(abreNodo(selecionado)){
				selecionado = ultimoAberto;//nodosFechados.get(nodosFechados.size()-1);
				achoufinal = true;
				break;
			}
			double menor = 99999999;
			double menoheuristica = 99999999;
			int menoridx = -1;
			
			synchronized (nodosAbertos) {				
				for(int z = 0; z < nodosAbertos.size();z++){
					nodo nodo2 = nodosAbertos.get(z);
					double soma = nodo2.energia+nodo2.euristica;
					if(soma<menor){
						menor = soma;
						menoridx = z;
						menoheuristica = nodo2.euristica;
					}else if(soma==menor){
						if(nodo2.euristica<menoheuristica){
							menor = soma;
							menoridx = z;
							menoheuristica = nodo2.euristica;					
						}
					}
				}
			}
			
			if(menoridx==-1){
				return null;
			}
			
			selecionado = nodosAbertos.get(menoridx);
			nodosAbertos.remove(menoridx);
		}
		
		if(achoufinal){
		
			ArrayList<nodo> caminho = new ArrayList<nodo>();
			
			nodo onodo = ultimoAberto;//nodosFechados.get(nodosFechados.size()-1);
			
			caminho.add(onodo);
			
			while(onodo.pai!=null){
				onodo = onodo.pai;
				caminho.add(0,onodo);
			}
			
			int ocaminho[] = new int[caminho.size()*2];
			for(int i =  0; i < caminho.size();i++){
				nodo nd = caminho.get(i);
				ocaminho[i*2] = nd.x;
				ocaminho[(i*2)+1] = nd.y;
			}
			
			iniciouAestrela = false;
			
			return ocaminho;
		}else{
			return null;
		}
	}
	
	public boolean abreNodo(nodo onodo){
		//nodosFechados.add(onodo);
		nodosFechados.add(onodo.x+onodo.y*1000000);
		ultimoAberto = onodo;
		
		nodo candidatos[] = new nodo[4];
		
		candidatos[0] = new nodo(onodo, onodo.x+1, onodo.y, onodo.energia+1);
		candidatos[1] = new nodo(onodo, onodo.x, onodo.y+1, onodo.energia+1);
		candidatos[2] = new nodo(onodo, onodo.x-1, onodo.y, onodo.energia+1);
		candidatos[3] = new nodo(onodo, onodo.x, onodo.y-1, onodo.energia+1);
		
//		candidatos[4] = new nodo(onodo, onodo.x+1, onodo.y+1, onodo.energia+1.4);
//		candidatos[5] = new nodo(onodo, onodo.x-1, onodo.y+1, onodo.energia+1.4);
//		candidatos[6] = new nodo(onodo, onodo.x-1, onodo.y-1, onodo.energia+1.4);
//		candidatos[7] = new nodo(onodo, onodo.x+1, onodo.y-1, onodo.energia+1.4);
		
		for(int i = 0; i < 4; i++){
			nodo ntest = candidatos[i];
			
			if(ntest.x==objetivox&&ntest.y==objetivoy){
				nodosFechados.add(onodo.x+onodo.y*1000000);
				ultimoAberto = onodo;
				return true;
			}
			
			if(ntest.x<0||ntest.y<0||ntest.x>=mapa.Largura||ntest.y>=mapa.Altura){
				continue;
			}
			if(mapa.mapa[ntest.y][ntest.x]==1){
				continue;
			}
			
			boolean igual = false;
			
			synchronized (nodosFechados) {			
//				for(int z = 0; z < nodosFechados.size();z++){
//					nodo nodo2 = nodosFechados.get(z);
//					if(nodo2!=null){
//						if(ntest.x == nodo2.x&&ntest.y==nodo2.y){
//							igual=true;
//							break;
//						}
//					}
//				}
				if(nodosFechados.contains(ntest.x+ntest.y*1000000)) {
					igual=true;
				}
			}
			if(igual){
				continue;
			}
			
			for(int z = 0; z < nodosAbertos.size();z++){
				nodo nodo2 = nodosAbertos.get(z);
				if(ntest.x == nodo2.x&&ntest.y==nodo2.y){
					igual=true;
					break;
				}
			}
			if(igual){
				continue;
			}			
			
			ntest.euristica = calculaheuristica(ntest.x, ntest.y, objetivox, objetivoy);
			nodosAbertos.add(ntest);
			
		}
		
		return false;
	}

	public double calculaheuristica(int x,int y,int objx,int objy){
		double difx =  objx-x;
		double dify =  objy-y;
		return Math.sqrt(difx*difx+dify*dify);
	}
}

class nodo{
	nodo pai;
	int x;
	int y;
	double energia;
	double euristica;
	
	public nodo(nodo pai,int x,int y, double energia) {
		// TODO Auto-generated constructor stub
		this.pai = pai;
		this.x = x;
		this.y = y;
		this.energia = energia;
	}
}