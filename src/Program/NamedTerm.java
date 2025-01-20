package Program;

import Exceptions.TypingException;
import IntermediateTerm.INamedTerm;
import IntermediateTerm.IntermediateTerm;
import Types.Type;
import Types.TypeRef;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class NamedTerm extends PCFTerm {
    
    private final String name;
    private Declaration declaration;

    public NamedTerm(String name){
        this.name = name;
    }

    @Override
    public String toStringPrec(int precedence) {
        return name;
    }

    @Override
    public Map<String,Type> getTypedVars() {
        return new HashMap();
    }

    @Override
    protected void type(VariableGenerator gen, Map<String,TypeRef> env, Program prog) throws TypingException {
    	declaration = prog.getDeclaration(name);
    }

    @Override
    public Type getType() {
        return declaration.getTyp();
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
    	//System.out.println(declaration.getName()+": "+declaration.getBody().execute(prog));
        return declaration.getBody().execute(prog);
    }
    
    @Override
    public IntermediateTerm toIntermediate() {
        return new INamedTerm(name);
    }
    
}
