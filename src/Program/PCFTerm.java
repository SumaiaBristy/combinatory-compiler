package Program;
import Exceptions.TypingException;
import IntermediateTerm.IntermediateTerm;
import Types.Type;
import Types.TypeRef;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import Combinators.CombinatorTerm;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;

public abstract class PCFTerm {
    
    private static final File ASMDIRECTORY = new File(System.getProperty("user.dir") + "\\asm"); //points to the "asm" directory, located within the current working directory of the user (user.dir).
    private static final String[] ASMCOMMAND = { ASMDIRECTORY + "\\JWASM.EXE", "-coff", "prog.asm" }; //an array of strings representing a command to run the assembler (JWASM.EXE) from the "asm" directory, with the -coff option to generate object code from prog.asm.
    private static final String[] LINKCOMMAND = { ASMDIRECTORY +  "\\link.exe","prog.obj", "/OUT:" + System.getProperty("user.dir") + "\\exe\\prog.exe", "msvcrt.lib" }; //  an array of strings representing a command to link the generated object file (prog.obj) using link.exe, outputting an executable (prog.exe) into an "exe" directory, and linking it with the msvcrt.lib library
    
    public abstract Map<String,Type> getTypedVars();
    
    protected abstract void type(VariableGenerator gen, Map<String,TypeRef> env, Program prog) throws TypingException;
    
    public void type(Program prog) throws TypingException {
        type(new VariableGenerator(),new HashMap(),prog);
    }
    
    public abstract Type getType();
    
    public abstract String toStringPrec(int precedence);
    
    public abstract PCFTerm substitute(String x, PCFTerm prog);
    public abstract PCFTerm execute(Program prog);
    public abstract Set<String> freeVars();
     
    public abstract IntermediateTerm toIntermediate();
    
    public CombinatorTerm translate() {
        return this.toIntermediate().translate().toCombinatorTerm();
    }
    
    public void compile(int stackCapacity, int heapCapacity) {
        CombinatorTerm ct = this.translate();
        String asmProg = ct.toASM("prog"); // combinator term is converted to an equaivalent asm program
        try {
            StringBuilder template = new StringBuilder();
            BufferedReader templateFile = new BufferedReader(new FileReader(ASMDIRECTORY + "\\Template1.asm"));
            String line = templateFile.readLine();
            while (line != null) {
                template.append(line);
                template.append("\n");
                line = templateFile.readLine();
            }
            templateFile.close();
            int index = template.indexOf("#stackCapacity#");
            template.replace(index,index+15,String.valueOf(stackCapacity));
            index = template.indexOf("#heapCapacity#");
            template.replace(index,index+14,String.valueOf(heapCapacity));
            index = template.indexOf("#program#");
            template.replace(index,index+9,asmProg);
            PrintWriter asmFile = new PrintWriter(ASMDIRECTORY + "\\prog.asm"); //A PrintWriter object (asmFile) is created to write the modified template to a new file called prog.asm in the ASMDIRECTORY.
            asmFile.print(template.toString()); // The print() method is used to output the updated content to the file.
            asmFile.close();
            Runtime runtime = Runtime.getRuntime();
            runtime.exec(ASMCOMMAND,null,ASMDIRECTORY);  
            Process p = runtime.exec(LINKCOMMAND,null,ASMDIRECTORY);
            p.waitFor();
            new File(ASMDIRECTORY + "\\prog.asm").delete();
            new File(ASMDIRECTORY + "\\prog.obj").delete();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    @Override
    public String toString() {
        return toStringPrec(0);
    }	    
}
