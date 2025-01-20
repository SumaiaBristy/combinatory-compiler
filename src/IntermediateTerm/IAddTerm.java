package IntermediateTerm;

import java.util.HashSet;
import java.util.Set;

import Combinators.AddTerm;
import Combinators.CombinatorTerm;
import Combinators.KTerm;
import Combinators.YTerm;


public class IAddTerm implements IntermediateTerm {

    public IAddTerm() {}
    
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
		return new AddTerm();
	}
}

