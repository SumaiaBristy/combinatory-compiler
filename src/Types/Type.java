package Types;

import Exceptions.UnificationException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class Type {
    
    public abstract String toStringPrec(int precedence);
    
    public abstract Set<String> variables();
    
    public abstract Type substitute(Map<String,Type> subst);
    
    protected abstract void unify(Boolean unify,Type other, Map<String,Type> unifier) throws UnificationException;
    
    public Map<String,Type> unify(Type other) throws UnificationException {
        Map<String,Type> unifier = new HashMap();
        unify(true,other,unifier);
        return unifier;
    }
    
    public Map<String, Type> matchTo (Type other) throws UnificationException {
    	Map <String, Type> unifier = new HashMap<>();
    	unify(false,other, unifier);
    	return unifier;
    }
    
    @Override
    public String toString() {
        return toStringPrec(0);
    }	    
}
