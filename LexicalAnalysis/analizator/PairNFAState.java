
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

// Class to represent LA's states and the list of automata which can operate in that LA state
public class PairNFAState implements Serializable {
	
	public String stateName;	// Name of the LA state
	public List<NFA> nfaList; 	// List of automata which can operate only while that state is active
	
	// Class constructors
	public PairNFAState(){}
	public PairNFAState(String _stateName){
		this.nfaList = new LinkedList<NFA>();
		this.stateName = _stateName;
	}
	public PairNFAState(String _stateName, List<NFA> _nfaList)
	{
		this.nfaList = new LinkedList<NFA>();
		this.nfaList = _nfaList;
		this.stateName = _stateName;
	}
	
	
	//  Adds a created NFA to the list
	public void addNFA(NFA _nfa)
	{
		this.nfaList.add(_nfa);
	}
}
