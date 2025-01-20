package UserInterface;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.OceanTheme;

import org.jparsec.error.ParserException;

import Combinators.CombinatorProgram;
import Combinators.CombinatorTerm;
import Program.PCFTerm;
import Program.PCFTermParser;
import Program.Program;
import Program.ProgramParser;
import Webasm.AbstractMachine;
import Exceptions.TypingException;
import Exceptions.UnificationException;

import java.util.HashMap;
import java.util.Map;
import javax.swing.UnsupportedLookAndFeelException;

public class TextEditor extends JFrame {
	
    private PrintStream out;
    private final LaTeXTextArea inArea;
    private final JTextArea outArea;

    public TextEditor(){
        try {
            out = new PrintStream(System.out,true,"UTF-8");
        } catch (UnsupportedEncodingException ex) {
            out = System.out;
        }
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
            MetalLookAndFeel.setCurrentTheme(new OceanTheme());
        } catch(ClassNotFoundException | IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException e ) {} 
        
        this.setLayout(new BorderLayout());

        JLabel inputLabel = new JLabel("Input Area");
        JLabel outputLabel = new JLabel("Output Area");

        inArea = new LaTeXTextArea(15,10);
        Map<String,String> map = new HashMap();
        map.put("\\lambda","\u03BB");
        inArea.setLaTeXSymbols(map);

        outArea = new JTextArea(10,10);

        JButton check = new JButton("Check");
        JButton compile = new JButton("Compile");
        JButton run = new JButton("Run");

        //panel for the button area
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.add(check);
        buttonPanel.add(compile);
        buttonPanel.add(run);

        // Panel for the input area and its label
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());
        inputPanel.add(inputLabel, BorderLayout.NORTH);
        inputPanel.add(new JScrollPane(inArea), BorderLayout.CENTER);

        // Panel for the output area and its label
        JPanel outputPanel = new JPanel();
        outputPanel.setLayout(new BorderLayout());
        outputPanel.add(outputLabel, BorderLayout.NORTH);
        outputPanel.add(new JScrollPane(outArea), BorderLayout.CENTER);

        // Add components to the frame
        this.add(inputPanel, BorderLayout.NORTH); //top
        this.add(outputPanel, BorderLayout.CENTER); // center
        this.add(buttonPanel, BorderLayout.SOUTH); //  bottom

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(500, 600);
        this.setVisible(true);

        check.addActionListener(e -> check());
        compile.addActionListener(e -> compile());
        run.addActionListener(e -> run());

    }

    private void check() {
    	
//    	fact :: Int -> Int = recf.λn.if n=0 then 1 else n * f(n-1);
//    	succ :: Int-> Int = λn.n+1;
//    	main :: Int = fact(succ 4);
    	outArea.setText("");
        String text = inArea.getText();
        try {
            Program prog = ProgramParser.getProgramParser().parse(text);
            prog.type();
            outArea.setText("Program \'"+prog+"\' is correct!");
        } catch (ParserException | TypingException e) {
            e.printStackTrace();
            outArea.setText(""+e.toString());
        }
	      
        }

    private void run() {
    	outArea.setText("");
    	String text = inArea.getText();
        Program prog = ProgramParser.getProgramParser().parse(text);
        CombinatorProgram combProg = prog.translate();
        System.out.println(combProg);
        AbstractMachine machine = new AbstractMachine(1000,4000);
        machine.store(combProg);
        machine.execute();
        outArea.setText("The result for the execution of the program \'"+prog+"\' is: " + machine.readResult());

    }

    private void compile() {
    	outArea.setText("");
		String text = inArea.getText(); //(recf.λn.if n=0 then 1 else n * f(n-1))3
	    Program prog = ProgramParser.getProgramParser().parse(text);
	    prog.compile(1000,4000);
	    outArea.setText("\'"+prog+"\' is compiled successfully!");
    }

}
