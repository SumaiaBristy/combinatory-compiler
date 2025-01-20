package IntermediateTerm;

import java.util.Set;

import Combinators.CombinatorTerm;

public interface IntermediateTerm {
    public abstract Set<String> freeVars();
    public abstract IntermediateTerm translate();
    public abstract CombinatorTerm toCombinatorTerm();
}
