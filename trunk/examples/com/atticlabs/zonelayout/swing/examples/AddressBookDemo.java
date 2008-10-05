package com.atticlabs.zonelayout.swing.examples;

import com.atticlabs.zonelayout.swing.ZoneLayout;
import com.atticlabs.zonelayout.swing.ZoneLayoutFactory;
import com.atticlabs.zonelayout.swing.ZoneLayoutDebugPanel;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.JList;
import javax.swing.BorderFactory;
import java.awt.BorderLayout;
import java.awt.Dimension;

public class AddressBookDemo {

    public JPanel buildPanel() {
        JTextField firstNameTxt = new JTextField(10);
        JTextField lastNameTxt = new JTextField(10);
        JTextField phoneTxt = new JTextField(10);
        JTextField emailTxt = new JTextField(10);
        JTextField addr1Txt = new JTextField(10);
        JTextField addr2Txt = new JTextField(10);
        JTextField cityTxt = new JTextField(10);
        JTextField stateTxt = new JTextField(10);
        JTextField postalTxt = new JTextField(10);
        JTextField countryTxt = new JTextField();
        JList nameList = new JList(new Object[] {
                "Bunny, Bugs",
                "Cat, Sylvester",
                "Coyote, Wile E.",
                "Devil, Tasmanian",
                "Duck, Daffy",
                "Fudd, Elmer",
                "Le Pew, Pepe",
                "Martian, Marvin"
        });

        ZoneLayout btnBarLayout = ZoneLayoutFactory.newZoneLayout();
        btnBarLayout.addRow("a2b2c2d2e");
        JPanel buttonPanel = new JPanel(btnBarLayout);
        buttonPanel.add(new JButton("New"), "a");
        buttonPanel.add(new JButton("Delete"), "b");
        buttonPanel.add(new JButton("Edit"), "c");
        buttonPanel.add(new JButton("Save"), "d");
        buttonPanel.add(new JButton("Cancel"), "e");
//        ZoneLayout emailLayout = ZoneLayoutFactory.newZoneLayout();
//        emailLayout.addRow("a2b~-b");
//        JPanel emailPanel = new JPanel(emailLayout);
//        emailPanel.add(new JLabel("Email"), "a");
//        emailPanel.add(emailTxt, "b");

        ZoneLayout layout = ZoneLayoutFactory.newZoneLayout();
        layout.addRow("a..3...................");
        layout.addRow("....b>b2c-~c2d<..d2e-~e", "doubleRow");
        layout.addRow("....6..................", "doubleRow");
        layout.addRow("....i>i2j-~j2k<k2l..-~l", "emailRow");
        layout.addRow("....6..................", "emailRow");
        layout.addRow("+*..f>f2g-~...........g", "singleRow");
        layout.addRow("....6..................", "singleRow");
        layout.addRow("....h.................h", "buttonBar");
        layout.addRow("..a.!..................");


        JPanel basePanel = new JPanel((layout));
        basePanel.add(nameList, "a");
        layout.insertTemplate("doubleRow");
        basePanel.add(new JLabel("Last Name"), "b");
        basePanel.add(lastNameTxt, "c");
        basePanel.add(new JLabel("First Name"), "d");
        basePanel.add(firstNameTxt, "e");
        layout.insertTemplate("emailRow");
        basePanel.add(new JLabel("Phone"), "i");
        basePanel.add(phoneTxt, "j");
        basePanel.add(new JLabel("Email"), "k");
        basePanel.add(emailTxt, "l");
        layout.insertTemplate("singleRow");
        basePanel.add(new JLabel("Address 1"), "f");
        basePanel.add(addr1Txt, "g");
        layout.insertTemplate("singleRow");
        basePanel.add(new JLabel("Address 2"), "f");
        basePanel.add(addr2Txt, "g");
        layout.insertTemplate("doubleRow");
        basePanel.add(new JLabel("City"), "b");
        basePanel.add(cityTxt, "c");
        layout.insertTemplate("doubleRow");
        basePanel.add(new JLabel("State"), "b");
        basePanel.add(stateTxt, "c");
        basePanel.add(new JLabel("Postal Code"), "d");
        basePanel.add(postalTxt, "e");
        layout.insertTemplate("doubleRow");
        basePanel.add(new JLabel("Country"), "b");
        basePanel.add(countryTxt, "c");
        layout.insertTemplate("buttonBar");
        basePanel.add(buttonPanel, "h");

        basePanel.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
        return basePanel;
    }

    public static void main(String[] args) {
        final JFrame frame = new JFrame("Address Book Demo");
        frame.getContentPane().setLayout(new BorderLayout());



        frame.getContentPane().add(new AddressBookDemo().buildPanel(), BorderLayout.CENTER);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                frame.pack();
                frame.setVisible(true);
            }
        });
    }
}
