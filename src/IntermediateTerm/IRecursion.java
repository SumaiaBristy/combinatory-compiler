package IntermediateTerm;

import java.util.Set;

import javax.management.InvalidApplicationException;

import Combinators.CombinatorTerm;

public class IRecursion implements IntermediateTerm {

    private final String variable;
    private final IntermediateTerm body;
    
    public IRecursion (String variable, IntermediateTerm body){
        this.variable = variable;
        this.body = body;
    }

    @Override
    public IntermediateTerm translate() {
    	return new IApplication(new IYTerm(),new IAbstraction(variable,body).translate());
    }

    @Override
    public Set<String> freeVars() {  
    	Set<String> result = body.freeVars();
        result.remove(variable);
        return result;
    }

    public IntermediateTerm getBody() {
        return body;
    }

    public String getVariable() {
        return variable;
    }

	@Override
	public CombinatorTerm toCombinatorTerm() {
        throw new UnsupportedOperationException();
	}
}
