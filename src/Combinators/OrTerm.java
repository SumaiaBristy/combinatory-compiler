package Combinators;

import static Webasm.AbstractMachine.OR;
import Webasm.Memory;
import java.util.Map;

public class OrTerm extends CombinatorTerm {
	
    public OrTerm(){}
    
    @Override   
    public String toStringPrec(int precedence) {
        return "OR";
    }

    @Override
    public CombinatorTerm execute(CombinatorProgram combProg) {
        return this;
    }
    
    @Override
    public void storeInMem(Memory mem, Map<String,Integer> labels) {
        int addr = mem.allocate(1);
        mem.storeByte(addr,OR);
    } 
    
    @Override
    protected void toASM(ASMBuildStruct asm) {
        asm.text.add("byte " + OR);
        asm.size++;
    }
    
}

