package Program;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import Exceptions.TypingException;
import Exceptions.UnificationException;
import IntermediateTerm.IApplication;
import IntermediateTerm.IntermediateTerm;
import Types.FunctionType;
import Types.Type;
import Types.TypeRef;
import Types.TypeVariable;

public class Application extends PCFTerm{
	
    private final PCFTerm left;
    private final PCFTerm right;
    private final int precedence = 3;

    public Application (PCFTerm left, PCFTerm right){
        this.left = left;
        this.right = right;
    }

    @Override   
    public String toStringPrec(int precedence) {
        String result = left.toStringPrec(4) + " " + right.toStringPrec(this.precedence);
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
	Map<String,Type> subst = new HashMap<>();
    	left.type(gen,env,prog);
    	if(left.getType() instanceof TypeVariable tv) {
            FunctionType ft = new FunctionType(tv,new TypeVariable(gen.generateUniqueNumber()));
            subst.put(tv.getName(), ft);
            env.forEach((var,t) -> t.substitute(subst));
    	} 
    	if(left.getType() instanceof FunctionType ft) {
            right.type(gen,env,prog);
            try {
                Map<String,Type> subst1 = right.getType().unify(ft.getParamType());
                env.forEach((var,t) -> t.substitute(subst1));
            } catch (UnificationException e) {
                throw new TypingException("Cannot unify " + right + " with "+left+ " in Application.");
            }
    	}
    	else {
            throw new TypingException(left +"must be either a funtion type or variable!");
    	}
    }

    @Override
    public Type getType() {
    	if(left.getType() instanceof FunctionType ft)
    		return ft.getResultType();
    	else return null;
}

	@Override
	public PCFTerm substitute(String x, PCFTerm prog) {
		return new Application(left.substitute(x, prog), right.substitute(x, prog));  
	}

	@Override
	public Set<String> freeVars() {
		Set<String> result = left.freeVars();
	    result.addAll(right.freeVars());
	    return result;
	}

    @Override
    public PCFTerm execute(Program prog) {
    	//System.out.println("Application: "+left.execute(prog));
        PCFTerm nLeft = left.execute(prog);
        if (nLeft instanceof Abstraction abst) {
        	//System.out.println("Application: "+abst.getBody().substitute(abst.getVariable(), right).execute(prog));
            return abst.getBody().substitute(abst.getVariable(), right).execute(prog);
        } else {
        	//System.out.println("Application: "+new Application(nLeft, right));
            return new Application(nLeft, right);
        } 	
    }

	@Override
	public IntermediateTerm toIntermediate() {
		return new IApplication(left.toIntermediate(), right.toIntermediate());
	} 
}
