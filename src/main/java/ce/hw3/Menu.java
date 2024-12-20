package ce.hw3;

import java.awt.*;
import java.io.*;
import java.awt.event.*;
import java.nio.file.Files;
import javax.swing.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import javax.xml.bind.JAXBException;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author panpa
 */
public class Menu implements ActionListener {
    JMenuBar bar;
    MakeGui currentFrame;
    static File source;
    static File dest;
    static int fileOp;
    
    
    public Menu(MakeGui frame) {
        currentFrame = frame;
        
        JMenu file = new JMenu("File");
        
        JMenuItem newWindow = new JMenuItem("New Window");
        newWindow.addActionListener(this);
        file.add(newWindow);
        
        JMenuItem exit = new JMenuItem("Exit");
        exit.addActionListener(this);
        file.add(exit);
        
        JMenu edit = new JMenu("Edit");
        edit.setEnabled(false);
        
        JMenuItem cut = new JMenuItem("Cut");
        cut.addActionListener(this);
        edit.add(cut);
        
        JMenuItem copy = new JMenuItem("Copy");
        copy.addActionListener(this);
        edit.add(copy);
        
        JMenuItem paste = new JMenuItem("Paste");
        paste.addActionListener(this);
        paste.setEnabled(false);
        edit.add(paste);
        
        JMenuItem rename = new JMenuItem("Rename");
        rename.addActionListener(this);
        edit.add(rename);
        
        JMenuItem delete = new JMenuItem("Delete");
        delete.addActionListener(this);
        edit.add(delete);
        
        JMenuItem addToFavourites = new JMenuItem("Add to Favourites");
        addToFavourites.addActionListener(this);
        edit.add(addToFavourites);
        
        JMenuItem properties = new JMenuItem("Properties");
        properties.addActionListener(this);
        edit.add(properties);
        
        JButton search = new JButton("Search");
        search.setContentAreaFilled(false);
        search.setBorderPainted(false);
        search.setFocusPainted(false);
        search.addActionListener(this);
        
        bar = new JMenuBar();
        bar.setBackground(new Color(226,210,247));
        bar.add(file);
        bar.add(edit);
        bar.add(search);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        
        if(e.getActionCommand().equals("Search")) {
            currentFrame.setSearchToolbar.searchButton.setVisible(!currentFrame.setSearchToolbar.searchButton.isVisible());

            currentFrame.setSearchToolbar.searchField.setVisible(!currentFrame.setSearchToolbar.searchField.isVisible());
        }
        
        if(e.getActionCommand().equals("New Window")) {
            new MakeGui();
        }
        if(e.getActionCommand().equals("Exit")) {
            currentFrame.frame.dispose();
        }
        if(e.getActionCommand().equals("Cut") || e.getActionCommand().equals("Copy")) {
            
            fileOp = e.getActionCommand().equals("Cut") ? 1 : -1;
            
            
            currentFrame.setCurrDir.statePaste = 1;
            
            currentFrame.setMenu.bar.getMenu(1).getMenuComponent(2).setEnabled(true);
            source = new File(currentFrame.setCurrDir.lastButtonClicked.getActionCommand());
        }
        if(e.getActionCommand().equals("Paste")) {
            currentFrame.setCurrDir.statePaste = 0;
            
            dest = new File(currentFrame.setCurrDir.lastButtonClicked.getActionCommand());
            
            try {
                paste(source,dest,fileOp);
            }
            catch(IOException ex) {
                JOptionPane.showMessageDialog(null,"I/O Exception occured.Close any file related to this folder and try again!","ERROR FOUND",JOptionPane.ERROR_MESSAGE);
            }
            fileOp = 0;
        }
        if(e.getActionCommand().equals("Rename")) {
            source = new File(currentFrame.setCurrDir.lastButtonClicked.getActionCommand());
            String newName = (String) JOptionPane.showInputDialog(null,"New name:","Name Selection",JOptionPane.INFORMATION_MESSAGE,null,null,source.getName());
            if(newName != null) {
                try {
                    Files.move(source.toPath(),source.toPath().resolveSibling(newName));
                    currentFrame.setCurrDir.setContents(currentFrame.setCurrDir.sortContentsDir());
                }
                catch(IOException ex) {
                    if(source.toPath().resolveSibling(newName).toFile().exists()) {
                        JOptionPane.showMessageDialog(null,"I/O Exception occured.File name exists try another name!","ERROR FOUND",JOptionPane.ERROR_MESSAGE);
                    }
                    else {
                        JOptionPane.showMessageDialog(null,"I/O Exception occured.Close any file related to this folder and try again!","ERROR FOUND",JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }
        if(e.getActionCommand().equals("Delete")) {
            int optionResult;
            fileOp = 0;
            File source = new File(currentFrame.setCurrDir.lastButtonClicked.getActionCommand());
            
            if(source.isDirectory()) {
                optionResult = JOptionPane.showConfirmDialog(null,"Are you sure you want to delete this folder?",
                                        "Validation Request",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
            }
            else {
                optionResult = JOptionPane.showConfirmDialog(null,"Are you sure you want to delete this file?",
                                        "Validation Request",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
            }
            
            if(optionResult == JOptionPane.YES_OPTION) {
                try {
                    Files.walkFileTree(source.toPath(), fileVisitor);
                    
                    Component [] favouriteComponents = currentFrame.setFavourites.favouritePanel.getComponents();
                    
                    for(int i = 0; i < favouriteComponents.length; i++) {
                        JButton checkingButton = (JButton)favouriteComponents[i];
                        if(currentFrame.setCurrDir.lastButtonClicked.getActionCommand().equals(checkingButton.getActionCommand())) {
                            currentFrame.setFavourites.buttonToDelete = checkingButton;
                            
                            
                            File isHomeDir = new File(currentFrame.setFavourites.buttonToDelete.getActionCommand());
                            if(isHomeDir.getAbsolutePath().equals(System.getProperty("user.home"))) {
                                currentFrame.setFavourites.deleteFlag = 1;
                            }
                            else {
                                currentFrame.setFavourites.deleteFlag = 0;
                            }
                            
                            currentFrame.setFavourites.buttonToDelete.addActionListener(currentFrame.setFavourites.deleteOp);
                            currentFrame.setFavourites.buttonToDelete.doClick();
                        }
                    }  
                }
                catch(IOException ex) {
                    JOptionPane.showMessageDialog(null,"I/O Exception occured.Close any file related to this folder or file and try again!","ERROR FOUND",JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        if(e.getActionCommand().equals("Add to Favourites")) {
            int isExisted = 0;
            XmlReps xmlOutput;
            
            String home = System.getProperty("user.home");
            File homeDir = new File(home);
            String xmlFolder = "/.java-file-browser/";
            
            File xmlFile = new File(homeDir.getPath() + xmlFolder + "properties.xml");
            
            if(!xmlFile.exists()) {
                File xmlDir = new File(homeDir.getPath() + xmlFolder);
                if(!xmlDir.exists()) {
                    xmlDir.mkdir();
                    isExisted = 1;
                }
                try {
                    xmlFile.createNewFile();
                    isExisted = 1;
                }
                catch(IOException ex) {
                    JOptionPane.showMessageDialog(null,"Exception occured in creation of xml file.","ERROR FOUND",JOptionPane.ERROR_MESSAGE);
                    System.exit(-1);
                }
            }
            
            File fileToFavourites = new File(currentFrame.setCurrDir.lastButtonClicked.getActionCommand());
            
            if(fileToFavourites.isDirectory()) {
                XmlRep xmlElement = new XmlRep(fileToFavourites.getName(),fileToFavourites.getPath());
                
                if(isExisted == 1) {
                    xmlOutput = new XmlReps();
                    xmlOutput.xml.add(xmlElement);
                    
                    try {
                        currentFrame.setFavourites.writeToXml(xmlOutput, xmlFile);
                    }
                    catch(JAXBException ex) {
                        JOptionPane.showMessageDialog(null,"JAXBException occured.","ERROR FOUND",JOptionPane.ERROR_MESSAGE);
                        System.exit(-1);
                    }
                    
                    
                }
                else {
                    
                    try {
                        xmlOutput = (XmlReps) currentFrame.setFavourites.readFromXml(xmlFile);
                        List<XmlRep> xmlList = xmlOutput.getReps();
                        xmlList.add(xmlElement);
                        
                        xmlOutput.setReps(xmlList);
                        
                        currentFrame.setFavourites.writeToXml(xmlOutput, xmlFile);
                    }
                    catch(JAXBException ex) {
                        JOptionPane.showMessageDialog(null,"JAXBException occured.","ERROR FOUND",JOptionPane.ERROR_MESSAGE);
                        System.exit(-1);
                    }
                }
                currentFrame.setFavourites.favouritesButtons(fileToFavourites);
            }
        }
        
        if(e.getActionCommand().equals("Properties")) {
            File fileName = new File(currentFrame.setCurrDir.lastButtonClicked.getActionCommand());
            
            JDialog properties = new JDialog((Dialog) null, "Properties: " + fileName.getName(), true);
            properties.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            
          
            properties.setLayout(new GridLayout(0,2));
            properties.setBounds(new Rectangle(500,250,500,400));
            
            JPanel firstColumn = new JPanel();
            firstColumn.setLayout(new GridLayout(0,1));
            firstColumn.setBackground(new Color(226,210,247));
            
            JPanel secondColumn = new JPanel();
            secondColumn.setLayout(new GridLayout(0,1));
            
            JLabel file;
            JLabel nameFile;
            
            if(fileName.isDirectory()) {
                file = new JLabel("Folder name: ");
                nameFile = new JLabel(fileName.getName());
            }
            else {
                file = new JLabel("File name: ");
                nameFile = new JLabel(fileName.getName());
            }
            
            firstColumn.add(file);
            secondColumn.add(nameFile);
            
            JLabel pathText = new JLabel("Path name: ");
            JLabel namePath = new JLabel(fileName.getPath());
            
            firstColumn.add(pathText);
            secondColumn.add(namePath);
            
            long fileSize = 0;
            
            if(fileName.isDirectory()) {
                fileSize = directorySize(fileName.listFiles(),fileSize);
            }
            else {
                fileSize = fileName.length();
            }
            
            double fileSizeFormat = fileSize;
            int iter = 0;
            
            while(true) {
                if(fileSizeFormat >= 1024) {
                    iter++;
                    fileSizeFormat = fileSizeFormat / 1024;
                }
                else {
                    break;
                }
            }
            
            JLabel sizeText = new JLabel("Size: ");

            JLabel size = switch (iter) {
                case (0) ->
                        new JLabel(String.format("%.2f", fileSizeFormat) + " byte" + " (" + fileSize + " byte" + ")");
                case (1) -> new JLabel(String.format("%.2f", fileSizeFormat) + " KB" + " (" + fileSize + " byte" + ")");
                case (2) -> new JLabel(String.format("%.2f", fileSizeFormat) + " MB" + " (" + fileSize + " byte" + ")");
                case (3) -> new JLabel(String.format("%.2f", fileSizeFormat) + " GB" + " (" + fileSize + " byte" + ")");
                case (4) -> new JLabel(String.format("%.2f", fileSizeFormat) + " TB" + " (" + fileSize + " byte" + ")");
                default -> new JLabel();
            };

            firstColumn.add(sizeText);
            secondColumn.add(size);
            
            properties.add(firstColumn);
            properties.add(secondColumn);
            
            JLabel permissions = new JLabel("Permissions: ");
            
            JCheckBox read;
            JCheckBox write;
            JCheckBox execute;
            
            if(fileName.canRead()) {
                read = new JCheckBox("read", true);
                fileName.setReadable(false);
                if(fileName.canRead()) {
                    read.setEnabled(false);
                }
                else {
                    fileName.setReadable(true);
                }
            }
            else {
                read = new JCheckBox("read", false);
                fileName.setReadable(true);
                if(!fileName.canRead()) {
                    read.setEnabled(false);
                }
                else {
                    fileName.setReadable(false);
                }
            }
            
            if(fileName.canWrite()) {
                write = new JCheckBox("write", true);
                fileName.setWritable(false);
                if(fileName.canWrite()) {
                    write.setEnabled(false);
                }
                else {
                    fileName.setWritable(true);
                }
            }
            else {
                write = new JCheckBox("write", false);
                fileName.setWritable(true);
                if(!fileName.canWrite()) {
                    write.setEnabled(false);
                }
                else {
                    fileName.setWritable(false);
                }
            }
            
            if(fileName.canExecute()) {
                execute = new JCheckBox("execute", true);
                fileName.setExecutable(false);
                if(fileName.canExecute()) {
                    execute.setEnabled(false);
                }
                else {
                    fileName.setWritable(true);
                }
            }
            else {
                execute = new JCheckBox("execute", false);
                fileName.setExecutable(true);
                if(!fileName.canExecute()) {
                    execute.setEnabled(false);
                }
                else {
                    fileName.setWritable(false);
                }
            }
            
            
            read.addItemListener(checkBoxListener);
            write.addItemListener(checkBoxListener);
            execute.addItemListener(checkBoxListener);
            
            JPanel permsPanel = new JPanel();
            permsPanel.setLayout(new BoxLayout(permsPanel, BoxLayout.X_AXIS));
            
            firstColumn.add(permissions);
            
            permsPanel.add(read);
            permsPanel.add(write);
            permsPanel.add(execute);
            
            secondColumn.add(permsPanel);
            
            properties.add(firstColumn);
            properties.add(secondColumn);
            
            properties.setVisible(true);
        }
    }
    
    ItemListener checkBoxListener = new ItemListener() {
      @Override
      public void itemStateChanged(ItemEvent e) {
          File fileName = new File(currentFrame.setCurrDir.lastButtonClicked.getActionCommand());
          
          JCheckBox itemChanged = (JCheckBox) e.getItem();
          
          if(itemChanged.getActionCommand().equals("read")) {
            if(e.getStateChange() == ItemEvent.SELECTED) {
                fileName.setReadable(true);
            }
            if(e.getStateChange() == ItemEvent.DESELECTED) {
                fileName.setReadable(false);
            }
          }
          if(itemChanged.getActionCommand().equals("write")) {
            if(e.getStateChange() == ItemEvent.SELECTED) {
                fileName.setWritable(true);
            }
            if(e.getStateChange() == ItemEvent.DESELECTED) {
                fileName.setWritable(false);
            }
          }
          if(itemChanged.getActionCommand().equals("execute")) {
            if(e.getStateChange() == ItemEvent.SELECTED) {
                fileName.setExecutable(true);
            }
            if(e.getStateChange() == ItemEvent.DESELECTED) {
                fileName.setExecutable(false);
            }
          }
        }
    };
    
    SimpleFileVisitor<Path> fileVisitor = new SimpleFileVisitor<Path> () {
        @Override
        public FileVisitResult visitFile(Path file,BasicFileAttributes attrs) throws IOException {
                Files.delete(file);
                
                if(fileOp == 1) {
                    Component []components = currentFrame.setCurrDir.currentDirectoryPanel.getComponents();
                    for(int i = 0; i < components.length; i++) {
                        JButton buttonToRemove = (JButton) components[i];
                        if(buttonToRemove.getActionCommand().equals(file.toString())) {
                            currentFrame.setCurrDir.currentDirectoryPanel.remove(buttonToRemove);
                            break;
                        }
                    }
                }
                else {
                    currentFrame.setCurrDir.currentDirectoryPanel.remove(currentFrame.setCurrDir.lastButtonClicked);
                }
                currentFrame.setCurrDir.currentDirectoryPanel.repaint();
                currentFrame.setCurrDir.currentDirectoryPanel.revalidate();
                return(FileVisitResult.CONTINUE);
        }
        @Override
        public FileVisitResult postVisitDirectory(Path dir,IOException exc) throws IOException {
            if(exc == null) {
                Files.delete(dir);
                
                if(fileOp == 1) {
                    Component []components = currentFrame.setCurrDir.currentDirectoryPanel.getComponents();
                    for(int i = 0; i < components.length; i++) {
                        JButton buttonToRemove = (JButton) components[i];
                        if(buttonToRemove.getActionCommand().equals(dir.toString())) {
                            currentFrame.setCurrDir.currentDirectoryPanel.remove(buttonToRemove);
                            break;
                        }
                    }
                }
                else {
                    currentFrame.setCurrDir.currentDirectoryPanel.remove(currentFrame.setCurrDir.lastButtonClicked);
                }
                currentFrame.setCurrDir.currentDirectoryPanel.repaint();
                currentFrame.setCurrDir.currentDirectoryPanel.revalidate();
                return(FileVisitResult.CONTINUE);
            }
            else {
                throw exc;
            }
        }
    };
    
    public long directorySize(File[] currentFolder,long size) {
        
        if(currentFolder == null) {
            return(0);
        }

        for (File file : currentFolder) {
            if (file.isDirectory()) {
                size = directorySize(file.listFiles(), size);
            } else {
                size += file.length();
            }
        }
        return(size);
    }
    
    public void paste(File sourceFile,File destFile,int operation) throws IOException {
        
        recPaste(sourceFile,destFile);
        
        
        if(operation == 1) {
           try {
               Files.walkFileTree(source.toPath(), fileVisitor);
           }
            catch(IOException ex) {
                JOptionPane.showMessageDialog(null,"I/O Exception occured.Close any file related to this folder or file and try again!","ERROR FOUND",JOptionPane.ERROR_MESSAGE);
            } 
        }
    }
    
    public boolean isExisted(File sourceFile,File destFile) {
        File [] destContents = destFile.listFiles();
        
        if(sourceFile.isFile()) {
            for (File destContent : destContents) {
                if (destContent.isFile() && sourceFile.getName().equals(destContent.getName())) {
                        return (true);
                    }

            }
            return(false);
        }
        else {
            for (File destContent : destContents) {
                if (destContent.isDirectory() && sourceFile.getName().equals(destContent.getName())) {
                        return (true);
                    }

            }
            return(false);
        }
    }
    
    
    
    public void recPaste(File source,File dest) throws IOException {
        Path sourcePath = source.toPath();
        Path destPath = dest.toPath();
        int optionResult;
        
        File [] sourceContents = source.listFiles();
        File [] destContents = dest.listFiles();
        
        if(source.isFile() || sourceContents == null) {
            if((destContents != null) && (isExisted(source, dest)) && (!source.isDirectory())) {
                optionResult = JOptionPane.showConfirmDialog(null,"Are you sure you want to replace this file?",
                                        "Validation Request",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                
                if(optionResult == JOptionPane.YES_OPTION) {
                    Files.copy(sourcePath,destPath.resolve(sourcePath.getFileName()),StandardCopyOption.REPLACE_EXISTING);
                }
            }
            else {
                Files.copy(sourcePath,destPath.resolve(sourcePath.getFileName()));
            }
        }
        else {
            if((destContents != null) && (isExisted(source, dest))) {
                optionResult = JOptionPane.showConfirmDialog(null,"Are you sure you want to replace this folder?",
                                        "Validation Request",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                
                if(optionResult == JOptionPane.YES_OPTION) {
                    Path newDest = destPath.resolve(sourcePath.getFileName());
                    for (File sourceContent : sourceContents) {
                        recPaste(sourceContent, newDest.toFile());
                    }
                }
            }
            else {
                Path newDest = Files.copy(sourcePath,destPath.resolve(sourcePath.getFileName()));
                for (File sourceContent : sourceContents) {
                    recPaste(sourceContent, newDest.toFile());
                }
            }
        }
    }
}
