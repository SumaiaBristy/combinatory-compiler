package Combinators;

import static Webasm.AbstractMachine.EQ;
import Webasm.Memory;
import java.util.Map;

public class EqualTerm extends CombinatorTerm {
	    
    public EqualTerm(){}
    
    @Override   
    public String toStringPrec(int precedence) {
        return "EQUAL";
    }

    @Override
    public CombinatorTerm execute(CombinatorProgram combProg) {
        return this;
    }
    
    @Override
    public void storeInMem(Memory mem, Map<String,Integer> labels) {
        int addr = mem.allocate(1);
        mem.storeByte(addr,EQ);
    } 
    
    @Override
    protected void toASM(ASMBuildStruct asm) {
        asm.text.add("byte " + EQ);
        asm.size++;
    }
    
}

