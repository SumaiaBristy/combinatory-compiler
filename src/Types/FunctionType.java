package Types;

import java.util.Map;
import java.util.Set;

import Exceptions.UnificationException;

public class FunctionType extends Type {
    
    private final Type left;
    private final Type right;
    private static final int precedence = 3;

    public FunctionType (Type left, Type right){
        this.left = left;
        this.right = right;
    }
    
    @Override
    public Set<String> variables() {
        Set<String> result = left.variables();
        result.addAll(right.variables());
        return result;
    }
    
    @Override
    public Type substitute(Map<String,Type> subst) {
        Type nLeft = left.substitute(subst);
        Type nRight = right.substitute(subst);
        return new FunctionType(nLeft,nRight);
    }

    @Override   
    public String toStringPrec(int precedence) {
        String result = left.toStringPrec(4) + "->" + right.toStringPrec(this.precedence);
        if (precedence > this.precedence) {
                result = "(" + result + ")";
        }       
        return result;
    }

    @Override
    protected void unify(Boolean unify,Type other, Map<String, Type> unifier) throws UnificationException {
        if(unify && other instanceof TypeVariable) {
            other.unify(this);
        } else {
            if (other instanceof FunctionType ft){
                left.unify(unify, ft.left,unifier);
                right.substitute(unifier).unify(unify, ft.right.substitute(unifier),unifier);
            } else throw new UnificationException("Can not unify " + this + " and " + other);
        }
    }
    public Type getParamType() {
    	return left;
    }
    
    public Type getResultType() {
    	return right;
    }
            
}

