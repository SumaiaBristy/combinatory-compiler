package Program;

import Exceptions.TypingException;
import IntermediateTerm.IAbstraction;
import IntermediateTerm.IntermediateTerm;
import Types.FunctionType;
import Types.Type;
import Types.TypeRef;
import Types.TypeVariable;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class Abstraction extends PCFTerm{
	
    private final String variable;
    private final PCFTerm body;
    private final int precedence = 3;
    private TypeRef varType;

    public Abstraction (String variable, PCFTerm body){
        this.variable = variable;
        this.body = body;
    }

    @Override
    public String toStringPrec(int precedence) {
        String result = "\u03BB" + variable + "." + body.toStringPrec(this.precedence);
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
          env.remove(variable);
    }

    @Override
    public Type getType() {
    	FunctionType ft = new FunctionType(varType.getType(), body.getType());
    	return ft;
    }

	@Override
	public PCFTerm substitute(String x, PCFTerm prog) {
		  if(variable.equals(x))
	            return this;
	        else
	        {
	            Set<String> vars = prog.freeVars();
	            if( vars!= null && vars.contains(variable)) {
	                VariableGenerator gen = new VariableGenerator();
	                String newVariable = gen.generateUniqueNumber();  
	                return new Abstraction(newVariable,body.substitute(variable, new Variable(newVariable)).substitute(x,prog));
	            } else {
	                return new Abstraction(variable, body.substitute(x, prog));
	            }
	        }
		 }

	  @Override
	    public Set<String> freeVars() {  
	        Set<String> result = body.freeVars();
	        if(result != null)
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
    	//System.out.println("Abstraction: "+this);
        return this;
    }

	@Override
	public IntermediateTerm toIntermediate() {
		return new IAbstraction(variable, body.toIntermediate());
	}	
}
