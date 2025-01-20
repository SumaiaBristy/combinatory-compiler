package Program;

import Exceptions.TypingException;
import IntermediateTerm.IInt;
import IntermediateTerm.IntermediateTerm;
import Types.IntType;
import Types.Type;
import Types.TypeRef;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class IntLiteral extends PCFTerm {

    private final int num;

    public IntLiteral (int i){
        this.num = i;
    }

    @Override
    public String toStringPrec(int precedence) {
        return String.valueOf(num);
    }
    
    @Override
    public Map<String,Type> getTypedVars() {
        return new HashMap();
    }

    @Override
    protected void type(VariableGenerator gen, Map<String, TypeRef> env, Program prog) throws TypingException {}

    @Override
    public Type getType() {
        return new IntType();
    }

	@Override
	public PCFTerm substitute(String x, PCFTerm prog){
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

	public int getNum() {
		return num;
	}

	@Override
	public IntermediateTerm toIntermediate() {
		return new IInt(num);
	}
    
}
