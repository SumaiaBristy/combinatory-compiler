package IntermediateTerm;

import java.util.HashSet;
import java.util.Set;

import Combinators.CombinatorTerm;
import Combinators.NamedTerm;

public class INamedTerm implements IntermediateTerm{
    private final String name;
    
    public INamedTerm(String name){
        this.name = name;
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
		return new NamedTerm(name);
	}

}
