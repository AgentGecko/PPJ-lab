
import java.io.Serializable;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;


public class State implements Serializable {

	public String name;						// A state holds it's name
	public HashSet<Transition> stateTransitions;	// A state holds all of it's transitions
	
	// Class Constructors
	public State()
	{
		this.stateTransitions = new HashSet<Transition>();
	}
	public State(String _name)
	{
		this.name = _name;
		this.stateTransitions = new HashSet<Transition>();
	}

	// Method for adding transitions to a state
	public void addTransition(Transition _newTransition)
	{
		this.stateTransitions.add(_newTransition);
	}
	
	// Returns a list of all states the current state can transit into with the current input char
	public HashSet<State> nextStates(String _char)
	{
		HashSet<State> _nextStates = new HashSet<State>();
		for(Transition tran : this.stateTransitions)
		{
			if(tran.currentChar.equals(_char))
				_nextStates.add(tran.nextState);
		}
		return _nextStates;
	}
}
