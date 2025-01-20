package Combinators;

import static Webasm.AbstractMachine.MUL;
import Webasm.Memory;
import java.util.Map;

public class MultTerm extends CombinatorTerm {
	
    public MultTerm(){}

    @Override   
    public String toStringPrec(int precedence) {
        return "MULT";
    }

    @Override
    public CombinatorTerm execute(CombinatorProgram combProg) {
        return this;
    }
    
    @Override
    public void storeInMem(Memory mem, Map<String,Integer> labels) {
        int addr = mem.allocate(1);
        mem.storeByte(addr,MUL);
    }
    
    @Override
    protected void toASM(ASMBuildStruct asm) {
        asm.text.add("byte " + MUL);
        asm.size++;
    }
    
}

