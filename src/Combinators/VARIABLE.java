package Combinators;

import Webasm.Memory;
import java.util.Map;

public class VARIABLE extends CombinatorTerm {

    private final String name;

    public VARIABLE (String name){
        this.name = name;
    }

    @Override
    public String toStringPrec(int precedence) {
        return name;
    }

    @Override
    public CombinatorTerm execute(CombinatorProgram combProg) {
            return this;
    }
    
    @Override
    public void storeInMem(Memory mem, Map<String,Integer> labels) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    protected void toASM(ASMBuildStruct asm) {
        throw new UnsupportedOperationException();
    }
    
}
