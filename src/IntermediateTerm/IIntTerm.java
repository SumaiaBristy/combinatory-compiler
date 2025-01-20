package IntermediateTerm;

import java.util.HashSet;
import java.util.Set;

import Combinators.CombinatorTerm;
import Combinators.INTTerm;


public class IIntTerm implements IntermediateTerm {

	private final int num;
	
    public IIntTerm(int i) {
    	this.num = i;
    }
    
    @Override
    public Set<String> freeVars() {
        return new HashSet();
    }
    
    public int getNum() {
		return num;
	}

    @Override
    public IntermediateTerm translate() {
        return this;
    }
	@Override
	public CombinatorTerm toCombinatorTerm() {
		return new INTTerm(num);
	}
}

