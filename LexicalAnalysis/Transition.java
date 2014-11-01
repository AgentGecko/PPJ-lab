
import java.io.Serializable;


public class Transition implements Serializable{

	public String currentChar;
	public State nextState;
	
	
	public Transition(){}
	public Transition(String _currentChar, State _next)
	{
		this.currentChar = _currentChar;
		this.nextState = _next;
	}
	
	public State getNext()
	{
		return this.nextState;
	}
	public String getCurrentChar()
	{
		return this.currentChar;
	}
}
