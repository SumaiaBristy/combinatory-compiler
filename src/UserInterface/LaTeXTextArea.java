package UserInterface;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

public class LaTeXTextArea extends JTextArea implements DocumentListener {

    private static enum Mode { INSERT, COMPLETION };
    private static final String COMMIT_ACTION = "commit";

    private Map<String,String> map = new HashMap();
    private List<String> keywords = new ArrayList();
    private Mode mode = Mode.INSERT;
    
    public LaTeXTextArea() {
        super();
        initialize();
    }
    
    public LaTeXTextArea(Document doc) {
        super(doc);
        initialize();
    }
    
    public LaTeXTextArea(Document doc, String text, int rows, int columns) {
        super(doc,text,rows,columns);
        initialize();
    }
    
    public LaTeXTextArea(int rows, int columns) {
        super(rows,columns);
        initialize();
    }
    
    public LaTeXTextArea(String text, int rows, int columns) {
        super(text,rows,columns);
        initialize();
    }

    private void initialize() {
        this.getDocument().addDocumentListener(this);
        this.getInputMap().put(KeyStroke.getKeyStroke("TAB"),COMMIT_ACTION);
        this.getActionMap().put(COMMIT_ACTION,new CommitAction(this));
    }
    
    public void setLaTeXSymbols(Map<String,String> map) {
        this.map = map;
        this.keywords = new ArrayList(map.keySet());
        Collections.sort(keywords);
    }

    @Override
    public void changedUpdate(DocumentEvent ev) { }

    @Override
    public void removeUpdate(DocumentEvent ev) { }

    @Override
    public void insertUpdate(DocumentEvent ev) {
        if (ev.getLength() == 1) { 
            int pos = ev.getOffset();
            String content = "";
            try {
                content = this.getText(0,pos+1);
            } catch (BadLocationException e) { }
            int startPos = content.lastIndexOf('\\');
            if (startPos!=-1 && pos-startPos >= 1) {
                String prefix = content.substring(startPos).toLowerCase();
                int n = Collections.binarySearch(keywords,prefix);
                if (n < 0 && -n <= keywords.size()) {
                    String match = keywords.get(-n-1);
                    if (match.startsWith(prefix)) {
                        String completion = match.substring(pos-startPos+1);
                        SwingUtilities.invokeLater(new CompletionTask(this,completion,pos+1));
                    }
                } else {
                    if (n >= 0 && prefix.startsWith("\\")) {
                        String match = keywords.get(n);
                        SwingUtilities.invokeLater(new ReplaceTask(this,map.get(match),startPos,match.length()));
                    } else {
                        mode = Mode.INSERT;
                    }
                }
            }
        }
    }
    
    public class CommitAction extends AbstractAction {  
        
        private final JTextArea textArea;
        
        public CommitAction(JTextArea textArea) {
            this.textArea = textArea;
        }
        
        @Override
        public void actionPerformed(ActionEvent ev) {
            if (mode == Mode.COMPLETION) {
                int pos = textArea.getSelectionEnd();
                StringBuilder sb = new StringBuilder(textArea.getText());
                int startPos = sb.substring(0,pos).lastIndexOf('\\');
                String key = sb.substring(startPos,pos);
                String value = map.get(key);
                sb.delete(startPos,pos);
                sb.insert(startPos,value);
                textArea.setText(sb.toString());
                textArea.setCaretPosition(startPos+value.length());
                mode = Mode.INSERT;
            } else {
                textArea.replaceSelection("\t");
            }
        }
    }
    
    private class CompletionTask implements Runnable {
        
        private final JTextArea textArea;
        private final String completion;
        private final int position;

        public CompletionTask(JTextArea textArea, String completion, int position) {
            this.textArea = textArea;
            this.completion = completion;
            this.position = position;
        }

        @Override
        public void run() {
            StringBuilder sb = new StringBuilder(textArea.getText());
            sb.insert(position, completion);
            textArea.setText(sb.toString());
            textArea.setCaretPosition(position + completion.length());
            textArea.moveCaretPosition(position);
            mode = Mode.COMPLETION;
        }
    }

    private class ReplaceTask implements Runnable {
        
        private final JTextArea textArea;
        private final String replaceText;
        private final int position;
        private final int length;

        public ReplaceTask(JTextArea textArea, String replaceText,int position, int length) {
            this.textArea = textArea;
            this.replaceText = replaceText;
            this.position = position;
            this.length = length;
        }

        @Override
        public void run() {
            StringBuilder sb = new StringBuilder(textArea.getText());
            sb.delete(position,position+length);
            sb.insert(position,replaceText);
            textArea.setText(sb.toString());
            textArea.moveCaretPosition(position+replaceText.length());
        }
    }
}
