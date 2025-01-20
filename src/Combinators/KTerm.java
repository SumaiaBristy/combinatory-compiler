package Combinators;

import static Webasm.AbstractMachine.K;
import Webasm.Memory;
import java.util.Map;

public class KTerm extends CombinatorTerm {
    
    public KTerm (){ }

    @Override
    public String toStringPrec(int precedence) {
        return "K";
    }

    @Override
    public CombinatorTerm execute(CombinatorProgram combProg) {
        return this;
    }
    
    @Override
    public void storeInMem(Memory mem, Map<String,Integer> labels) {
        int addr = mem.allocate(1);
        mem.storeByte(addr,K);
    } 
    
    @Override
    protected void toASM(ASMBuildStruct asm) {
        asm.text.add("byte " + (int)(K & 0xff));
        asm.size++;
    }
    
}

