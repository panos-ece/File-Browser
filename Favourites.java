package ce326.hw3;


import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import javax.xml.bind.*;
import java.util.List;
import java.util.*;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author panpa
 */
public class Favourites {
    JPanel favouritePanel;
    JPopupMenu popUp;
    MakeGui currentGui;
    static int favouritesClicked;
    static JButton buttonToDelete;
    static int deleteFlag;
    
    public Favourites(MakeGui gui) {
        String home = System.getProperty("user.home");
        File homeDir = new File(home);
        String xmlFolder = "/.java-file-browser/";
        
        currentGui = gui;
        
        favouritePanel = new JPanel();
        favouritePanel.setLayout(new BoxLayout(favouritePanel,BoxLayout.Y_AXIS));
        favouritePanel.setBackground(new Color(255,154,255));
        favouritePanel.setPreferredSize(new Dimension(170, 100));
        
        favouritesButtons(homeDir);
        
        File xmlFile = new File(homeDir.getPath() + xmlFolder + "properties.xml");
        
        if(xmlFile.exists()) {
            try {
                XmlReps xmlData = (XmlReps) readFromXml(xmlFile);
                List<XmlRep> dataList = xmlData.getReps();
                ListIterator<XmlRep> it = dataList.listIterator();
                while(it.hasNext()) {
                    XmlRep directory = it.next();
                    File importFavourites = new File(directory.DirPathName);
                    favouritesButtons(importFavourites);
                }
                
            }
            catch(JAXBException ex) {
                System.out.println("JAXBException occured.");
                ex.printStackTrace();
            }
        }
    }
    
    public void favouritesButtons(File currentFavourite) {
        JButton homeButton = new JButton(currentFavourite.getName(),new ImageIcon("./icons/folder.png"));
        homeButton.setActionCommand(currentFavourite.getPath());
        homeButton.setToolTipText(currentFavourite.getPath());
        homeButton.setFocusPainted(false);
        homeButton.addMouseListener(favouritesOp);
        homeButton.setMinimumSize(favouritePanel.getPreferredSize());
        homeButton.setMaximumSize(favouritePanel.getPreferredSize());
        favouritePanel.add(homeButton);
        favouritePanel.repaint();
        favouritePanel.revalidate();
    }
    
    
    public void writeToXml(XmlReps favouritesDir,File xmlDoc) throws JAXBException{
        JAXBContext jc = JAXBContext.newInstance(XmlReps.class);
        Marshaller writer = jc.createMarshaller();
        writer.marshal(favouritesDir, xmlDoc);
        
    }
    
    public Object readFromXml(File xmlDoc) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(XmlReps.class);
        Unmarshaller reader = jc.createUnmarshaller();
        Object xmlData = reader.unmarshal(xmlDoc);
        
        return(xmlData);
    }
    
    MouseListener favouritesOp = new MouseListener() {
        @Override
        public void mouseClicked(MouseEvent e) {
            if(e.getButton() == MouseEvent.BUTTON1) {
                favouritesClicked = 1;
                JButton chosenButton = (JButton) e.getSource();
                chosenButton.addActionListener(currentGui.setCurrDir.actionOperation);
                chosenButton.doClick();
                chosenButton.removeActionListener(currentGui.setCurrDir.actionOperation);
            }
            if(e.getButton() == MouseEvent.BUTTON3) {
                buttonToDelete = (JButton) e.getSource();
                
                File isHomeDir = new File(buttonToDelete.getActionCommand());
                if(isHomeDir.getAbsolutePath().equals(System.getProperty("user.home"))) {
                    deleteFlag = 1;
                }
                
                popUp = new JPopupMenu();
                
                JMenuItem delete = new JMenuItem("delete");
                delete.addActionListener(deleteOp);
                popUp.add(delete);
                
                popUp.show(e.getComponent(),e.getX(),e.getY());
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
        }

        @Override
        public void mouseReleased(MouseEvent e) {
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }   
    };
    
    ActionListener deleteOp = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            
            if(deleteFlag != 1) {
                String xmlFolder = "/.java-file-browser/";
                File xmlFile = new File(System.getProperty("user.home") + xmlFolder + "properties.xml");
                
                try {
                    XmlReps xmlData = (XmlReps) readFromXml(xmlFile);
                    List<XmlRep> dataList = xmlData.getReps();
                    ListIterator<XmlRep> it = dataList.listIterator();
                    while(it.hasNext()) {
                        XmlRep directory = it.next();
                        if(directory.DirPathName.equals(buttonToDelete.getActionCommand())) {
                            it.remove();
                            xmlData.setReps(dataList);
                            writeToXml(xmlData, xmlFile);
                        }
                    }
                }
                catch(JAXBException ex) {
                    JOptionPane.showMessageDialog(null,"JAXBException occured.","ERROR FOUND",JOptionPane.ERROR_MESSAGE);
                    System.exit(-1);
                }
            }
            
            
            favouritePanel.remove(buttonToDelete);
            favouritePanel.repaint();
            favouritePanel.revalidate();
        }
    };
}
