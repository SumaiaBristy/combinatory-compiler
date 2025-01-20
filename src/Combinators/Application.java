package Combinators;

import static Webasm.AbstractMachine.APP;
import Webasm.Memory;
import java.util.Map;

public class Application extends CombinatorTerm {

    private final CombinatorTerm left;
    private final CombinatorTerm right;
    private final int precedence = 1;

    public Application(CombinatorTerm left, CombinatorTerm right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public String toStringPrec(int precedence) {
        String result = left.toStringPrec(this.precedence) + " " + right.toStringPrec(2);
        if (precedence > this.precedence) {
                result = "(" + result + ")";
        }
        return result;
    }

    @Override
    public CombinatorTerm execute(CombinatorProgram combProg) {       
        CombinatorTerm nLeft = left.execute(combProg);
        CombinatorTerm nRight = right;
        if(nLeft instanceof Application appl1) {
            if (appl1.left instanceof Application appl2) {
                if (appl2.left instanceof STerm) {
                    return new Application(new Application(appl2.right,nRight),new Application(appl1.right,nRight)).execute(combProg);
                }
                if (appl2.left instanceof ConditionalTerm) { //COnditional-If-Else
                    CombinatorTerm rr = appl2.right.execute(combProg);
                    if(rr instanceof TrueTerm) {
                        return appl1.right.execute(combProg);
                    } else {
                        if(rr instanceof FalseTerm) {
                            return nRight.execute(combProg);
                        }
                        else return new Application(nLeft, nRight);
                    }
                }
            }
            if (appl1.left instanceof KTerm) {
                return appl1.right.execute(combProg);
            }
            if (appl1.left instanceof AddTerm) { //ADD
                CombinatorTerm ll = appl1.right.execute(combProg);
                CombinatorTerm rr = nRight.execute(combProg);
                if((ll instanceof INTTerm lnum)&& (rr instanceof INTTerm rnum)) {
                    return new INTTerm(lnum.getNum() + rnum.getNum());
                }
                else return new Application(new Application(new AddTerm(), ll), rr);
            }
            if (appl1.left instanceof SubTerm) { //SUB
                CombinatorTerm ll = appl1.right.execute(combProg);
                CombinatorTerm rr = nRight.execute(combProg);
                if((ll instanceof INTTerm lnum)&& (rr instanceof INTTerm rnum)) {
                    return new INTTerm(lnum.getNum() - rnum.getNum());
                }
                else return new Application(new Application(new SubTerm(), ll), rr);
            }
            if (appl1.left instanceof MultTerm) { //MUL
                CombinatorTerm ll = appl1.right.execute(combProg);
                CombinatorTerm rr = nRight.execute(combProg);
                if((ll instanceof INTTerm lnum)&& (rr instanceof INTTerm rnum)) {
                    return new INTTerm(lnum.getNum() * rnum.getNum());
                }
                else return new Application(new Application(new MultTerm(), ll), rr);
            } 
            if (appl1.left instanceof AndTerm) { //AND
                CombinatorTerm ll = appl1.right.execute(combProg);
                CombinatorTerm rr = nRight.execute(combProg);
                if((ll instanceof TrueTerm)&& (rr instanceof TrueTerm)) {
                    return new TrueTerm();
                } else if(((ll instanceof TrueTerm)&& (rr instanceof FalseTerm)) ||
                                       ((ll instanceof FalseTerm)&& (rr instanceof TrueTerm))) 
                    return new FalseTerm();
                    else return new Application(new Application(new AndTerm(), ll), rr);
                }
            if (appl1.left instanceof OrTerm) { //OR
                    CombinatorTerm ll = appl1.right.execute(combProg);
                    CombinatorTerm rr = nRight.execute(combProg);
                    if((ll instanceof FalseTerm)&& (rr instanceof FalseTerm)) {
                        return new FalseTerm();
                    } else if(((ll instanceof TrueTerm)&& (rr instanceof FalseTerm)) ||
                                           ((ll instanceof FalseTerm)&& (rr instanceof TrueTerm))) 
                        return new TrueTerm();
                        else return new Application(new Application(new OrTerm(), ll), rr);
                    }
            if (appl1.left instanceof EqualTerm) { //EQUAL
                    CombinatorTerm ll = appl1.right.execute(combProg);
                    CombinatorTerm rr = nRight.execute(combProg);
                    if((ll instanceof INTTerm lnum)&& (rr instanceof INTTerm rnum)) {
                        if(lnum.getNum() == rnum.getNum())
                            return new TrueTerm();
                            else return new FalseTerm();
                    }
                        else return new Application(new Application(new EqualTerm(), ll), rr);
                    }
            if (appl1.left instanceof LEqualTerm) { //LEQUAL
                    CombinatorTerm ll = appl1.right.execute(combProg);
                    CombinatorTerm rr = nRight.execute(combProg);
                    if((ll instanceof INTTerm lnum)&& (rr instanceof INTTerm rnum)) {
                        if(lnum.getNum() <= rnum.getNum())
                            return new TrueTerm();
                        else return new FalseTerm();
                    }
                    else return new Application(new Application(new LEqualTerm(), ll), rr);
            }
        }
        if (nLeft instanceof ITerm) {
            return nRight.execute(combProg);
        }
        if(nLeft instanceof YTerm) {
            return new Application(nRight, new Application(nLeft, nRight)).execute(combProg);
        }
        if (nLeft instanceof NotTerm) { //NOT
            CombinatorTerm rr = nRight.execute(combProg);
            if((rr instanceof FalseTerm)) {
                   return new TrueTerm();
            }
            else if(rr instanceof TrueTerm) 
                return new FalseTerm();
                else return new Application(new NotTerm(),rr);
        }
//        if(nLeft instanceof NamedTerm) {
//        	return nLeft.execute(combProg);
//        }
        return new Application(nLeft,nRight);
    }
    
    @Override
    public void storeInMem(Memory mem, Map<String,Integer> labels) {
        int addr = mem.allocate(9);
        mem.storeByte(addr,APP);
        mem.storeInt(addr+1,addr+9);
	left.storeInMem(mem,labels);
        mem.storeInt(addr+5,mem.allocate(0));
	right.storeInMem(mem,labels);
    }
    
    @Override
    protected void toASM(ASMBuildStruct asm) {
        asm.text.add("byte " + (int)(APP & 0xff));
        asm.text.add("dword " + asm.label + "+" + (asm.size + 9));
        int pos = asm.text.size();
        asm.size += 9;
        left.toASM(asm);
        asm.text.add(pos,"dword " + asm.label + "+" + asm.size);
        right.toASM(asm);
    }
    
}
