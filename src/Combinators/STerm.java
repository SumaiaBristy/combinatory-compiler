package Combinators;

import static Webasm.AbstractMachine.S;
import Webasm.Memory;
import java.util.Map;

public class STerm extends CombinatorTerm {
    
    public STerm() {}

    @Override
    public String toStringPrec(int precedence) {
        return "S";
    }

    @Override
    public CombinatorTerm execute(CombinatorProgram combProg) {
        return this;
    }
    
    @Override
    public void storeInMem(Memory mem, Map<String,Integer> labels) {
        int addr = mem.allocate(1);
        mem.storeByte(addr,S);
    } 
    
    @Override
    protected void toASM(ASMBuildStruct asm) {
        asm.text.add("byte " + (int)(S & 0xff));
        asm.size++;
    }
    
}

