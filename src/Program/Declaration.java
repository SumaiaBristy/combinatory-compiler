package Program;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Exceptions.UnificationException;
import Exceptions.TypingException;
import Types.Type;
import Types.TypeRef;

public class Declaration {
	
    private final String name;
    private final Type type;
    private final PCFTerm body;

    public Declaration(String name, Type type, PCFTerm body) {
        this.name = name;
        this.type = type;
        this.body = body;
    }

    public String getName() {
        return name;
    }

    public Type getTyp() {
        return type;
    }

    public PCFTerm getBody() {
        return body;
    }

    public void type(Program prog) throws TypingException {
        if (!body.freeVars().isEmpty()) {
            throw new TypingException("Unkown entities " + body.freeVars());
        }
        Map<String,TypeRef> env = new HashMap();
        body.type(new VariableGenerator(),env,prog);
        try {
            Map<String,Type> unifier = body.getType().matchTo(type);
            env.forEach((var,t) -> { t.substitute(unifier); });
        } catch (UnificationException e) {
            throw new TypingException("Cannot unify " + body + " with "+type);
        }
    }

}
