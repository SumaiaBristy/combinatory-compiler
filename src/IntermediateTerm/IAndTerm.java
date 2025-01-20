package IntermediateTerm;

import java.util.HashSet;
import java.util.Set;
import Combinators.AndTerm;
import Combinators.CombinatorTerm;


public class IAndTerm implements IntermediateTerm {

    public IAndTerm() {}
    
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
		return new AndTerm();
	}
}

