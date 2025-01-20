package IntermediateTerm;

import java.util.HashSet;
import java.util.Set;

import Combinators.CombinatorTerm;
import Combinators.INTTerm;
import Combinators.VARIABLE;

public class IInt implements IntermediateTerm {

    private final int num;
    
    public IInt (int i){
        this.num = i;
    }
    
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
		return new INTTerm(num);
	}
}

