package Types;

import java.util.Map;
import java.util.Set;

import Exceptions.UnificationException;

public class ProductType extends Type {
    
    private final Type left;
    private final Type right;
    private static final int precedence = 5;

    public ProductType (Type left, Type right){
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
        return new ProductType(nLeft,nRight);
    }

    @Override   
    public String toStringPrec(int precedence) {
        String result = left.toStringPrec(this.precedence) + "*" + right.toStringPrec(this.precedence);
        if (precedence > this.precedence) {
                result = "(" + result + ")";
        }       
        return result;
    }

    @Override
    protected void unify(Boolean unify, Type other, Map<String, Type> unifier) throws UnificationException {
        if(unify && other instanceof TypeVariable) {
            other.unify(this);
        } else {
            if (other instanceof ProductType pt){
                left.unify(unify, pt.left, unifier);
                right.substitute(unifier).unify(unify, pt.right.substitute(unifier),unifier);
            } else throw new UnificationException("Can not unify " + this + " and " + other);
        }
    }
    
}
