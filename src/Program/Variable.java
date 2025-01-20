package Program;

import java.util.Map;
import java.util.Set;

import Exceptions.TypingException;
import IntermediateTerm.IVariable;
import IntermediateTerm.IntermediateTerm;
import Types.TypeVariable;
import Types.Type;
import Types.TypeRef;
import java.util.HashMap;
import java.util.List;
import java.util.HashSet;

public class Variable extends PCFTerm {

    private final String name;
    private TypeRef type;

    public Variable (String name){
        this.name = name;
    }

    @Override
    public String toStringPrec(int precedence) {
        return name;
    }

    @Override
    public Map<String,Type> getTypedVars() {
        Map<String,Type> result = new HashMap();
        result.put(name,type.getType());
        return result;
    }

    @Override
    protected void type(VariableGenerator gen, Map<String,TypeRef> env, Program prog) throws TypingException {
    	if (env.containsKey(name)) {
            type = env.get(name);
        } else {             
            type = new TypeRef(new TypeVariable(gen.generateUniqueNumber()));
            env.put(name,type);
        }
    }

    @Override
    public Type getType() {
        return type.getType();
    }

    @Override
    public PCFTerm substitute(String x, PCFTerm prog) {
            if(x.equals(name)) 
                    return prog;
            else return this;
    }

    @Override
    public Set<String> freeVars() {
        Set<String> fVars = new HashSet();
        fVars.add(name);
        return fVars;	
    }

    @Override
    public PCFTerm execute(Program prog) {
        return this;
    }

    @Override
    public IntermediateTerm toIntermediate() {
            return new IVariable(name);
    }
}
