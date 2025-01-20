package Program;

import Exceptions.TypingException;
import Exceptions.UnificationException;
import IntermediateTerm.IAndTerm;
import IntermediateTerm.IApplication;
import IntermediateTerm.IntermediateTerm;
import Types.BoolType;
import Types.Type;
import Types.TypeRef;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class And extends PCFTerm {
    
    private final PCFTerm left;
    private final PCFTerm right;
    public static final int precedence = 4;

    public And (PCFTerm left, PCFTerm right){
        this.left = left;
        this.right = right;
    }

    @Override   
    public String toStringPrec(int precedence) {
    		String result = left.toStringPrec(5) + " and " + right.toStringPrec(this.precedence);
	    	if (precedence > this.precedence) {
	    		result = "(" + result + ")";
	    	}
        return result;
    }
    
    @Override
    public Map<String,Type> getTypedVars() {
        Map<String,Type> result = left.getTypedVars();
        result.putAll(right.getTypedVars());
        return result;
    }
        
    @Override
    protected void type(VariableGenerator gen, Map<String, TypeRef> env, Program prog) throws TypingException {
        left.type(gen,env,prog);
        try {
            Map<String,Type> subst1 = left.getType().unify(new BoolType());
            env.forEach((var,t) -> t.substitute(subst1));
            right.type(gen,env,prog);
            try {
                Map<String,Type> subst2 = right.getType().unify(new BoolType());
                env.forEach((var,t) -> t.substitute(subst2));
            } catch (UnificationException e) {
                throw new TypingException("Cannot unify " + right + " with Bool in And.");
            }
        } catch (UnificationException e) {
            throw new TypingException("Cannot unify " + left + " with Bool in And.");
        }
    }
    
    @Override
    public Type getType() {
        return new BoolType();
    }


	@Override
	public PCFTerm substitute(String x, PCFTerm prog) {
		return new And(left.substitute(x, prog), right.substitute(x, prog));  
	}

	@Override
	public Set<String> freeVars() {
		return new HashSet();
	}

    @Override
    public PCFTerm execute(Program prog) {
        PCFTerm updatedLeft = left.execute(prog);
        PCFTerm updatedRight = right.execute(prog);
        
        if((updatedLeft instanceof True) && (updatedRight instanceof True)) {
            return new True();
        } 
        else return new False();
    }

    @Override
	public IntermediateTerm toIntermediate() {
		return new IApplication(new IApplication(new IAndTerm(),left.toIntermediate()), right.toIntermediate()); 
	}
}

