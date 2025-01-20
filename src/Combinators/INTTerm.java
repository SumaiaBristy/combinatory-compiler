package Combinators;

import static Webasm.AbstractMachine.INT;
import Webasm.Memory;
import java.util.Map;

public class INTTerm extends CombinatorTerm {

    private final int num;

    public INTTerm (int i){
        this.num = i;
    }

    @Override
    public String toStringPrec(int precedence) {
        return String.valueOf(num);
    }

    @Override
    public CombinatorTerm execute(CombinatorProgram combProg) {
            return this;
    }

    public int getNum() {
            return num;
    }
    
    @Override
    public void storeInMem(Memory mem, Map<String,Integer> labels) {
        int addr = mem.allocate(5);
        mem.storeByte(addr,INT);
        mem.storeInt(addr+1,num);
    }
    
    @Override
    protected void toASM(ASMBuildStruct asm) {
        asm.text.add("byte " + INT);
        asm.text.add("dword " + num);
        asm.size += 5;
    }
    
}
