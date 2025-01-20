package Combinators;

import static Webasm.AbstractMachine.NOT;
import Webasm.Memory;
import java.util.Map;

public class NotTerm extends CombinatorTerm {
	
    public NotTerm(){}
    
    @Override   
    public String toStringPrec(int precedence) {
        return "NOT";
    }

    @Override
    public CombinatorTerm execute(CombinatorProgram combProg) {
        return this;
    }
    
    @Override
    public void storeInMem(Memory mem, Map<String,Integer> labels) {
        int addr = mem.allocate(1);
        mem.storeByte(addr,NOT);
    }
    
    @Override
    protected void toASM(ASMBuildStruct asm) {
        asm.text.add("byte " + NOT);
        asm.size++;
    }
    
}

