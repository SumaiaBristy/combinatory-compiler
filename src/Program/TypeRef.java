package Program;
import Types.Type;
import Types.TypeVariable;

public class TypeRef {
	TypeVariable typeVariable;
	Type type;
	public TypeRef() {
	}
	public TypeRef(TypeVariable typeVariable) {
		this.typeVariable = typeVariable;
	}
	public Type getType() {
		return type;
	}
}
