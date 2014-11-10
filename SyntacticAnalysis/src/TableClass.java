import java.util.HashMap;
import java.util.HashSet;


public class TableClass {

	HashSet<TableState> states;
	
	public void TableClass()
	{
		this.states = new HashSet<TableState>();
	}
	
	public void addTableState(TableState _state)
	{
		this.states.add(_state);
	}
}
