package IntermediateTerm;

import java.util.Set;

import Combinators.CombinatorTerm;
import Combinators.ConditionalTerm;

public class IAbstraction implements IntermediateTerm {

    private final String variable;
    private final IntermediateTerm body;
    
    public IAbstraction (String variable, IntermediateTerm body){
        this.variable = variable;
        this.body = body;
    }

    @Override
    public IntermediateTerm translate() {
        if (body != null && !body.freeVars().contains(variable)) {
            return new IApplication(new IKTerm(),body.translate());
        }
        if (body instanceof IVariable) {
            return new IITerm();
        }
        if (body instanceof IAbstraction abst) { //rule-5
            return new IAbstraction(variable,abst.translate()).translate();
        }
        if (body instanceof IApplication appl) {
            return new IApplication(new IApplication(new ISTerm(),new IAbstraction(variable,appl.getLeftTerm()).translate()),new IAbstraction(variable,appl.getRightTerm()).translate());
        }
//        if (body instanceof IConditionalTerm iCond) {
//    		return new IConditionalTerm(iCond.getConditionalIf(), iCond.getConditionalThen(), iCond.getConditionalElse());
//        }

        return null;
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
