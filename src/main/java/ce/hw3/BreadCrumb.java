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
import java.nio.file.FileSystems;
import javax.swing.filechooser.FileSystemView;

/**
 *
 * @author panpa
 */
public class BreadCrumb {
    MakeGui mainFrame;
    JPanel breadCrumbPanel;
    File currDirFile;

    public BreadCrumb(MakeGui currentFrame) {
        mainFrame = currentFrame;
        
        breadCrumbPanel = new JPanel();
        breadCrumbPanel.setLayout(new BoxLayout(breadCrumbPanel, BoxLayout.X_AXIS));
        breadCrumbPanel.setBackground(new Color(255, 222, 130));

        String homeDir = System.getProperty("user.home");
        String separator = FileSystems.getDefault().getSeparator();
                
        currDirFile = new File(homeDir);
        
        buttonsBreadCrumb(currDirFile.getAbsolutePath(), separator);
        
    }
    
    public void buttonsBreadCrumb(String path,String separator) {
        String regex;
        regex = (separator.compareTo("\\") == 0) ? "\\\\" : separator;
        
        String [] arrayStr = path.split(regex);
        
        String  dirpath = arrayStr[0];
        
        File strAsFile = new File(path);
        
        for(int i = 0; i < arrayStr.length; i++) {
            File currentFile = currDirFile;
            if((strAsFile.isFile()) && (i == arrayStr.length - 1)) {
                breadCrumbPanel.remove(breadCrumbPanel.getComponentCount() - 1);
                continue;
            }
            
            if(( i > 0) && (i != arrayStr.length - 1)) {
                for(int j = 0; j < arrayStr.length - 1 - i; j++) {
                    currentFile = currentFile.getParentFile();
                    dirpath = currentFile.getPath();
                }
            }
            if(i == arrayStr.length - 1) {
                dirpath = currDirFile.getPath();
            }
            
            JButton link = new JButton(arrayStr[i]);
            link.setContentAreaFilled(false);
            link.setBorderPainted(false);
            link.setFocusPainted(false);
            link.setActionCommand(dirpath);
            link.addActionListener(buttonOperation);
            breadCrumbPanel.add(link);
            if(i != arrayStr.length - 1) {
                JLabel label = new JLabel(">");
                breadCrumbPanel.add(label);
            }
        }
    }
    
    ActionListener buttonOperation = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            int checkOp = 0;
            
            File[] files  = File.listRoots();
            for (File f : files) {
                if(f.getPath().contains(e.getActionCommand())) {
                    String systemFile = FileSystemView.getFileSystemView().getSystemDisplayName (f);
                    if(systemFile.contains(e.getActionCommand())) {
                        FileSystem createFileSystemRoot = new FileSystem(f);
                        currDirFile = createFileSystemRoot.fileSystemRoot;
                        checkOp = 1;
                    }
                }
            }
            
            if(checkOp == 0) {
                currDirFile = new File(e.getActionCommand());
            }
            
            breadCrumbPanel.removeAll();
            buttonsBreadCrumb(currDirFile.getAbsolutePath(), FileSystems.getDefault().getSeparator());
            breadCrumbPanel.repaint();
            breadCrumbPanel.revalidate();
            mainFrame.setCurrDir.currentDir = currDirFile;
            mainFrame.setCurrDir.setContents(mainFrame.setCurrDir.sortContentsDir());
        }
    };
}
