package IntermediateTerm;

import java.util.HashSet;
import java.util.Set;

import Combinators.CombinatorTerm;
import Combinators.ITerm;


public class IITerm implements IntermediateTerm {
    
    public IITerm() {}

    @Override
    public Set<String> freeVars() {
        return new HashSet();
    }
    
    @Override
    public IntermediateTerm translate() {
        return this;
    }

	@Override
	public CombinatorTerm toCombinatorTerm() {
		return new ITerm();
	}
}

