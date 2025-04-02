package ui.custom.input;

import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import model.Space;
import service.EventEnum;
import service.EventListener;

import java.awt.*;

import static java.awt.Font.PLAIN;

public class NumberText extends JTextField implements EventListener {

    private final Space space;

    public NumberText(final Space space) {
        this.space = space;
        var dimension = new Dimension(50,50);
        this.setSize(dimension);
        this.setPreferredSize(dimension);
        this.setVisible(true);
        this.setFont(new Font("Arial", PLAIN, 20));
        this.setHorizontalAlignment(CENTER);
        this.setDocument(new NumberTextLimit());
        this.setEnabled(!space.isFixo());

        if(space.isFixo()) {
            this.setText(space.getAtual().toString());
        }

        this.getDocument().addDocumentListener(new DocumentListener() {

            private void changeSpace() {
                if(getText().isEmpty()) {
                    space.limparSpace();
                    return;
                }
                space.setAtual(Integer.parseInt(getText()));
            }


            @Override
            public void insertUpdate(DocumentEvent e) {
                changeSpace();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                changeSpace();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                changeSpace();

            }
        });


    }

    @Override
    public void update(EventEnum eventEnum) {
        if(eventEnum.equals(EventEnum.CLEAR_SPACE) && this.isEnabled()){
            this.setText("");
        }
    }
}
