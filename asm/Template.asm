;--- Win32 Template for combinator compiler.
;--- assemble: jwasm -coff Test2.asm
;--- link:     link Test2.obj msvcrt.lib

    .386
    .MODEL flat, c
    option casemap:none
    
BOOL    equ 0
INTC    equ 1
    
ADDI    equ 64
ANDI    equ 65
COND    equ 66
EQI     equ 67
LEQ     equ 68
MULI    equ 69
NOTI    equ 70
ORI     equ 71
SUBI    equ 72
  
APP     equ 128
I       equ 129
K       equ 130
S       equ 131
Y       equ 132

printf  proto c :ptr byte, :vararg
malloc  proto c :dword
exit    proto c :dword

    .CONST

; messages

intMessage              db "%d",13,10,0
trueMessage             db "True",13,10,0
falseMessage            db "False",13,10,0
errorMessage            db "Unexpected error.",13,10,0
stackOverFlowMessage    db "Stack overflow.",0
outOfHeapMessage        db "Out of heap.",0

; important constant

stackCapacity   dword #stackCapacity#                              ; must be dividable by 4
heapCapacity    dword #heapCapacity#

#program#   

    .DATA
    
stackBaseAddr   dword ?    
stackPointer    dword ?
backupPointer   dword ?
heapBaseAddr    dword ?
heapPointer     dword ?
flagRUN         byte 1

    .CODE
    
_allocateStack proc c
    push eax
    mov eax, stackCapacity 
    invoke malloc, eax
    mov stackBaseAddr, eax
    mov stackPointer, eax
    add eax, stackCapacity                ; eax set to highest dword address
    sub eax, 4                            ; in stack memory
    mov backupPointer, eax
    pop eax
    ret
_allocateStack endp

_allocateHeap proc c
    push eax
    mov eax, heapCapacity
    invoke malloc, eax
    mov heapBaseAddr, eax
    mov heapPointer, eax
    pop eax
    ret
_allocateHeap endp

_newOnHeap proc c
    push ebx
    mov ebx, heapPointer
    add ebx, eax
    sub ebx, heapBaseAddr
    .if (heapCapacity < ebx)
        invoke printf, addr outOfHeapMessage
        mov eax, 1
        invoke exit, eax
    .endif
    mov ebx, heapPointer
    add heapPointer, eax
    mov eax, ebx
    pop ebx
    ret
_newOnHeap endp
    
_push proc c
    push ebx
    mov ebx, stackPointer
    .if (backupPointer < ebx)
        invoke printf, addr stackOverFlowMessage
        mov eax, 1
        invoke exit, eax
    .endif
    mov [ebx], eax
    add stackPointer, 4
    pop ebx
    ret
_push endp

_pop proc c
    push ebx
    sub stackPointer, 4
    mov ebx, stackPointer
    mov eax, [ebx]
    pop ebx
    ret
_pop endp

_remove proc c
    sub stackPointer, 4
    ret
_remove endp

_peek proc c
    push ebx
    mov ebx, stackPointer
    sub ebx, 4
    mov eax, [ebx]
    pop ebx
    ret
_peek endp

_peekSecond proc c
    push ebx
    mov ebx, stackPointer
    sub ebx, 8
    mov eax, [ebx]
    pop ebx
    ret
_peekSecond endp

_peekThird proc c
    push ebx
    mov ebx, stackPointer
    sub ebx, 12
    mov eax, [ebx]
    pop ebx
    ret
_peekThird endp

_backup proc c
    push eax
    push ebx
    sub stackPointer, 4
    mov ebx, stackPointer
    mov eax, [ebx]
    mov ebx, backupPointer
    mov [ebx], eax
    sub backupPointer, 4
    pop ebx
    pop eax
    ret
_backup endp

_restore proc c
    push eax
    push ebx
    add backupPointer, 4
    mov ebx, backupPointer
    mov eax, [ebx]
    mov ebx, stackPointer
    mov [ebx], eax
    add stackPointer, 4
    pop ebx
    pop eax
    ret
_restore endp

_stackSize proc c
    mov eax, stackPointer
    add eax, stackCapacity
    sub eax, backupPointer
    shr eax, 2                  ; divide by 4
    dec eax
    ret
_stackSize endp

main proc c 
    call _allocateStack
    call _allocateHeap 
    mov eax, offset prog
    call _push
    .while(flagRUN != 0)
        call _peek
        mov ebx, eax
        mov al, [ebx]
        .if (al == BOOL  || al == INTC)
            call _stackSize
            .if (eax == 1)
                mov flagRUN, 0
            .else
                call _restore
            .endif
        .else
        .if (al == ADDI || al == MULI || al == SUBI)
            mov dl, al
            call _peekSecond
            mov al, [eax]
            and al, 11000000b
            .if (al == 0)
                call _peekThird
                mov al, [eax]
                and al, 11000000b
                .if (al == 0)
                    call _remove
                    call _pop
                    mov ebx, [eax+1]
                    call _pop
                    mov ecx, [eax+1]
                    mov eax, ebx
                    mov ebx, ecx
                    .if (dl == ADDI)
                        add eax, ebx
                    .else
                    .if (dl == MULI)
                        mul ebx
                    .else
                        sub eax, ebx
                    .endif
                    .endif
                    mov ebx, eax
                    mov eax, 5
                    call _newOnHeap
                    mov [eax], dword ptr INTC
                    mov [eax+1], ebx
                    call _push
                 .else 
                    call _backup
                    call _backup
                .endif
            .else
                call _backup
            .endif
        .else
        .if (al == ANDI || al == ORI)
            mov dl, al
            call _peekSecond
            mov al, [eax]
            and al, 11000000b
            .if (al == 0)
                call _peekThird
                mov al, [eax]
                and al, 11000000b
                .if (al == 0)
                    call _remove
                    call _pop
                    mov bl, [eax+1]
                    call _pop
                    mov cl, [eax+1]
                    mov al, bl
                    mov bl, cl
                    .if (dl == ANDI)
                        and al, bl
                    .else
                        or al, bl
                    .endif
                    mov bl, al
                    mov eax, 2
                    call _newOnHeap
                    mov [eax], dword ptr BOOL
                    mov [eax+1], bl
                    call _push
                 .else 
                    call _backup
                    call _backup
                .endif
            .else
                call _backup
            .endif
        .else
        .if (al == COND)
            mov dl, al
            call _peekSecond
            mov al, [eax]
            and al, 11000000b
            .if (al == 0)          
                call _remove
                call _pop
                mov bl, [eax+1]
                .if (bl == 0)
                    call _remove
                .else
                    call _pop
                    call _remove
                    call _push
                .endif            
            .else
                call _backup
            .endif
        .else
        .if (al == EQI || al == LEQ)
            mov dl, al
            call _peekSecond
            mov al, [eax]
            and al, 11000000b
            .if (al == 0)
                call _peekThird
                mov al, [eax]
                and al, 11000000b
                .if (al == 0)
                    call _remove
                    call _pop
                    mov ebx, [eax+1]
                    call _pop
                    mov ecx, [eax+1]
                    mov eax, ebx
                    mov ebx, ecx
                    .if (dl == EQI)
                        .if (eax == ebx)
                            mov al, 1
                        .else 
                            xor al, al
                        .endif
                    .else
                        .if (eax <= ebx)
                            mov al, 1
                        .else 
                            xor al, al
                        .endif
                    .endif
                    mov bl, al
                    mov eax, 2
                    call _newOnHeap
                    mov [eax], dword ptr BOOL
                    mov [eax+1], bl
                    call _push
                 .else 
                    call _backup
                    call _backup
                .endif
            .else
                call _backup
            .endif
        .else
        .if (al == NOTI)
            mov dl, al
            call _peekSecond
            mov al, [eax]
            and al, 11000000b
            .if (al == 0)          
                call _remove
                call _pop
                mov bl, [eax+1]
                .if (bl == 0)
                    mov bl, 1
                .else
                    mov bl, 0
                .endif  
                mov eax, 2
                call _newOnHeap
                mov [eax], dword ptr BOOL
                mov [eax+1], bl
                call _push
            .else
                call _backup
            .endif
        .else
        .if (al == APP)
            call _remove
            mov eax, [ebx+5]
            call _push
            mov eax, [ebx+1]
            call _push
        .else 
        .if (al == I)
            call _remove
        .else
        .if (al == K)
            call _remove
            call _pop
            call _remove
            call _push
        .else
        .if (al == S)
            call _remove
            call _pop
            mov ebx, eax            ; ebx = x
            call _pop
            mov ecx, eax            ; ecx = y
            call _pop
            mov edx, eax            ; edx = z
            mov eax, 9
            call _newOnHeap
            mov [eax], dword ptr APP
            mov [eax+1], ecx
            mov [eax+5], edx
            call _push
            mov eax, edx
            call _push
            mov eax, ebx
            call _push   
        .else    
        .if (al == Y)
            call _remove
            call _pop
            mov ecx, eax            ; ebx = addr ; ecx = g            
            mov eax, 9
            call _newOnHeap
            mov [eax], dword ptr APP
            mov [eax+1], ebx
            mov [eax+5], ecx
            call _push
            mov eax, ecx
            call _push
        .else
            invoke printf, addr errorMessage
            xor eax, 1
            invoke exit, eax
        .endif
        .endif 
        .endif 
        .endif
        .endif
        .endif
        .endif
        .endif
        .endif
        .endif
        .endif
    .endw
    call _pop
    mov ebx, eax
    mov al, [ebx]
    .if (al == 0)
        mov al, [ebx+1]
        .if (al == BOOL)
            invoke printf, addr falseMessage
        .else
            invoke printf, addr trueMessage
        .endif
    .else
    .if (al == 1)
        mov eax, [ebx+1]
        invoke printf, addr intMessage, eax
    .else
        invoke printf, addr errorMessage
    .endif 
    .endif   
    xor eax,eax                                ; eax = 0
    invoke exit, eax
main endp

    END main
