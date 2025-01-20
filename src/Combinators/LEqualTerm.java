package Combinators;

import static Webasm.AbstractMachine.LEQ;
import Webasm.Memory;
import java.util.Map;

public class LEqualTerm extends CombinatorTerm {
	    
    public LEqualTerm(){}
    
    @Override   
    public String toStringPrec(int precedence) {
        return "LEQUAL";
    }

    @Override
    public CombinatorTerm execute(CombinatorProgram combProg) {
        return this;
    }
    
    @Override
    public void storeInMem(Memory mem, Map<String,Integer> labels) {
        int addr = mem.allocate(1);
        mem.storeByte(addr,LEQ);
    } 
    
    @Override
    protected void toASM(ASMBuildStruct asm) {
        asm.text.add("byte " + LEQ);
        asm.size++;
    }
    
}

