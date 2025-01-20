package Types;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import Exceptions.UnificationException;

public class TypeVariable extends Type {
    
    private final String name;

    public TypeVariable (String name){ 
        this.name = name;
    }
    
    @Override
    public Set<String> variables() {
        Set<String> result = new HashSet();
        result.add(name);
        return result;
    }
    
    @Override
    public Type substitute(Map<String,Type> subst) {
        Type result = this;
        if (subst.containsKey(name)) result = subst.get(name);
        return result;
    }

    @Override
    public String toStringPrec(int precedence) {
        return name;
    }

    @Override
    protected void unify(Boolean unify,Type other, Map<String, Type> unifier) throws UnificationException {
        if (!(other instanceof TypeVariable otherVar && otherVar.name.equals(name))) {
            if (other.variables().contains(name))
                throw new UnificationException("Infinite term.");
            else {
                unifier.put(name,other);
                unifier.forEach((var,t) -> t.substitute(unifier));
            }
        }
    }

    public String getName() {
            return name;
    }
    
}
