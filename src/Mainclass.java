import Exceptions.TypingException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

import Combinators.CombinatorParser;
import Combinators.CombinatorProgram;
import UserInterface.TextEditor;
import Program.PCFTerm;
import Program.PCFTermParser;
import Program.Program;
import Program.ProgramParser;
import Program.VariableGenerator;
import Webasm.AbstractMachine;

public class Mainclass {
    
    public static void main (String[] args) {
        PrintStream out;
        try {
            out = new PrintStream(System.out,true,"UTF-8");
        } catch (UnsupportedEncodingException ex) {
            out = System.out;
        }
	
//        VariableGenerator gen = new VariableGenerator();
//        ProgramParser parser = ProgramParser.getProgramParser();
//        Program p = parser.parse("fact :: Int -> Int = recf.λn.if n=0 then 1 else n * f(n-1);\n"
//        		+ " succ :: Int-> Int = λn.n+1; \n main :: Int = fact(succ 4);");
//        
//        out.println(p);           
//        try {
//            p.type();
//        } catch(TypingException e) {
//            e.printStackTrace();
//        }
//        
        // Program executed using abstract machine.
//        CombinatorProgram prog = p.translate();
//        System.out.println(prog);
//        AbstractMachine machine = new AbstractMachine(1000,4000);
//        machine.store(p.translate());
//        machine.execute();
//        out.println("Result: " + machine.readResult());
//        
//        // Program compiled.
        //p.compile(1000,4000);
        
      TextEditor editor = new TextEditor();
       
    }
}
































