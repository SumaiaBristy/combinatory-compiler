package Program;

import Exceptions.TypingException;
import Exceptions.UnificationException;
import IntermediateTerm.IApplication;
import IntermediateTerm.IEqualTerm;
import IntermediateTerm.IntermediateTerm;
import Types.BoolType;
import Types.IntType;
import Types.Type;
import Types.TypeRef;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Equal extends PCFTerm {
    
    private final PCFTerm left;
    private final PCFTerm right;
    public static final int precedence = 8;

    public Equal (PCFTerm left, PCFTerm right){
        this.left = left;
        this.right = right;
    }

    @Override   
    public String toStringPrec(int precedence) {
    		String result = left.toStringPrec(9) + " = " + right.toStringPrec(this.precedence);
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
                throw new TypingException("Cannot unify " + right + " with Int in =.");
            }
        } catch (UnificationException e) {
            throw new TypingException("Cannot unify " + left + " with Int in =.");
        }
    }

    @Override
    public Type getType() {
        return new BoolType();
    }

    @Override
	public PCFTerm substitute(String x, PCFTerm prog){
		return new Equal(left.substitute(x, prog), right.substitute(x, prog));  
	}


	@Override
	public Set<String> freeVars() {
		return new HashSet();
	}

    @Override
    public PCFTerm execute(Program prog) {
	PCFTerm updatedLeft = left.execute(prog);
        PCFTerm updatedRight = right.execute(prog);
        if((updatedLeft instanceof IntLiteral lnum) && (updatedRight instanceof IntLiteral rnum)) {
        	if(lnum.getNum()==rnum.getNum()) {
            	//System.out.println("Equal: "+ new True());
        		return new True();
        	}
        	else {
            	//System.out.println("Equal: "+ new False());
        		return new False();
        	}
        }
        else {
        	//System.out.println("Equal: "+ new Equal(updatedLeft, updatedRight));
            return new Equal(updatedLeft, updatedRight);
        }
    }

	@Override
	public IntermediateTerm toIntermediate() {
		return new IApplication(new IApplication(new IEqualTerm(),left.toIntermediate()), right.toIntermediate()); 
	}
}

