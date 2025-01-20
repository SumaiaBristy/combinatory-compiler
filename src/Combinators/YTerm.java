package Combinators;

import static Webasm.AbstractMachine.Y;
import Webasm.Memory;
import java.util.Map;

public class YTerm extends CombinatorTerm {
        
    public YTerm (){ }

    @Override
    public String toStringPrec(int precedence) {
        return "Y";
    }

    @Override
    public CombinatorTerm execute(CombinatorProgram combProg) {
        return this;
    }
    
    @Override
    public void storeInMem(Memory mem, Map<String,Integer> labels) {
        int addr = mem.allocate(1);
        mem.storeByte(addr,Y);
    } 
    
    @Override
    protected void toASM(ASMBuildStruct asm) {
        asm.text.add("byte " + (int)(Y & 0xff));
        asm.size++;
    }
    
}

