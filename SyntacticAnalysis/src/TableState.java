import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


public class TableState implements Serializable{

	Map<String, String> actions;
	Map<String, String> newStates;
	
	
	public TableState()
	{
		this.actions = new HashMap<String,String>();
		this.newStates = new HashMap<String,String>();
	}
	
	public void addNewAction(String _string, String _action)
	{
		this.actions.put(_string, _action);
	}
	public void addNewStates(String _nonterminal, String _newState)
	{
		this.newStates.put(_nonterminal, _newState);
	}


}
