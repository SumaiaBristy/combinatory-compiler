package Webasm;

import Combinators.CombinatorProgram;
import Combinators.CombinatorTerm;
import java.util.HashMap;
import java.util.Map;

public class AbstractMachine {
    
    public static final byte BOOL   = (byte) 0;
    public static final byte INT    = (byte) 1;
    
    public static final byte ADD    = (byte) 64;
    public static final byte AND    = (byte) 65;
    public static final byte COND   = (byte) 66;
    public static final byte EQ     = (byte) 67;
    public static final byte LEQ    = (byte) 68;
    public static final byte MUL    = (byte) 69;
    public static final byte NOT    = (byte) 70;
    public static final byte OR     = (byte) 71;
    public static final byte SUB    = (byte) 72;
    
    public static final byte APP    = (byte) 128;
    public static final byte I      = (byte) 129;
    public static final byte K      = (byte) 130;
    public static final byte S      = (byte) 131;
    public static final byte Y      = (byte) 132;
    public static final byte JMP    = (byte) 133;
    
    private int[] backupStack;
    private final int stackCapacity;
    
    private int stackPointer;
    private int backupPointer;
    
    private int startAddress;
    
    private final Memory memory;
    
    private boolean flagRUN;
    	
    public AbstractMachine(int stackCapacity, int heapCapacity) {
        this.stackCapacity = stackCapacity;
        this.memory = new Memory();
    }
    
    public void store(CombinatorTerm t) {
        store(t,new HashMap());
        startAddress = 0;
    }
    
    private void store(CombinatorTerm t, Map<String,Integer> labels) {
        t.storeInMem(memory,labels);     
    }
    
    public void store(CombinatorProgram prog) {
        Map<String,Integer> labels = new HashMap();
        for(String name : prog.getNames()) {
            labels.put(name,memory.allocate(0));
            if (name.equals("main")) startAddress = labels.get("main");
            prog.getTerm(name).storeInMem(memory,labels);
        }
        System.out.println("byte arrary: "+memory.toString());
    }

    public void execute() {
        allocateStack();
        push(startAddress);
        flagRUN = true;
        while (flagRUN) {
            int addr = peek();
            byte opCode = memory.getByte(addr);
            switch (opCode) {
                case INT, BOOL -> {
                    if (stackSize()==1) {
                        flagRUN = false;
                    } else {
                        restore();
                    }
                }                
                case ADD, MUL, SUB -> {             
                    if (opCodeIsConst(memory.getByte(peekSecond()))) {
                        if(opCodeIsConst(memory.getByte(peekThird()))) {
                            remove();
                            int x = memory.getInt(pop()+1);
                            int y = memory.getInt(pop()+1);
                            int newAddress = memory.allocate(5);
                            memory.storeByte(newAddress,INT);
                            switch (opCode) {
                                case ADD -> {
                                    memory.storeInt(newAddress+1,x+y);
                                }
                                case MUL -> {
                                    memory.storeInt(newAddress+1,x*y);
                                }
                                case SUB -> {
                                    memory.storeInt(newAddress+1,x-y);
                                }


                            }
                            push(newAddress);
                        } else {
                            backUp();
                            backUp();
                        }
                    } else {
                        backUp();
                    }
                }
                case AND, OR -> {
                    if (opCodeIsConst(memory.getByte(peekSecond()))) {
                        if(opCodeIsConst(memory.getByte(peekThird()))) {
                            remove();
                            byte x = memory.getByte(pop()+1);
                            byte y = memory.getByte(pop()+1);
                            int newAddress = memory.allocate(2);
                            memory.storeByte(newAddress,BOOL);
                            switch (opCode) {
                                case AND -> {
                                    memory.storeByte(newAddress+1,(byte)(x&y));
                                }
                                case OR -> {
                                    memory.storeByte(newAddress+1,(byte)(x|y));
                                }


                            }
                            push(newAddress);
                        } else {
                            backUp();
                            backUp();
                        }
                    } else {
                        backUp();
                    }
                }                
                case COND -> {             
                    if (opCodeIsConst(memory.getByte(peekSecond()))) {
                        remove();
                        byte cond = memory.getByte(pop()+1);
                        if (cond==0) {
                            remove();
                        } else {
                            int x = pop();
                            remove();
                            push(x);
                        }
                    } else {
                        backUp();
                    }
                }
                case EQ, LEQ -> { 
                    if (opCodeIsConst(memory.getByte(peekSecond()))) {
                        if(opCodeIsConst(memory.getByte(peekThird()))) {
                            remove();
                            int x = memory.getInt(pop()+1);
                            int y = memory.getInt(pop()+1);
                            int newAddress = memory.allocate(2);
                            memory.storeByte(newAddress,BOOL);
                            switch (opCode) {
                                case EQ -> {
                                    memory.storeByte(newAddress+1,(byte)(x==y? 1 : 0));
                                }
                                case OR -> {
                                    memory.storeByte(newAddress+1,(byte)(x<=y? 1 : 0));
                                }


                            }
                            push(newAddress);
                        } else {
                            backUp();
                            backUp();
                        }
                    } else {
                        backUp();
                    }
                }    
                case NOT -> {
                    if (opCodeIsConst(memory.getByte(peekSecond()))) {
                        remove();
                        byte x = memory.getByte(pop()+1);
                        int newAddress = memory.allocate(2);
                        memory.storeByte(newAddress,BOOL);
                        memory.storeByte(newAddress+1,(byte)((x==1?0:1)));
                        push(newAddress);
                    } else {
                        backUp();
                    }
                }
                case APP -> {
                    remove();
                    push(memory.getInt(addr+5));
                    push(memory.getInt(addr+1));
                }
                case I -> {             
                    remove();
                }           
                case K -> {             
                    remove();
                    int x = pop();
                    remove();
                    push(x);
                }
                case S -> {           
                    remove();
                    int x = pop();
                    int y = pop();
                    int z = pop();
                    int newAddress = memory.allocate(9);
                    memory.storeByte(newAddress,APP);
                    memory.storeInt(newAddress+1,y);
                    memory.storeInt(newAddress+5,z);
                    push(newAddress);
                    push(z);
                    push(x);
                }
                case Y -> {
                    remove();
                    int g = pop();
                    int newAddress = memory.allocate(9);
                    memory.storeByte(newAddress,APP);
                    memory.storeInt(newAddress+1,addr);
                    memory.storeInt(newAddress+5,g);
                    push(newAddress);
                    push(g);
                }
                case JMP -> {
                    remove();
                    push(memory.getInt(addr+1));
                }
                default -> throw new UnsupportedOperationException("Unknown operation code in abstract machine.");
            }
        }
    }
    
    public String readResult() {
        String result;
        int addr = pop();
        switch (memory.getByte(addr)) {
            case BOOL -> {
                result = memory.getByte(addr+1)==1? "True" : "False";
            }
            case INT -> {
                result = Integer.toString(memory.getInt(addr+1));
            }
            default -> {
                result = "Unexpected error";
            }
        }
        return result;
    }
    
    private boolean opCodeIsConst(byte b) {
        return (b & 0b11000000) == 0;
    }
    
    private void allocateStack() {
        backupStack = new int[stackCapacity];
        stackPointer = 0;
        backupPointer = stackCapacity-1;
    }
    
    private void push(int value) {
        if (backupPointer < stackPointer) throw new StackOverflowError();
        backupStack[stackPointer] = value;
        stackPointer++;
    }
    
    private int pop() {
        stackPointer--;
        return backupStack[stackPointer];
    }
    
    private void remove() {
        stackPointer--;
    }
    
    private int peek() {
        return backupStack[stackPointer-1];
    }
        
    private int peekSecond() {
        return backupStack[stackPointer-2];
    }
        
    private int peekThird() {
        return backupStack[stackPointer-3];
    }
    
    private void backUp() {
        stackPointer--;
        int x = backupStack[stackPointer];
        backupStack[backupPointer] = x;
        backupPointer--;
    }
    
    private void restore() {
        backupPointer++;
        int x = backupStack[backupPointer];
        backupStack[stackPointer] = x;
        stackPointer++;
    }
    
    private int stackSize() {
        return stackPointer+stackCapacity-(backupPointer+1);
    }
    
}
