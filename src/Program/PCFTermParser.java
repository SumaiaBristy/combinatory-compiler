package Program;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jparsec.OperatorTable;
import org.jparsec.Parser;
import org.jparsec.Parsers;
import org.jparsec.Scanners;
import org.jparsec.Terminals;

public class PCFTermParser {

    private static final String[] SYMBOLS = { "(", ")","True","False","and","or","=","<=","not","+","-","*",".", "\u03BB", "rec", "if", "then", "else"};

    private static final Terminals progOperators = Terminals.operators(SYMBOLS);
	 
    private static PCFTermParser PARSER;

    public static PCFTermParser getPCFTermParser() {
        if (PARSER == null) {
            PARSER = new PCFTermParser();
        }
        return PARSER;
    }
	private PCFTermParser() {
    }

    public Parser<PCFTerm> getParser(Set decls, Terminals operators) {
        Parser.Reference<PCFTerm> ref = org.jparsec.Parser.newReference();
        Parser<PCFTerm> term = 
                ref.lazy().between(operators.token("("), operators.token(")"))
                .or(Terminals.Identifier.PARSER.map(s -> { if (decls.contains(s)) 
                	return new NamedTerm(s); else return new Variable(s); }))
                .or(operators.token("True").retn(new True()))
                .or(operators.token("False").retn(new False()))
                .or(Parsers.sequence(operators.token("if"),ref.lazy(),
                operators.token("then"),ref.lazy(),operators.token("else"),
                ref.lazy(),(t1,p1,t2,p2,t3,p3) -> new Conditional(p1,p2,p3)))
                .or(Terminals.IntegerLiteral.PARSER.map(s -> new IntLiteral(Integer.valueOf(s))))
                .or(Parsers.sequence(operators.token("\u03BB"),Terminals.Identifier.PARSER,
                operators.token("."),ref.lazy(),(s1,s2,s3,t) -> new Abstraction(s2,t)))
                .or(Parsers.sequence(operators.token("rec"),
                Terminals.Identifier.PARSER,operators.token("."),ref.lazy(),
                (s1,s2,s3,t) -> new Recursion(s2,t)));
        Parser<PCFTerm> typeTerm = term.many1().map(l -> makeApplications(l));
        Parser<PCFTerm> parser = new OperatorTable<PCFTerm>()
            .infixr(operators.token("or").retn((l,r) -> new Or(l,r)),Or.precedence)
            .infixr(operators.token("and").retn((l,r) -> new And(l,r)),And.precedence)
            .prefix(operators.token("not").retn((l) -> new Not(l)), Not.precedence )
            .infixr(operators.token("=").retn((l,r) -> new Equal(l,r)), Equal.precedence)
            .infixr(operators.token("<=").retn((l,r) -> new LEqual(l,r)), LEqual.precedence)
            .infixr(operators.token("+").retn((l,r) -> new Addition(l,r)), Addition.precedence)
            .infixn(operators.token("-").retn((l,r) -> new Subtraction(l,r)), Subtraction.precedence)
            .infixr(operators.token("*").retn((l,r) -> new Mult(l,r)), Mult.precedence)
            .build(typeTerm);
        ref.set(parser);
        return parser;
    }

    public PCFTerm parse(Set<String> decls, CharSequence source) {
        return getParser(decls,progOperators)
        		.from(progOperators.tokenizer().cast().or(
                        Terminals.Identifier.TOKENIZER)
                        .or(Terminals.IntegerLiteral.TOKENIZER),
                         Scanners.WHITESPACES.skipMany())
                        .parse(source);
    }
    
    public PCFTerm parse(CharSequence source) {
        return parse(new HashSet(),source);
    }
    
    private PCFTerm makeApplications(List<PCFTerm> l) {
        PCFTerm result = l.get(0);
        for(int i=1; i<l.size(); i++) {
            result = new Application (result,l.get(i));
        }
        return result;
    }
}


