import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

public class FuzzyMain {
	public static void main(String[] args) {
		
		VariavelFuzzy muitoBarato = new VariavelFuzzy("Muito Barato", 0, 0, 10, 20);
		VariavelFuzzy barato = new VariavelFuzzy("Barato", 10, 20, 30, 60);
		VariavelFuzzy cmedio = new VariavelFuzzy("Custo Medio", 20, 40, 50, 70);
		VariavelFuzzy caro = new VariavelFuzzy("Caro", 40, 60, 70, 120);
		VariavelFuzzy muitocaro = new VariavelFuzzy("Muito Caro", 70, 110, 500, 500);
		
		GrupoVariaveis grupoPreco = new GrupoVariaveis();
		grupoPreco.add(muitoBarato);
		grupoPreco.add(barato);
		grupoPreco.add(cmedio);
		grupoPreco.add(caro);
		grupoPreco.add(muitocaro);
		
		GrupoVariaveis grupoRating = new GrupoVariaveis();
		grupoRating.add(new VariavelFuzzy("MR",0,0,10,20));
		grupoRating.add(new VariavelFuzzy("R",10,20,30,40));
		grupoRating.add(new VariavelFuzzy("B",20,40,45,50));
		grupoRating.add(new VariavelFuzzy("MB",40,48,50,50));
		
		GrupoVariaveis grupoAtratividade = new GrupoVariaveis();
		grupoRating.add(new VariavelFuzzy("NA",0,0,3,6));
		grupoRating.add(new VariavelFuzzy("A",5,7,8,10));
		grupoRating.add(new VariavelFuzzy("MA",7,9,10,10));

		
		try {
			BufferedReader bfr = new BufferedReader(new FileReader(new File("restaurantes_filtrados.csv")));
			
			String header = bfr.readLine();
			String splitheder[] = header.split(";");
			for (int i = 0; i < splitheder.length;i++) {
				System.out.println(""+i+" "+splitheder[i]);
			}
			
			String line = "";
			
			while((line=bfr.readLine())!=null) {
				String spl[] = line.split(";");
				HashMap<String,Float> asVariaveis = new HashMap<String,Float>();
				
				float custodinheiro = Float.parseFloat(spl[3]);
				grupoPreco.fuzzifica(custodinheiro, asVariaveis);
				
				float rating = Float.parseFloat(spl[5]);
				grupoRating.fuzzifica(rating, asVariaveis);
				
				//System.out.println("custodinheiro "+custodinheiro+" -> "+asVariaveis);
				//System.out.println("rating "+rating+" -> "+asVariaveis);
				
				// Barato e B -> A
				// Muito Barato e B -> A
				// Muito Barato e MB -> MA
				// Barato e MB -> MA
				// Barato e R -> NA
				// Muito Barato e R -> A
				// Muito Barato e MR -> NA
				
				rodaRegraE(asVariaveis,"Barato","B","A");
				rodaRegraE(asVariaveis,"Muito Barato","B","A");
				rodaRegraE(asVariaveis,"Muito Barato","MB","MA");
				rodaRegraE(asVariaveis,"Barato","MB","MA");
				rodaRegraE(asVariaveis,"Barato","R","NA");
				rodaRegraE(asVariaveis,"Muito Barato","R","A");
				rodaRegraE(asVariaveis,"Muito Barato","MR","NA");
				rodaRegraE(asVariaveis,"Muito Caro","MR","NA");
				rodaRegraE(asVariaveis,"Muito Caro","R","NA");
				rodaRegraE(asVariaveis,"Muito Caro","B","NA");
				rodaRegraE(asVariaveis,"Muito Caro","MB","A");
				
				float NA = asVariaveis.get("NA");
				float A = asVariaveis.get("A");
				float MA = asVariaveis.get("MA");
				
				float score = (NA*1.5f+A*7.0f+MA*9.5f)/(NA+A+MA);
				
				System.out.println(" "+custodinheiro+" "+rating +"-> "+score);
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	private static void rodaRegraE(HashMap<String, Float> asVariaveis,String var1,String var2,String varr) {
		float v = Math.min(asVariaveis.get(var1),asVariaveis.get(var2));
		if(asVariaveis.keySet().contains(varr)) {
			float vatual = asVariaveis.get(varr);
			asVariaveis.put(varr, Math.max(vatual, v));
		}else {
			asVariaveis.put(varr, v);
		}
	}
}
