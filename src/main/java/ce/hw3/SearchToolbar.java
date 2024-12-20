package ce.hw3;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.io.*;

/**
 *
 * @author panpa
 */
public class SearchToolbar {
    JPanel searchPanel;
    JButton searchButton;
    JTextField searchField;
    MakeGui currentGui;
    JPanel resultsPanel;
    static int searchUsed;
    
    public SearchToolbar(MakeGui gui) {
        
        currentGui = gui;
        
        searchPanel = new JPanel();
        searchPanel.setLayout(new BoxLayout(searchPanel,BoxLayout.X_AXIS));
        
        searchField = new JTextField("Search here");
        searchField.setVisible(false);
        
        searchButton = new JButton("Search");
        searchButton.setVisible(false);
        searchButton.addActionListener(searchOp);
        
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
    }
    
    ActionListener searchOp = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            String searched = searchField.getText();
            
            String type = null;
            String formatSearch = " type:";
            
            resultsPanel = new JPanel();
            resultsPanel.setLayout(new BoxLayout(resultsPanel, BoxLayout.Y_AXIS));
            
            if(searched.contains(formatSearch)) {
                int index = searched.lastIndexOf(formatSearch);
                type = searched.substring(index+formatSearch.length());
                searched = searched.substring(0, index);
            }
            
            if(type == null) {
                search(currentGui.setCurrDir.currentDir,searched.toLowerCase(),null);
            }
            else {
                search(currentGui.setCurrDir.currentDir,searched.toLowerCase(),type.toLowerCase());
            }
            
            
            currentGui.panelCurrent.removeAll();
            currentGui.panelCurrent.add(resultsPanel,BorderLayout.CENTER);
            
            JScrollPane scroll = new JScrollPane(resultsPanel);
            scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
            scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            
            currentGui.panelCurrent.add(scroll);
            
            currentGui.panelCurrent.repaint();
            currentGui.panelCurrent.revalidate();
            
            searchUsed = 1;
            JOptionPane.showMessageDialog(null,"Search is finished","Search Result",JOptionPane.INFORMATION_MESSAGE);
        }
    };
    
    
    public void setButtons(File file,Icon imageButton)  {
        JButton dirButton = new JButton(file.getName(),imageButton);
        dirButton.setContentAreaFilled(false);
        dirButton.setBorderPainted(true);
        dirButton.setFocusPainted(false);
        dirButton.setActionCommand(file.getPath());
        dirButton.setToolTipText(file.getPath());
        dirButton.setHorizontalTextPosition(SwingConstants.CENTER);
        dirButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        dirButton.addActionListener(currentGui.setCurrDir.actionOperation);
        resultsPanel.add(dirButton);
    }
    
    public void search(File currentFolder,String searchedText,String type) {
        File [] contentsDir = currentFolder.listFiles();
        
        if((searchedText == null) || (searchedText.equals("Search here".toLowerCase()))) {
            JOptionPane.showMessageDialog(null,"Nothing to search","Search Result",JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        if(contentsDir == null) {
            return;
        }
        
        if(type != null) {
            for (File file : contentsDir) {
                boolean contains = file.getName().toLowerCase().contains(searchedText);
                if (type.equals("dir")) {
                    if (file.isDirectory()) {
                        if (contains) {
                            setButtons(file, currentGui.setCurrDir.iconDisplay(file));
                        }
                        search(file, searchedText, type);
                    }
                } else {
                    boolean flag = isType(file, type);

                    if (contains && (flag)) {
                        setButtons(file, currentGui.setCurrDir.iconDisplay(file));
                    }
                    if (file.isDirectory()) {
                        search(file, searchedText, type);
                    }
                }
            }
        }
        else {
            for (File file : contentsDir) {
                if (file.getName().toLowerCase().contains(searchedText)) {
                    setButtons(file, currentGui.setCurrDir.iconDisplay(file));
                }
                if (file.isDirectory()) {
                    search(file, searchedText, null);
                }
            }
        }
    }
    
    public boolean isType(File file,String type) {
        if(file.isDirectory()) {
            return false;
        }
        
        int index = file.getName().lastIndexOf(".");
        
        if(index == -1) {
            return false;
        }
        
        if(type.contains(("."))) {
            return file.getName().substring(index).equals(type);
        }
        else {
            return file.getName().substring(index + 1).equals(type);
        }
    }
}
