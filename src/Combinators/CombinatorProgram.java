package Combinators;

import java.util.Map;
import java.util.Set;


public class CombinatorProgram {
	
    private final Map<String,CombinatorTerm> terms;

    public CombinatorProgram(Map<String,CombinatorTerm> terms) {
        this.terms = terms;
    }

    public CombinatorTerm execute() {
        if (!terms.containsKey("main")) throw new RuntimeException("No main program.");
        CombinatorTerm term = terms.get("main").execute(this);
        return term;
    }

    public String toASM() {
        StringBuilder result = new StringBuilder();
        for(String name : terms.keySet()) {
            result.append(terms.get(name).toASM("_"+name)) ;
            result.append("\n");
        }
        return result.toString();
    }
    
    public Set<String> getNames() {
        return terms.keySet();
    }

    public CombinatorTerm getTerm(String name) {
        return terms.get(name);
    }
    
    @Override
    public String toString() {
        String result = "";
        for(String name : terms.keySet()) {
            result += name + " = " + terms.get(name) + "\n";
        }
        return result;
    }
    
}
