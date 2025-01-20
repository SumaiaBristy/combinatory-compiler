package Program;

public class Result {
    
    private Integer ival;
    private Boolean bval;
    
    public Result(int n) {
        ival = n;
        bval = null;
    }
    
    public Result(boolean b) {
        ival = null;
        bval = b;
    }

    @Override
    public String toString() {
        String result = "";
        if (ival != null) result = ival.toString();
        if (bval != null) result = bval.toString();
        return result;
    }
    
    
    
}
