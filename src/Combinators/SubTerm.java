package Combinators;

import static Webasm.AbstractMachine.SUB;
import Webasm.Memory;
import java.util.Map;

public class SubTerm extends CombinatorTerm {
	
    public SubTerm(){}
    
    @Override   
    public String toStringPrec(int precedence) {
        return "SUB";
    }

    @Override
    public CombinatorTerm execute(CombinatorProgram combProg) {
        return this;
    }
    
    @Override
    public void storeInMem(Memory mem, Map<String,Integer> labels) {
        int addr = mem.allocate(1);
        mem.storeByte(addr,SUB);
    } 
    
    @Override
    protected void toASM(ASMBuildStruct asm) {
        asm.text.add("byte " + SUB);
        asm.size++;
    }
    
}


