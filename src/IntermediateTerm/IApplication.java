package IntermediateTerm;

import java.util.Set;

import Combinators.CombinatorTerm;
import Combinators.Application;

public class IApplication implements IntermediateTerm {
    
    private final IntermediateTerm left;
    private final IntermediateTerm right;

    public IApplication (IntermediateTerm left, IntermediateTerm right){
        this.left = left;
        this.right = right;
    }

    @Override
    public Set<String> freeVars() {
        Set<String> result = left.freeVars();
        result.addAll(right.freeVars());
        return result;
    }

    @Override
    public IntermediateTerm translate() {
        return new IApplication(left.translate(), right.translate());
    }

     public IntermediateTerm getLeftTerm() {
        return left;
    }

     public IntermediateTerm getRightTerm() {
        return right;
    }

	@Override
	public CombinatorTerm toCombinatorTerm() {
		return new Application(left.toCombinatorTerm(), right.toCombinatorTerm());
	}
}

