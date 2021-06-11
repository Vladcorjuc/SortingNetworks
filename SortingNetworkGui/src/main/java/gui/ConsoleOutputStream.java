package gui;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class ConsoleOutputStream extends ByteArrayOutputStream
{
    private final String EOL = System.getProperty("line.separator");
    private SimpleAttributeSet attributes;
    private PrintStream printStream;
    private StringBuffer buffer = new StringBuffer(80);
    private boolean isFirstLine;
    private final Document document;
    private final JTextComponent textComponent;

    public ConsoleOutputStream(Color textColor, PrintStream printStream,MessageConsole messageConsole)
    {
        if (textColor != null)
        {
            attributes = new SimpleAttributeSet();
            StyleConstants.setForeground(attributes, textColor);
        }

        this.printStream = printStream;
        isFirstLine = true;
        document = messageConsole.getDocument();
        textComponent = messageConsole.getTextComponent();
    }

    public void flush()
    {
        String message = toString();

        if (message.length() == 0) return;

        handleAppend(message);

        reset();
    }

    private void handleAppend(String message)
    {
        if (document.getLength() == 0)
            buffer.setLength(0);

        if (EOL.equals(message))
        {
            buffer.append(message);
        }
        else
        {
            buffer.append(message);
            clearBuffer();
        }

    }
    private void handleInsert(String message)
    {
        buffer.append(message);

        if (EOL.equals(message))
        {
            clearBuffer();
        }
    }

    private void clearBuffer()
    {
        if (isFirstLine && document.getLength() != 0)
        {
            buffer.insert(0, "\n");
        }

        isFirstLine = false;
        String line = buffer.toString();

        try
        {
            int offset = document.getLength();
            document.insertString(offset, line, attributes);
            textComponent.setCaretPosition( document.getLength() );
        }
        catch (BadLocationException ignored) {}

        if (printStream != null)
        {
            printStream.print(line);
        }

        buffer.setLength(0);
    }
}