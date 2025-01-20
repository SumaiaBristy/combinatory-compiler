package Types;

import org.jparsec.OperatorTable;
import org.jparsec.Parser;
import org.jparsec.Scanners;
import org.jparsec.Terminals;

public class TypeParser {

    private static final String[] SYMBOLS = { "(", ")", "Int", "Bool", "->", "*" };

    private static final Terminals typeOperators = Terminals.operators(SYMBOLS);
	  
    private static TypeParser parser;

    public static TypeParser getTypeParser() {
        if (parser == null) {
            parser = new TypeParser();
        }
        return parser;
    }
	private TypeParser() {
    }

    public Parser<Type> getParser( Terminals operators) {
        Parser.Reference<Type> ref = org.jparsec.Parser.newReference();
        Parser<Type> term = 
                ref.lazy().between(operators.token("("), operators.token(")"))
                .or(operators.token("Int").retn(new IntType()))
                .or(operators.token("Bool").retn(new BoolType()))
                .or(Terminals.Identifier.PARSER.map(TypeVariable::new));
        Parser<Type> parser = new OperatorTable<Type>()
            .infixr(operators.token("->").retn((l,r) -> new FunctionType(l,r)), 3)
            .infixr(operators.token("*").retn((l,r) -> new ProductType(l,r)), 5)
            .build(term);
        ref.set(parser);
        return parser;
    }

    public Type parse(CharSequence source) {
        return getParser(typeOperators)
                .from(typeOperators.tokenizer().cast()
                .or(Terminals.Identifier.TOKENIZER).or(Terminals.IntegerLiteral.TOKENIZER)
                ,Scanners.WHITESPACES.skipMany())
                .parse(source);
    }
}


