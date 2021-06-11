package gui;

import java.io.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;

public class MessageConsole
{
    private JTextComponent textComponent;
    private Document document;

    public MessageConsole(JTextComponent textComponent)
    {
        this(textComponent, true);
    }

    public MessageConsole(JTextComponent textComponent, boolean isAppend)
    {
        this.textComponent = textComponent;
        this.document = textComponent.getDocument();
        textComponent.setEditable( false );
    }

    public JTextComponent getTextComponent() {
        return textComponent;
    }
    public Document getDocument() {
        return document;
    }

    public void redirectOut()
    {
        redirectOut(null, null);
    }

    public void redirectOut(Color textColor, PrintStream printStream){
        ConsoleOutputStream cos = new ConsoleOutputStream(textColor, printStream,this);
        System.setOut( new PrintStream(cos, true) );
    }
    public void redirectErr() {
        redirectErr(null, null);
    }

    public void redirectErr(Color textColor, PrintStream printStream) {
        ConsoleOutputStream cos = new ConsoleOutputStream(textColor, printStream,this);
        System.setErr( new PrintStream(cos, true) );
    }


}