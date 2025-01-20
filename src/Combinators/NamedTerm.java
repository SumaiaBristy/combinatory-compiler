package Combinators;

import static Webasm.AbstractMachine.JMP;
import Webasm.Memory;
import java.util.Map;

public class NamedTerm extends CombinatorTerm {
    
    private final String name;
    
    public NamedTerm(String name){
        this.name = name;
    }
    
    @Override
    public void storeInMem(Memory mem, Map<String,Integer> labels) {
        int addr = mem.allocate(5);
        mem.storeByte(addr,JMP);
        mem.storeInt(addr+1,labels.get(name));
    }

    @Override
    protected void toASM(ASMBuildStruct asm) {
        asm.text.add("byte " + (JMP & 0xff));
        asm.text.add("dword _" + name);
        asm.size += 5;
    }

    @Override
    protected String toStringPrec(int precedence) {
        return name;
    }

    @Override
    public CombinatorTerm execute(CombinatorProgram combProg) {
        return combProg.getTerm(name).execute(combProg);
    }

}
