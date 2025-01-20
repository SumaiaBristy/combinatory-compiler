package Combinators;

import static Webasm.AbstractMachine.COND;
import Webasm.Memory;
import java.util.Map;


public class ConditionalTerm extends CombinatorTerm {
        
    public ConditionalTerm(){}
    
    @Override   
    public String toStringPrec(int precedence) {
        return "IF";
    }

    @Override
    public CombinatorTerm execute(CombinatorProgram combProg) {
        return this;
    }
    
    @Override
    public void storeInMem(Memory mem, Map<String,Integer> labels) {
        int addr = mem.allocate(1);
        mem.storeByte(addr,COND);
    } 
    
    @Override
    protected void toASM(ASMBuildStruct asm) {
        asm.text.add("byte " + COND);
        asm.size++;
    }
    
}

