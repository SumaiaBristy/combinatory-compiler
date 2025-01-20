package IntermediateTerm;

import java.util.HashSet;
import java.util.Set;

import Combinators.Application;
import Combinators.CombinatorTerm;
import Combinators.ConditionalTerm;
import Combinators.EqualTerm;
import Program.Conditional;
import Program.False;
import Program.PCFTerm;
import Program.True;


public class IConditionalTerm implements IntermediateTerm {
	 public IConditionalTerm() {}
	    
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
			return new ConditionalTerm();
		}
	}
