package Types;

import java.util.Map;

public class TypeRef {
    
    private Type type;
    
    public TypeRef(Type type) {
        this.type = type;
    }
    
    public Type getType() {
        return type;
    }
    
    public void substitute(Map<String,Type> subst) {
        type = type.substitute(subst);
    }
    
}
