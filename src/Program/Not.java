package Program;

import Exceptions.TypingException;
import Exceptions.UnificationException;
import IntermediateTerm.IApplication;
import IntermediateTerm.INotTerm;
import IntermediateTerm.IntermediateTerm;
import Types.BoolType;
import Types.Type;
import Types.TypeRef;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Not extends PCFTerm {
    
    private final PCFTerm body;
    public static final int precedence = 13;
    
    public Not (PCFTerm prog){
        this.body = prog;
    }

    @Override   
    public String toStringPrec(int precedence) {
        String result = " not " + body ;
        if (precedence > this.precedence) {
            result = "(" + result + ")";
        }
        return result;
    }
    
    @Override
    public Map<String,Type> getTypedVars() {
        Map<String,Type> result = body.getTypedVars();
        //result.putAll(right.getTypedVars());
        return result;
    }

    @Override
    protected void type(VariableGenerator gen, Map<String, TypeRef> env, Program prog) throws TypingException {
        body.type(gen,env,prog);
        try {
            Map<String,Type> subst1 = body.getType().unify(new BoolType());
            env.forEach((var,t) -> t.substitute(subst1));
        } catch (UnificationException e) {
            throw new TypingException("Cannot unify " + body + " with Bool in Not.");
        }
    }

    @Override
    public Type getType() {
        return new BoolType();
    }

    @Override
	public PCFTerm substitute(String x, PCFTerm subs) {
    	return new Not(body.substitute(x, subs));  
	}
	@Override
	public Set<String> freeVars() {
		return new HashSet();
	}

    @Override
    public PCFTerm execute(Program prog) {
    	
        PCFTerm updatedProg = body.execute(prog);
        if(updatedProg instanceof True) {
            return new False();
        } 
        else return new True();
    }

    @Override
	public IntermediateTerm toIntermediate() {
		return new IApplication(new INotTerm(),body.toIntermediate()); 
	}
}

