package Combinators;

import static Webasm.AbstractMachine.AND;
import Webasm.Memory;
import java.util.Map;

public class AndTerm extends CombinatorTerm {
	
    public AndTerm(){}
    
    @Override   
    public String toStringPrec(int precedence) {
        return "AND";
    }

    @Override
    public CombinatorTerm execute(CombinatorProgram combProg) {
        return this;
    }
    
    @Override
    public void storeInMem(Memory mem, Map<String,Integer> labels) {
        int addr = mem.allocate(1);
        mem.storeByte(addr,AND);
    } 
    
    @Override
    protected void toASM(ASMBuildStruct asm) {
        asm.text.add("byte " + AND);
        asm.size++;
    }
    
}

