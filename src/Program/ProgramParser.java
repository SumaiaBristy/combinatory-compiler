package Program;

import org.jparsec.Parser;
import org.jparsec.Parsers;
import org.jparsec.Scanners;
import org.jparsec.Terminals;
import Types.TypeParser;
import java.util.function.BiFunction;

public class ProgramParser {
    
    private static final String[] SYMBOLS = {"(", ")","True","False","and","or","=","<=","not","+","-","*",".", "\u03BB", "rec", "if", "then", "else", 
			"::", ";","Int", "Bool", "->" };
    private static final Terminals OPERATORS = Terminals.operators(SYMBOLS);
    private static ProgramParser PARSER;
    
    public static ProgramParser getProgramParser() {
        if (PARSER == null) {
            PARSER = new ProgramParser();
        }
        return PARSER;
    }
    
    private ProgramParser() {}

    public Parser<Program> getParser() {
        BiFunction<BiFunction,Program,Parser<Program>> frec = (f,p) -> 
            Parsers.sequence(Terminals.Identifier.PARSER, 
            OPERATORS.token("::"), 
            TypeParser.getTypeParser().getParser(OPERATORS),
            OPERATORS.token("="), 
            PCFTermParser.getPCFTermParser().getParser(p.nameSet(),OPERATORS),
            OPERATORS.token(";"),
            (n,s,t,s1,body,s2) -> {
                Declaration decl = new Declaration(n,t,body);
                p.addDeclaration(decl.getName(),decl); 
                return p; 
            }).next(q -> (Parser<Program>) f.apply(f,q))
            .or(Parsers.constant(p));
        return frec.apply(frec,new Program());
    }
    
    public Program parse(CharSequence source) {
        return getParser()
            .from(OPERATORS.tokenizer().cast()
                .or(Terminals.Identifier.TOKENIZER)
                .or(Terminals.IntegerLiteral.TOKENIZER),
             Scanners.WHITESPACES.skipMany())
            .parse(source);
    }
    
}


