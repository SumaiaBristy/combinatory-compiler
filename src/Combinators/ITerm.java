package Combinators;

import static Webasm.AbstractMachine.I;
import Webasm.Memory;
import java.util.Map;

public class ITerm extends CombinatorTerm {
    
    public ITerm (){ }

    @Override
    public String toStringPrec(int precedence) {
        return "I";
    }

    @Override
    public CombinatorTerm execute(CombinatorProgram combProg) {
        return this;
    } 
    
    @Override
    public void storeInMem(Memory mem, Map<String,Integer> labels) {
        int addr = mem.allocate(1);
        mem.storeByte(addr,I);
    } 
    
    @Override
    protected void toASM(ASMBuildStruct asm) {
        asm.text.add("byte " + (int)(I & 0xff));
        asm.size++;
    } 
    
}

