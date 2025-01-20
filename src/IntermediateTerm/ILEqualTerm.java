package IntermediateTerm;

import java.util.HashSet;
import java.util.Set;

import Combinators.CombinatorTerm;
import Combinators.LEqualTerm;

public class ILEqualTerm implements IntermediateTerm {
	 public ILEqualTerm() {}
	    
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
			return new LEqualTerm();
		}
	}


