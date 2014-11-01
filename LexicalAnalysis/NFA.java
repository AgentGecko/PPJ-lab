

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;


public class NFA implements Serializable {

	public String name;				// represents name of the lexical entity the NFA recognises
	public State startState;			// start state of the NFA
	public HashSet<State> allStates; 		// all states of the NFA
	public HashSet<State> currentStates;	// list of current states.
	public State finalState;			// final state of the NFA
	public HashSet<String> rules;			// set of rules the lexical analyser has to follow after recognising this lexical entity
	
	// NFA's constructors
	public NFA()
	{
	}
	public NFA(String _name)
	{
		this.name = _name;
	}
	
	
	public void Initialize()
	{
		this.allStates = new HashSet<State>();
		this.currentStates = new HashSet<State>();
		this.rules = new HashSet<String>();
	}
	public void addState(State _state)
	{
		this.allStates.add(_state);
	}
	public void setStartState(State _startState)
	{
		this.startState = _startState;
	}
	public void addRule(String rule)
	{
		this.rules.add(rule);
	}
	
	public void Reset()
	{
		this.currentStates = new HashSet<State>();
		this.currentStates.add(startState);
		this.emptyStringTransition();
	}
	
	//TODO 
	// The nfa should first find all next states with currentChar transitions and then check all empty string transitions
	// The above doesn't apply at the nfa-start ( first add empty string transitions and move along as stated)
	public HashSet<State> nextState(String _currentChar){
		
		HashSet<State> _newList = new HashSet<State>();
		Collection<State> _collection;
		
		for(State currentState : this.currentStates)
		{
			_collection = currentState.nextStates(_currentChar);
			_newList.addAll(_collection);
		}
		if(!_newList.isEmpty())
			return _newList;
		else
			{
				return null;
			}
		
	}
	public void emptyStringTransition()
	{
		HashSet<State> _newList = new HashSet<State>();
		do{_newList.clear();
		for(State currentState : this.currentStates)
		{
			
			for(Transition stateTransition : currentState.stateTransitions)
				if(stateTransition.currentChar.equals("$"))
					if(!currentStates.contains(stateTransition.nextState))
						_newList.add(stateTransition.nextState);
		}
		this.currentStates.addAll(_newList);
		}while(!_newList.isEmpty());
	}
}
