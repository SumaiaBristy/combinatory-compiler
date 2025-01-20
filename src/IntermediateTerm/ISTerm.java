package IntermediateTerm;

import java.util.HashSet;
import java.util.Set;

import Combinators.CombinatorTerm;
import Combinators.STerm;

public class ISTerm implements IntermediateTerm {
    
    public ISTerm (){}

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
		return new STerm();
	}
}

