package Program;

import Exceptions.TypingException;
import Exceptions.UnificationException;
import IntermediateTerm.IRecursion;
import IntermediateTerm.IntermediateTerm;
import Types.Type;
import Types.TypeRef;
import Types.TypeVariable;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class Recursion extends PCFTerm{
	
    private final String variable;
    private final PCFTerm body;
    private final int precedence = 3;
    private TypeRef varType;

    public Recursion (String variable, PCFTerm body){
        this.variable = variable;
        this.body = body;
    }

    @Override
    public String toStringPrec(int precedence) {
        String result = "rec" + variable + "." + body.toStringPrec(this.precedence);
        if (precedence > this.precedence) {
            result = "(" + result + ")";
        }
        return result;
    }
    
    @Override
    public Map<String,Type> getTypedVars() {
    	 Map<String,Type> result = body.getTypedVars();
         result.remove(variable);
         return result;
    }

    @Override
    protected void type(VariableGenerator gen, Map<String, TypeRef> env, Program prog) throws TypingException {
    	 Type newVar = new TypeVariable(gen.generateUniqueNumber());
   	     varType = new TypeRef(newVar);
         env.put(variable,varType);
         body.type(gen,env,prog);
         try {
        	 Map<String,Type> subst1 = body.getType().unify(varType.getType());
             env.forEach((var,t) -> t.substitute(subst1));
             env.remove(variable);
             }catch (UnificationException e) {
                 throw new TypingException("Cannot unify " + body + " with "+varType.getType()+" in recursion.");
             }
         }

    @Override
    public Type getType() {
    	return varType.getType();
    }

	@Override
	public PCFTerm substitute(String x, PCFTerm prog) {
		  if(variable.equals(x))
	            return this;
	        else
	        {
	            Set<String> vars = prog.freeVars();
	            if(vars != null && vars.contains(variable)) {
	                VariableGenerator gen = new VariableGenerator();
	                String newVariable = gen.generateUniqueNumber();  
	                return new Recursion(newVariable,body.substitute(variable, new Variable(newVariable)).substitute(x,prog));
	            } else {
	                return new Recursion(variable, body.substitute(x, prog));
	            }
	        }	
	}

	 @Override
	    public Set<String> freeVars() {  
	        Set<String> result = body.freeVars();
	        result.remove(variable);
	        return result;
	    }
	    public PCFTerm getBody() {
	        return body;
	    }

	    public String getVariable() {
	        return variable;
	    }

    @Override
    public PCFTerm execute(Program prog) {
    	//System.out.println("Recursion: "+body.substitute(variable, this).execute(prog));
        return body.substitute(variable, this).execute(prog);
    }

	@Override
	public IntermediateTerm toIntermediate() {
    	//return new IApplication(new IYTerm(),new IAbstraction(variable, body.toIntermediate())).to;
		return new IRecursion(variable, body.toIntermediate());
	}
}
