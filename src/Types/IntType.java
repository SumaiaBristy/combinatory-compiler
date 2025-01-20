package Types;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import Exceptions.UnificationException;

public class IntType extends Type {

    public IntType (){  }
    
    @Override
    public Set<String> variables() {
        return new HashSet();
    }
    
    @Override
    public Type substitute(Map<String,Type> subst) {
        return this;
    }

    @Override
    public String toStringPrec(int precedence) {
        return "Int";
    }

    @Override
    protected void unify(Boolean unify,Type other, Map<String, Type> unifier) throws UnificationException {
       if(unify && other instanceof TypeVariable) {
            other.unify(unify, this,unifier);
        } else {
            if(!(other instanceof IntType)) 
                throw new UnificationException("Can not unify " + this + " and " + other);
       }
    }
    
}


