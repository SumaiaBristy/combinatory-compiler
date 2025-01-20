package Combinators;

import static Webasm.AbstractMachine.BOOL;
import Webasm.Memory;
import java.util.Map;

public class FalseTerm extends CombinatorTerm {
        
    public FalseTerm (){  }

    @Override
    public String toStringPrec(int precedence) {
        return "FALSE";
    }
    
    @Override
    public CombinatorTerm execute(CombinatorProgram combProg) {
        return this;
    }
    
    @Override
    public void storeInMem(Memory mem, Map<String,Integer> labels) {
        int addr = mem.allocate(2);
        mem.storeByte(addr,BOOL);
        mem.storeByte(addr+1,(byte)0);
    }
    
    @Override
    protected void toASM(ASMBuildStruct asm) {
        asm.text.add("byte " + BOOL);
        asm.text.add("byte " + 0);
        asm.size += 2;
    }
    
}


