package Program;

import Exceptions.TypingException;
import Exceptions.UnificationException;
import IntermediateTerm.IApplication;
import IntermediateTerm.IConditionalTerm;
import IntermediateTerm.IntermediateTerm;
import Types.BoolType;
import Types.Type;
import Types.TypeRef;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class Conditional extends PCFTerm {
    
    private final PCFTerm conditionalIf;
    private final PCFTerm conditionalThen;
    private final PCFTerm conditionalElse;
    private final int precedence = 3;

    public Conditional (PCFTerm conditionalIf, PCFTerm conditionalThen, PCFTerm conditionalElse){
        this.conditionalIf = conditionalIf;
        this.conditionalElse = conditionalElse;
        this.conditionalThen = conditionalThen;
    }

    @Override   
    public String toStringPrec(int precedence) {
        String result = " if "+conditionalIf.toStringPrec(this.precedence) + " then " + conditionalThen.toStringPrec(this.precedence) + " else "+
                    conditionalElse.toStringPrec(this.precedence);
        if (precedence > this.precedence) {
                result = "(" + result + ")";
        }
        return result;
    }
    
    @Override
    public Map<String,Type> getTypedVars() {
        Map<String,Type> result = conditionalIf.getTypedVars();
        result.putAll(conditionalThen.getTypedVars());
        result.putAll(conditionalElse.getTypedVars());
        return result;
    }

    @Override
    protected void type(VariableGenerator gen, Map<String,TypeRef> env, Program prog) throws TypingException {
        conditionalIf.type(gen,env,prog);
        try {
            Map<String,Type> subst1 = conditionalIf.getType().unify(new BoolType());
            env.forEach((var,t) -> t.substitute(subst1));
            conditionalThen.type(gen,env,prog);
            conditionalElse.type(gen,env,prog);
            try {
                Map<String,Type> subst2 = conditionalThen.getType().unify(conditionalElse.getType());
                env.forEach((var,t) -> t.substitute(subst2));
            } catch (UnificationException e) {
                throw new TypingException("Cannot unify type of then-case " + conditionalThen + " with type of else-case " + conditionalElse);
            }
        } catch (UnificationException e) {
            throw new TypingException("Conditional If statement expects Bool but getting " + conditionalThen);
        }
    }
    
    @Override
    public Type getType() {
        return conditionalThen.getType();
    }

	@Override
	public PCFTerm substitute(String x, PCFTerm prog){
		PCFTerm updatedIf = conditionalIf.substitute(x, prog);
		PCFTerm updatedThen = conditionalThen.substitute(x, prog);
		PCFTerm updatedElse = conditionalElse.substitute(x, prog);
		return new Conditional(updatedIf, updatedThen, updatedElse);
	}

	@Override
	public Set<String> freeVars() {
		return new HashSet();
	}

    @Override
    public PCFTerm execute(Program prog) {
        PCFTerm updatedIf = conditionalIf.execute(prog);
        if(updatedIf instanceof True) {
        	//System.out.println("ConditionalThen: "+conditionalThen.execute(prog));
            return conditionalThen.execute(prog);
        } else {
            if (updatedIf instanceof False) {
            	//System.out.println("ConditionalElse: "+conditionalElse.execute(prog));
                return conditionalElse.execute(prog);
            } else {
            	//System.out.println("Conditional: "+new Conditional(updatedIf, conditionalThen, conditionalElse));
                return new Conditional(updatedIf, conditionalThen, conditionalElse);
            }
        }
    }

	@Override
	public IntermediateTerm toIntermediate() {
		return new IApplication(new IApplication(new IApplication(new IConditionalTerm(),conditionalIf.toIntermediate())
				,conditionalThen.toIntermediate()),conditionalElse.toIntermediate());
				
		}
}

