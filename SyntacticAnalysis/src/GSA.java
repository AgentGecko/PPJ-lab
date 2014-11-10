import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.util.LinkedList;

import com.sun.xml.internal.bind.v2.schemagen.xmlschema.List;


public class GSA {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		TableState generatedTable = new TableState();
		LinkedList<String> productionRules = new LinkedList<String>();
		String[] terminalSymbols, nonterminalSymbols, synchronizationSymbols;
		String currentProduction = "";
		
		BufferedReader citacReda = new BufferedReader(new InputStreamReader(System.in));
		String line;
		
		line = citacReda.readLine();
		while(line!=null)
		{
			if(line.startsWith("%V"))
			{
				terminalSymbols = line.split(" ");
			}
			else if(line.startsWith("%T"))
			{
				nonterminalSymbols = line.split(" ");
			}
			else if(line.startsWith("%Syn"))
			{
				synchronizationSymbols = line.split(" "); 
			}
			else if(line.startsWith(" "))
			{
				currentProduction += line + "|";
			}
			else
			{
				if(!currentProduction.isEmpty())
					currentProduction = currentProduction.substring(0, currentProduction.length()-1);
				productionRules.add(currentProduction);
				currentProduction = line + "-";
			}
			line = citacReda.readLine();
		}
		currentProduction = currentProduction.substring(0, currentProduction.length()-1);
		productionRules.add(currentProduction);
		productionRules.remove(0);
		
		// TableClass serialization
		FileOutputStream fOut = null;
		ObjectOutputStream oOut = null;

		String outputPath = "analizator/data.ser";
		
		fOut = new FileOutputStream(outputPath);
		oOut = new ObjectOutputStream(fOut);
		oOut.writeObject(generatedTable);
		
		oOut.close();
	}

}
