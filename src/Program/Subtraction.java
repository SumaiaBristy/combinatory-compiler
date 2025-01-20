package Program;

import Exceptions.TypingException;
import Exceptions.UnificationException;
import IntermediateTerm.IApplication;
import IntermediateTerm.ISubTerm;
import IntermediateTerm.IntermediateTerm;
import Types.IntType;
import Types.Type;
import Types.TypeRef;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class Subtraction extends PCFTerm {
    
    private final PCFTerm left;
    private final PCFTerm right;
    public static final int precedence = 11;

    public Subtraction(PCFTerm left, PCFTerm right){
        this.left = left;
        this.right = right;
    }

    @Override   
    public String toStringPrec(int precedence) {
    		String result = left.toStringPrec(12) + " - " + right.toStringPrec(this.precedence);
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
            Map<String,Type> subst1 = left.getType().unify(new IntType());
            env.forEach((var,t) -> t.substitute(subst1));
            right.type(gen,env,prog);
            try {
                Map<String,Type> subst2 = right.getType().unify(new IntType());
                env.forEach((var,t) -> t.substitute(subst2));
            } catch (UnificationException e) {
                throw new TypingException("Cannot unify " + right + " with Int in -.");
            }
        } catch (UnificationException e) {
            throw new TypingException("Cannot unify " + left + " with Int in -.");
        
        }    
     }

    @Override
    public Type getType() {
        return new IntType();
    }
    
    @Override
	public PCFTerm substitute(String x, PCFTerm prog) {
    	return new Subtraction(left.substitute(x, prog), right.substitute(x, prog));  
	}

	@Override
	public Set<String> freeVars() {
		return null;
	}

    @Override
    public PCFTerm execute(Program prog) {
	PCFTerm updatedLeft = left.execute(prog);
        PCFTerm updatedRight = right.execute(prog);
        if((updatedLeft instanceof IntLiteral lnum) && (updatedRight instanceof IntLiteral rnum)) {
        	//System.out.println("SUB: "+ new IntLiteral(lnum.getNum()-rnum.getNum()));
            return new IntLiteral(lnum.getNum()-rnum.getNum());
        }
        else {
        	//System.out.println("SUB: "+ new Subtraction(updatedLeft, updatedRight));
            return new Subtraction(updatedLeft, updatedRight);
        }
    }
    
	@Override
	public IntermediateTerm toIntermediate() {
		return new IApplication(new IApplication(new ISubTerm(),left.toIntermediate()), right.toIntermediate()); //new IMultTerm(left.toIntermediate(), right.toIntermediate());

	}
}
