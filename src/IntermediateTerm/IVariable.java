package IntermediateTerm;

import java.util.HashSet;
import java.util.Set;

import Combinators.CombinatorTerm;
import Combinators.VARIABLE;

public class IVariable implements IntermediateTerm {

    private final String name;
    
    public IVariable (String name){
        this.name = name;
    }
    
    public String getName() {
        return name;
    }

    @Override
    public Set<String> freeVars() {
        Set<String> fVars = new HashSet();
        fVars.add(name);
        return fVars;
    }

    @Override
    public IntermediateTerm translate() {
        return this;
    }

	@Override
	public CombinatorTerm toCombinatorTerm() {
		return new VARIABLE(name);
	}
}

