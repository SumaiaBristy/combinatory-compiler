package IntermediateTerm;

import java.util.HashSet;
import java.util.Set;

import Combinators.CombinatorTerm;
import Combinators.KTerm;
import Combinators.TrueTerm;
import Combinators.YTerm;


public class ITrueTerm implements IntermediateTerm {

    public ITrueTerm() {}
    
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
		return new TrueTerm();
	}
}

