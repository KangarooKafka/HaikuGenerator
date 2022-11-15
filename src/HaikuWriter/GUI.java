/*
Date: 11/3/22
Author: Kevin Darke
Contact: kevindarke@gmail.com
This class runs the GUI for the HaikuWriter package. It lets the user choose what inputs they would like to generate
a haiku from, then uses the WordManager to build an Arraylist of Words objects from .txt files stored in the same
directory. It then displays the generated haiku.
*/

package HaikuWriter;

import javax.swing.*;

public class GUI {
    private JButton generateBtn;
    private JButton resetBtn;
    private JTextArea haikuArea;
    private JCheckBox weirdAlCheckBox;
    private JCheckBox charlesDickensCheckBox;
    private JCheckBox taylorSwiftCheckBox;
    private JCheckBox edgarAllenPoeCheckBox;
    private JPanel mainPanel;
    private JLabel wordCountLabel;
    private final HaikuGenerator manager;
    private boolean boxesEnabled = true;

    // Constructor
    GUI(){
        JFrame frame = new JFrame("Haiku Writer");
        frame.setSize(600,400);
        frame.setContentPane(mainPanel);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);

        manager = new HaikuGenerator();// Word manager that contains all the words and writes the Haiku

        // Generate haiku button
        generateBtn.addActionListener(e -> {

            // If there are no words loaded already
            if(manager.wordMap.size() == 0){

                // Check what files to import
                if(weirdAlCheckBox.isSelected()){
                    manager.importFile("WeirdAl.txt");
                }
                if(taylorSwiftCheckBox.isSelected()){
                    manager.importFile("TaylorSwift.txt");
                }
                if(edgarAllenPoeCheckBox.isSelected()){
                    manager.importFile("Poe.txt");
                }
                if(charlesDickensCheckBox.isSelected()){
                    manager.importFile("CharlesDickens.txt");
                }

                // Disable the checkboxes
                toggleBoxes();
                // Display the word count
                wordCountLabel.setText("Vocabulary Size: " + manager.wordMap.size() + " words");
            }

            // If there are still no words loaded, no boxes were checked so display an error
            if(manager.wordMap.size() == 0){
                haikuArea.setText("""
                        Please select one of the boxes
                        I have no material to work with here
                        ¯\\_(ツ)_/¯""");
                // Re-enable the boxes
                toggleBoxes();
            } else {
                // If there are words loaded, generate and display a Haiku
                String haiku = manager.writeHaiku();
                haikuArea.setText(haiku);
            }
        });
        // Reset button
        resetBtn.addActionListener(e -> {
            // If the boxes are not enabled, reset everything
            if(!boxesEnabled) {
                manager.clearWords();
                toggleBoxes();
                weirdAlCheckBox.setSelected(false);
                taylorSwiftCheckBox.setSelected(false);
                edgarAllenPoeCheckBox.setSelected(false);
                charlesDickensCheckBox.setSelected(false);
                haikuArea.setText("");
                wordCountLabel.setText("Vocabulary Size: ");
            }
        });
    }

    // Method to toggle the boxes to the opposite of what they are now
    private void toggleBoxes(){
        weirdAlCheckBox.setEnabled(!boxesEnabled);
        taylorSwiftCheckBox.setEnabled(!boxesEnabled);
        edgarAllenPoeCheckBox.setEnabled(!boxesEnabled);
        charlesDickensCheckBox.setEnabled(!boxesEnabled);
        boxesEnabled = !boxesEnabled;
    }

    // Main method
    public static void main(String[] args){
        new GUI();
    }
}
