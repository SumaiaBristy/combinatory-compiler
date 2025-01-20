package Combinators;

import Webasm.Memory;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public abstract class CombinatorTerm {
    
    protected class ASMBuildStruct {
        
        public List<String> text;
        public int size;
        public String label;
        
        public ASMBuildStruct(String label) {
            text = new LinkedList();
            this.label = label;
        }
        	
    }
    
    public abstract CombinatorTerm execute(CombinatorProgram combProg);
    
    public abstract void storeInMem(Memory mem, Map<String,Integer> labels);
    
    protected abstract void toASM(ASMBuildStruct asm);
        
    public String toASM(String name) {
        ASMBuildStruct asm = new ASMBuildStruct(name);
        toASM(asm);
        StringBuilder result = new StringBuilder(name+"\t");
        int i=0;
        for(String s : asm.text) {
            if (i != 0) result.append("\t\t");
            result.append(s);
            if (i < asm.text.size()-1) result.append("\n");
            i++;
        }
        return result.toString();
    }
    
    protected abstract String toStringPrec(int precedence);
    
    @Override
    public String toString() {
        return toStringPrec(0);
    }
    
}
