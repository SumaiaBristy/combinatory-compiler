package IntermediateTerm;

import java.util.HashSet;
import java.util.Set;

import Combinators.AddTerm;
import Combinators.Application;
import Combinators.CombinatorTerm;
import Combinators.KTerm;
import Combinators.MultTerm;
import Combinators.SubTerm;
import Combinators.YTerm;


public class IMultTerm implements IntermediateTerm {

    public IMultTerm (){    }

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
		return new MultTerm();
	}
}

