package Webasm;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Memory {
    
    private final List<Byte> mem;
    
    public Memory() {
        mem = new ArrayList();
    }
    
    public byte getByte(int addr) {
        return mem.get(addr);
    }
    
    public void storeByte(int addr, byte value) {
        mem.set(addr,value);
    }
    
    public int getInt(int addr) {
        byte[] value = new byte[4];
        for(int i=0; i<4; i++) {
            value[i] = mem.get(addr+i);
        }
        return ByteBuffer.wrap(value).getInt();
    }
    
    public void storeInt(int addr, int value) {
        byte[] arr = ByteBuffer.allocate(4).putInt(value).array();
        for(int i=0; i<4; i++) {
            mem.set(addr+i,arr[i]);
        }
    }
    
    public int allocate(int numBytes) {
        int address = mem.size();
        mem.addAll((List<Byte>) Collections.nCopies(numBytes,(byte)0));
        return address;
    }
    
    public int size() {
        return mem.size();
    }
    
    @Override
    public String toString() {
        int[] intArr = new int[mem.size()];
        for(int i=0; i<mem.size(); i++) {
            intArr[i] = mem.get(i) & 0xff;
        }
        return Arrays.toString(intArr);
    }   
}
