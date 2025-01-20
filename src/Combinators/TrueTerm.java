package Combinators;

import static Webasm.AbstractMachine.BOOL;
import Webasm.Memory;
import java.util.Map;

public class TrueTerm extends CombinatorTerm {
 
    public TrueTerm (){  }

    @Override
    public String toStringPrec(int precedence) {
        return "TRUE";
    }
    
    @Override
    public CombinatorTerm execute(CombinatorProgram combProg) {
        return this;
    }
    
    @Override
    public void storeInMem(Memory mem, Map<String,Integer> labels) {
        int addr = mem.allocate(2);
        mem.storeByte(addr,BOOL);
        mem.storeByte(addr+1,(byte)1);
    }
    
    @Override
    protected void toASM(ASMBuildStruct asm) {
        asm.text.add("byte " + BOOL);
        asm.text.add("byte " + 1);
        asm.size += 2;
    }
    
}


