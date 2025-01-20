package IntermediateTerm;

import java.util.HashSet;
import java.util.Set;

import Combinators.AddTerm;
import Combinators.Application;
import Combinators.CombinatorTerm;
import Combinators.EqualTerm;

public class IEqualTerm implements IntermediateTerm {
	 public IEqualTerm() {}
	    
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
			return new EqualTerm();
		}
	}


