package Combinators;

import static Webasm.AbstractMachine.ADD;
import Webasm.Memory;
import java.util.Map;

public class AddTerm extends CombinatorTerm {
    
    public AddTerm(){ }
    
    @Override   
    public String toStringPrec(int precedence) {
        return "ADD";
    }

    @Override
    public CombinatorTerm execute(CombinatorProgram combProg) {
        return this;
    }
    
    @Override
    public void storeInMem(Memory mem, Map<String,Integer> labels) {
        int addr = mem.allocate(1);
        mem.storeByte(addr,ADD);
    }
    
    @Override
    protected void toASM(ASMBuildStruct asm) {
        asm.text.add("byte " + ADD);
        asm.size++;
    }

}

