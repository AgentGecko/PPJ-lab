
import java.util.LinkedList;
import java.util.List;

public class Creator {

	// Method which adds a new state to a specified automata and returns the new created state
	public static State newState(NFA automat) {
		State a;
		if(automat.allStates.isEmpty())
		{
			a = new State(automat.name + "_" + 0);
		}
		else
		{
			a = new State(automat.name + "_" +  Integer.toString(automat.allStates.size()));
		}
		automat.addState(a);
		return a;
	}

	// Method which checks whether an char is a regular char or in fact an operator
	public static boolean isAnOperator(String regEx, int counter) {
		int count = 0;
		while (counter - 1 >= 0 && regEx.charAt(counter - 1) == '\\') {
			count++;
			counter--;
		}

		return count % 2 == 0;
	}

	// Method which "interprets" a regEx - breaks it into tiny standalone regular expression parts
	// and adds a needed number of states and connects them in the proper way 
	public static StatePair transform(String regEx, NFA automat) {
		List<String> _reg = new LinkedList<String>();
		int ORcounter = 0;
		boolean ORFound = false;
		int bracketCounter = 0;
		for(int i = 0 ; i< regEx.length() ; i++)
		{
			if(regEx.charAt(i) == '(' && isAnOperator(regEx,i))
				bracketCounter ++;
			else if(regEx.charAt(i) == ')' && isAnOperator(regEx,i))
				bracketCounter --;
			else if(bracketCounter == 0 && regEx.charAt(i) == '|' && isAnOperator(regEx,i))
			{
				if(ORcounter == 0)
					_reg.add(regEx.substring(ORcounter, i));
				else
					_reg.add(regEx.substring(ORcounter+1, i));
				ORcounter = i;
				ORFound=true;
			}
		}
		if(ORFound)
		{
			_reg.add(regEx.substring(ORcounter+1));
		}
		
		State left = newState(automat);
		State right = newState(automat);
		
		if(ORFound)
		{
			for(int i =0; i< _reg.size(); i++)
			{
				StatePair temporaryPair = transform(_reg.get(i),automat);
				AddEmptyStringTransition(left, temporaryPair.first);
				AddEmptyStringTransition(temporaryPair.second, right);
			}
		}
		else
		{
			boolean prefix = false;
			State lastState = left;
			State a,b;
			char _char ;
			for(int i = 0; i < regEx.length(); i++)
			{
				_char = regEx.charAt(i);
				if(prefix == true)
				{
					prefix = false;
					String transitChar;
					
					if( _char == 't' )
						transitChar = Character.toString('\t');
					else if( _char == 'n' )
						transitChar = Character.toString('\n');
					else if( _char == '_' )
						transitChar = Character.toString(' ');
					else
						transitChar = Character.toString(regEx.charAt(i));
					a = newState(automat);
					b = newState(automat);
					AddTransition(transitChar,a,b);
				}
				else
				{
					if( _char == '\\')
					{
						prefix = true;
						continue;
					}
					if( _char != '(')
					{
						a = newState(automat);
						b = newState(automat);
						if( _char == '$')
							AddEmptyStringTransition(a,b);
						else
							AddTransition(Character.toString(_char),a,b);
					}
					else
					{ 
						int j = findClosingBracket(i,regEx);
						StatePair temporary = transform(regEx.substring(i+1, j),automat);
						a = temporary.first;
						b = temporary.second;
						i=j;
					}
				}
				if( i+1 < regEx.length() && regEx.charAt(i+1) == '*')
				{
					State x = a;
					State y = b;
					a = newState(automat);
					b = newState(automat);
					AddEmptyStringTransition(a,x);
					AddEmptyStringTransition(y,b);
					AddEmptyStringTransition(a,b);
					AddEmptyStringTransition(y,x);
					i++;
				}
				AddEmptyStringTransition(lastState,a);
				lastState = b;
			}
			AddEmptyStringTransition(lastState,right);
		}
		
		return new StatePair(left,right);
	}
	
	// Methods for adding transitions to a pair of states
	public static void AddEmptyStringTransition(State first, State second)
	{
		first.addTransition(new Transition("$", second));
	}
	public static void AddTransition(String _char,State first, State second)
	{
		first.addTransition(new Transition(_char, second));
	}
	public static int findClosingBracket(int startLocation, String string)
	{
		int counter = 0,location=-1;
		for(int i = startLocation; i < string.length() ; i++)
		{
			if(string.charAt(i) == '(' && isAnOperator(string,i))
				counter++;
			if(string.charAt(i) == ')' && isAnOperator(string,i))
			{
				counter--;
				if(counter == 0)
					{location = i;break;}
			}
		}
		return location;
	}
}

