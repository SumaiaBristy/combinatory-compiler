package Combinators;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jparsec.Parser;
import org.jparsec.Scanners;
import org.jparsec.Terminals;


public class CombinatorParser {

    private static final String[] SYMBOLSC = { "(", ")", "S", "K", "I", "Y", "ADD","MULT","SUB", "TRUE", "FALSE",
    		"AND", "OR", "NOT", "IF", "EQUAL", "LEQUAL"};

    private static final Terminals operatorsC = Terminals.operators(SYMBOLSC);

    private static CombinatorParser parser;

    public static CombinatorParser getCombParser() {
        if (parser == null) {
            parser = new CombinatorParser();
        }
        return parser;
    }
    private CombinatorParser() {
    }

    public Parser<CombinatorTerm> getParser(Set decls, Terminals operators) {
        Parser.Reference<CombinatorTerm> ref = org.jparsec.Parser.newReference();
        Parser<CombinatorTerm> term = 
                ref.lazy().between(operatorsC.token("("), operatorsC.token(")"))
                .or(Terminals.Identifier.PARSER.map(s -> { if (decls.contains(s)) 
                return new Combinators.NamedTerm(s); else return new VARIABLE(s); }))
                .or(operatorsC.token("TRUE").retn(new TrueTerm()))
                .or(operatorsC.token("FALSE").retn(new FalseTerm()))
                .or(operatorsC.token("S").retn(new STerm()))
                .or(operatorsC.token("K").retn(new KTerm()))
                .or(operatorsC.token("I").retn(new ITerm()))
                .or(operatorsC.token("Y").retn(new YTerm()))
                .or(operatorsC.token("ADD").retn(new AddTerm()))
                .or(operatorsC.token("MULT").retn(new MultTerm()))
                .or(operatorsC.token("SUB").retn(new SubTerm()))
                .or(operatorsC.token("AND").retn(new AndTerm()))
                .or(operatorsC.token("OR").retn(new OrTerm()))
                .or(operatorsC.token("NOT").retn(new NotTerm()))
                .or(operatorsC.token("EQUAL").retn(new EqualTerm()))
                .or(operatorsC.token("LEQUAL").retn(new LEqualTerm()))
                .or(operatorsC.token("IF").retn(new ConditionalTerm()))
                .or(Terminals.IntegerLiteral.PARSER.map(s -> new 
                INTTerm(Integer.valueOf(s))));
                 Parser<CombinatorTerm> combTerm = 
                term.many1().map(l -> makeApplications(l));
			    ref.set(combTerm);
			    return combTerm;
    }

//    public CombinatorTerm parse(CharSequence source) {
//        return getParser()
//                .from(operatorsC.tokenizer().cast()
//                .or(Terminals.Identifier.TOKENIZER)
//				.or(Terminals.IntegerLiteral.TOKENIZER),
//                Scanners.WHITESPACES.skipMany())
//                .parse(source);
//    }
    
    public CombinatorTerm parse(Set<String> decls, CharSequence source) {
        return getParser(decls,operatorsC)
        		.from(operatorsC.tokenizer().cast().or(
                        Terminals.Identifier.TOKENIZER)
                        .or(Terminals.IntegerLiteral.TOKENIZER),
                         Scanners.WHITESPACES.skipMany())
                        .parse(source);
    }
    
    public CombinatorTerm parse(CharSequence source) {
        return parse(new HashSet(),source);
    }
    
    private CombinatorTerm makeApplications(List<CombinatorTerm> l) {
        CombinatorTerm result = l.get(0);
        for(int i=1; i<l.size(); i++) {
            result = new Application (result,l.get(i));
        }
        return result;
    }
}


