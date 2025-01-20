package Program;

import Exceptions.TypingException;
import Types.BoolType;
import Types.IntType;
import Types.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import Combinators.CombinatorProgram;
import Combinators.CombinatorTerm;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class Program {
    
    private static final File ASMDIRECTORY = new File(System.getProperty("user.dir") + "\\asm"); //points to the "asm" directory, located within the current working directory of the user (user.dir).
    private static final String[] ASMCOMMAND = { ASMDIRECTORY + "\\JWASM.EXE", "-coff", "prog.asm" }; //an array of strings representing a command to run the assembler (JWASM.EXE) from the "asm" directory, with the -coff option to generate object code from prog.asm.
    private static final String[] LINKCOMMAND = { ASMDIRECTORY +  "\\link.exe","prog.obj", "/OUT:" + System.getProperty("user.dir") + "\\exe\\prog.exe", "msvcrt.lib" }; //  an array of strings representing a command to link the generated object file (prog.obj) using link.exe, outputting an executable (prog.exe) into an "exe" directory, and linking it with the msvcrt.lib library
    
    private final Map<String,Declaration> declarations;
    
    public Program() {
        this.declarations = new HashMap();
    }
    
    public void addDeclaration(String name, Declaration decl) {
        declarations.put(name,decl);
    }
    
    public Declaration getDeclaration(String name) {
        return declarations.get(name);
    }
    
    public Set<String> nameSet() {
        return declarations.keySet();
    }
    
    public void type() throws TypingException {
        for(Declaration decl : declarations.values()) {
            decl.type(this);
        }
        if (declarations.containsKey("main")) {
            Type t = declarations.get("main").getTyp();
            if (!(t instanceof IntType) && !(t instanceof BoolType)) {
                throw new TypingException("main is not of type Int or Bool.");
            }
        }
    }
    
    public Result execute() {
        if (!declarations.containsKey("main")) throw new RuntimeException("No main program.");
        Result result = null;
        //System.out.println(""+);
        PCFTerm compres = declarations.get("main").getBody().execute(this);
        if (compres instanceof True) result = new Result(true);
        if (compres instanceof False) result = new Result(false);
        if (compres instanceof IntLiteral n) result = new Result(n.getNum());
        return result;
    }
    
    @Override
    public String toString() {
        String result = "";
        for(String name : declarations.keySet()) {
            result += name + " :: " + declarations.get(name).getTyp();
            result += " = " + declarations.get(name).getBody() + "\n";
        }
        return result;
    }
    
    public CombinatorProgram translate() {
        if (!declarations.containsKey("main")) throw new RuntimeException("No main program.");
        Map<String,CombinatorTerm> terms = new HashMap<>();
        for (Declaration dec: declarations.values()) {
        	CombinatorTerm combTerm = dec.getBody().translate();
        	terms.put(dec.getName(),combTerm);
        }
        return new CombinatorProgram(terms);
    }
    
    public void compile(int stackCapacity, int heapCapacity) {
        CombinatorProgram cp = this.translate();
        String asmProg = cp.toASM();
        System.out.println(asmProg);
        try {
            StringBuilder template = new StringBuilder();
            BufferedReader templateFile = new BufferedReader(new FileReader(ASMDIRECTORY + "\\Template2.asm"));
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
    
}
