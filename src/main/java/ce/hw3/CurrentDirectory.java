package ce.hw3;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import javax.swing.filechooser.FileSystemView;
/**
 *
 * @author panpa
 */
public class CurrentDirectory {
    MakeGui mainFrame;
    JFrame frame;
    JPanel currentDirectoryPanel;
    File currentDir;
    JPopupMenu popUp;
    static JButton lastButtonClicked = new JButton();
    static int statePaste = 0;
    
    public CurrentDirectory(File homeDir,MakeGui currentFrame) {
        mainFrame = currentFrame;
        
        currentDirectoryPanel = new JPanel();
        currentDirectoryPanel.setLayout(new WrapLayout(FlowLayout.LEFT));
        currentDirectoryPanel.addMouseListener(mouseOperation);
        
        currentDir = homeDir;
        
        setContents(sortContentsDir());
    }
    
    public File [] sortContentsDir() {
        int counterFiles = 0;
        
        File [] contents = currentDir.listFiles();
        
        if(contents == null) {
            return(null);
        }

        for (File content : contents) {
            if (content == null) {
                continue;
            }
            if ((content.isHidden()) || (!content.exists())) {
                continue;
            }
            counterFiles++;
        }
        
        File [] sortedContents = new File[counterFiles];
        
        int counter = 0;

        for (File content : contents) {
            if (content == null) {
                continue;
            }
            if ((content.isHidden()) || (!content.exists())) {
                continue;
            }
            if (content.isDirectory()) {
                sortedContents[counter++] = content;
            }
        }

        for (File content : contents) {
            if (content == null) {
                continue;
            }
            if ((content.isHidden()) || (!content.exists())) {
                continue;
            }
            if (content.isFile()) {
                sortedContents[counter++] = content;
            }
        }
        return(sortedContents);
    }
    
    public void setButtonsDir(File file,Icon imageButton) {
        JButton dirButton = new JButton(file.getName(),imageButton);
        dirButton.setContentAreaFilled(false);
        dirButton.setBorderPainted(false);
        dirButton.setFocusPainted(false);
        dirButton.setActionCommand(file.getPath());
        dirButton.setHorizontalTextPosition(SwingConstants.CENTER);
        dirButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        dirButton.addMouseListener(mouseOperation);
        currentDirectoryPanel.add(dirButton);
    }
    
    public void setContents(File [] contentsDir) {
        Icon imageFile;
        
        if(mainFrame.setSearchToolbar.searchUsed == 1) {
            mainFrame.panelCurrent.removeAll();
        }
        currentDirectoryPanel.removeAll();
        
        if(contentsDir == null) {
            return;
        }

        for (File file : contentsDir) {
            imageFile = iconDisplay(file);
            setButtonsDir(file, imageFile);
        }
        
        if(mainFrame.setSearchToolbar.searchUsed == 1) {
            JScrollPane scroll = new JScrollPane(currentDirectoryPanel);
            scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
            scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            mainFrame.panelCurrent.add(scroll);
            mainFrame.panelCurrent.repaint();
            mainFrame.panelCurrent.revalidate();
        }
        
        currentDirectoryPanel.repaint();
        currentDirectoryPanel.revalidate();
    }
    
    public Icon iconDisplay(File contentsFile) {
        Icon imageIcon;
        
        if(contentsFile.isDirectory()) {
                imageIcon = new ImageIcon("./icons/folder.png");
                return(imageIcon);
        }
        else {
            String extension = contentsFile.getPath().substring(contentsFile.getPath().lastIndexOf(".") + 1);

            File checkFile = new File("./icons/" + extension + ".png");

            if(checkFile.exists()) {
                imageIcon = new ImageIcon("./icons/" + extension + ".png"); 
            }
            else {
                imageIcon = new ImageIcon("./icons/question.png");
            }
        }
        return(imageIcon);
    }
    
    MouseListener mouseOperation = new MouseListener() {
        @Override
        public void mouseClicked(MouseEvent e) {
            if(currentDirectoryPanel.equals(e.getSource())) {
                lastButtonClicked.setContentAreaFilled(false);
                mainFrame.setMenu.bar.getMenu(1).setEnabled(false);
            }
            else {
                JButton chosenButton = (JButton)e.getSource();
                if(lastButtonClicked != chosenButton) {
                    lastButtonClicked.setContentAreaFilled(false);
                }
                chosenButton.setBackground(Color.LIGHT_GRAY);
                chosenButton.setContentAreaFilled(true);
                lastButtonClicked = chosenButton;
                mainFrame.setMenu.bar.getMenu(1).setEnabled(true);
                if(e.getButton() == MouseEvent.BUTTON1) {
                    if(e.getClickCount() == 2) {
                        chosenButton.setContentAreaFilled(false);
                        if(!chosenButton.isContentAreaFilled()) {
                            mainFrame.setMenu.bar.getMenu(1).setEnabled(false);
                        }
                        chosenButton.addActionListener(actionOperation);
                        chosenButton.doClick();
                        mainFrame.setBreadCrumb.currDirFile = currentDir;
                        mainFrame.setBreadCrumb.breadCrumbPanel.removeAll();
                        mainFrame.setBreadCrumb.buttonsBreadCrumb(mainFrame.setBreadCrumb.currDirFile.getPath(), System.getProperty("file.separator"));
                        mainFrame.setBreadCrumb.breadCrumbPanel.repaint();
                        mainFrame.setBreadCrumb.breadCrumbPanel.revalidate();
                        chosenButton.removeActionListener(actionOperation);
                    }
                }
                if(e.getButton() == MouseEvent.BUTTON3) {
                    popUp = new JPopupMenu();
                    
                    JMenuItem cut = new JMenuItem("Cut");
                    cut.addActionListener(mainFrame.setMenu);
                    popUp.add(cut);

                    JMenuItem copy = new JMenuItem("Copy");
                    copy.addActionListener(mainFrame.setMenu);
                    popUp.add(copy);

                    JMenuItem paste = new JMenuItem("Paste");
                    paste.addActionListener(mainFrame.setMenu);
                    paste.setEnabled(statePaste == 1);
                    popUp.add(paste);

                    JMenuItem rename = new JMenuItem("Rename");
                    rename.addActionListener(mainFrame.setMenu);
                    popUp.add(rename);

                    JMenuItem delete = new JMenuItem("Delete");
                    delete.addActionListener(mainFrame.setMenu);
                    popUp.add(delete);

                    JMenuItem addToFavourites = new JMenuItem("Add to Favourites");
                    addToFavourites.addActionListener(mainFrame.setMenu);
                    popUp.add(addToFavourites);

                    JMenuItem properties = new JMenuItem("Properties");
                    properties.addActionListener(mainFrame.setMenu);
                    popUp.add(properties);

                    popUp.show(e.getComponent(),e.getX(),e.getY());
                }
            }
        }
        @Override
      public void mouseEntered(MouseEvent e) {
      }
        @Override
      public void mouseExited(MouseEvent e) {
      }
        @Override
      public void mousePressed(MouseEvent e) {
      }
        @Override
      public void mouseReleased(MouseEvent e) {
      }
    };
    
    ActionListener actionOperation = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            int checkOp = 0;
            
            File newFile = currentDir;
            
            File[] files  = File.listRoots();
            for (File f : files) {
                if(f.getPath().contains(e.getActionCommand())) {
                    String systemFile = FileSystemView.getFileSystemView().getSystemDisplayName (f);
                    if(systemFile.contains(e.getActionCommand())) {
                        FileSystem createFileSystemRoot = new FileSystem(f);
                        newFile = createFileSystemRoot.fileSystemRoot;
                        checkOp = 1;
                    }
                }
            }
            
            if(checkOp == 0) {
               newFile = new File(e.getActionCommand());
            }
            if(newFile.isDirectory()) {
                currentDir = newFile;
                setContents(sortContentsDir());
                
                if(mainFrame.setSearchToolbar.searchUsed == 1 || mainFrame.setFavourites.favouritesClicked == 1) {
                    mainFrame.setBreadCrumb.currDirFile = currentDir;
                    mainFrame.setBreadCrumb.breadCrumbPanel.removeAll();
                    mainFrame.setBreadCrumb.buttonsBreadCrumb(mainFrame.setBreadCrumb.currDirFile.getPath(), System.getProperty("file.separator"));
                    mainFrame.setBreadCrumb.breadCrumbPanel.repaint();
                    mainFrame.setBreadCrumb.breadCrumbPanel.revalidate();
                }
                
            }
            else if(newFile.isFile()) {
                try {
                    Desktop.getDesktop().open(newFile);
                }
                catch(IOException ex) {
                    JOptionPane.showMessageDialog(null,"IOException occured while opening a file.","ERROR FOUND",JOptionPane.ERROR_MESSAGE);
                    System.exit(-1);
                }
            }
        }
    };
}
