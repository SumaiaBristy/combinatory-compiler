package Program;

import Exceptions.TypingException;
import IntermediateTerm.IFalseTerm;
import IntermediateTerm.IntermediateTerm;
import Types.BoolType;
import Types.Type;
import Types.TypeRef;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class False extends PCFTerm {

    public False (){  }

    @Override
    public String toStringPrec(int precedence) {
        return "False";
    }
    
    @Override
    public Map<String,Type> getTypedVars() {
        return new HashMap();
    }

    @Override
    protected void type(VariableGenerator gen, Map<String, TypeRef> env, Program prog) throws TypingException {}

    @Override
    public Type getType() {
        return new BoolType();
    }

	@Override
	public PCFTerm substitute(String x, PCFTerm prog) {
		return this;
	}

	@Override
	public Set<String> freeVars() {
		return new HashSet();
	}

    @Override
    public PCFTerm execute(Program prog) {
        return this;
    }

	@Override
	public IntermediateTerm toIntermediate() {
		return new IFalseTerm();
	}
}


