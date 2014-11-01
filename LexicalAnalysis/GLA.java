
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;


public class GLA {

	public static void main(String[] args) throws IOException  {
		// TODO Auto-generated method stub
		// Need to match the exact formatting of the input file

		// Defining variables and objects in which to store data
		BufferedReader citacReda = new BufferedReader(new InputStreamReader(System.in));
		String line;
		List<CharObject> myCharObjects = new LinkedList<CharObject>();
		List<PairNFAState> LAStructures = new LinkedList<PairNFAState>();
		
		// Reading names for groups and characters which are in the specified group
		while(!(line = citacReda.readLine()).startsWith("%"))
		{
			 CharObject a = new CharObject();
			 a.name = line.split(" ")[0];
			 a.allowedValues = line.split(" ")[1];
			 for(CharObject some : myCharObjects)
			 {
				 if(a.allowedValues.contains(some.name))
					 a.allowedValues = a.allowedValues.replace(some.name, "(" + some.allowedValues + ")");
					 
			 }
			 myCharObjects.add(a);
		}
		
		// Saving possible states
		line = line.replace("%X ", "");
		String[] stanja = line.split(" ");
		
		// At this point we know the number of possible states, so we are creating the Pair's already
		line = citacReda.readLine();
		for(int i = 0; i < stanja.length; i++)
		{
			LAStructures.add(new PairNFAState(stanja[i]));
		}
		// Saving possible lexical entity
		line = line.replace("%L ", "");
		String[] leksJedinke = line.split(" ");
		
		
		// Reading the set of rules which must be followed
		line = citacReda.readLine();
		while(line!=null)
		{
			if(line.contains("<"))
			{
				
				for(PairNFAState c : LAStructures)
				{
					// Find me the LA state named like one in the rule I just read
					if(c.stateName.equals(line.split(">")[0].replace("<", "")))
					{
						
						NFA automat = new NFA();
						automat.Initialize();
						String helper;
						StatePair firstLast;
							
						helper = line.substring(line.indexOf('>')+1);
						for(CharObject a : myCharObjects)
						{
								if(helper.contains(a.name))
									helper = helper.replace(a.name, "("+ a.allowedValues+")" );
								
						}
						firstLast = Creator.transform(helper, automat);
						automat.finalState = firstLast.second;
						automat.startState = firstLast.first;
						line = citacReda.readLine();
						// After the first line of the rule we know there are one or more lines
						// that describe what needs to be done and those lines belong to 
						// this rule. We are checking what else is there in the rule before the }
						// character which marks the end of a rule. 
						// Everything but the lexical entity name is saved in automata.rules
						// Lexical entity name is saved in the automata.name
						while(!line.equalsIgnoreCase("}"))
						{
							if(line.contains("NOVI_REDAK"))
							{
								automat.rules.add(line.trim());
							}
							else if(line.contains("UDJI_U_STANJE"))
							{
								automat.rules.add(line.trim());
							}
							else if(line.contains("VRATI_SE"))
							{
								automat.rules.add(line.trim());
							}
							else if(line.contains("-"))
							{
								automat.rules.add(line.trim());
							}
							else
							{
								automat.name = line.trim();
								if(automat.name.equals("{"))
								{
									automat.name = "none";
								}
							}
							line =  citacReda.readLine();
						}
						automat.currentStates.add(automat.startState);
						automat.emptyStringTransition();
						c.addNFA(automat);
					}
				}
				
			}
			line = citacReda.readLine();
		}
		FileOutputStream fOut = null;
		ObjectOutputStream oOut = null;

		String outputPath = "analizator/data.ser";
		
		fOut = new FileOutputStream(outputPath);
		oOut = new ObjectOutputStream(fOut);
		oOut.writeObject(LAStructures);
		
		oOut.close();

	}

}

