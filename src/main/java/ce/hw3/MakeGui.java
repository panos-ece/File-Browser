package ce.hw3;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.awt.*;
import javax.swing.*;

/**
 *
 * @author panpa
 */
public class MakeGui {
    JFrame frame;
    Menu setMenu;
    Favourites setFavourites;
    JPanel frameSearchPanel;
    SearchToolbar setSearchToolbar;
    JPanel panelBread;
    JPanel panelCurrent;
    BreadCrumb setBreadCrumb;
    CurrentDirectory setCurrDir;
    
    public MakeGui() {
        frame = new JFrame("File Browser");
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        setMenu = new Menu(this);
        frame.add(setMenu.bar,BorderLayout.NORTH);
        
        setFavourites = new Favourites(this);
        
        frame.add(setFavourites.favouritePanel,BorderLayout.WEST);
        
        frameSearchPanel = new JPanel();
        frameSearchPanel.setLayout(new BorderLayout());
        
        setSearchToolbar = new SearchToolbar(this);
        frameSearchPanel.add(setSearchToolbar.searchPanel,BorderLayout.NORTH);
        
        JPanel breadAndCurr = new JPanel();
        breadAndCurr.setLayout(new BorderLayout());
        
        panelBread = new JPanel();
        panelBread.setLayout(new BorderLayout());
        
        
        setBreadCrumb = new BreadCrumb(this);
        panelBread.add(setBreadCrumb.breadCrumbPanel,BorderLayout.NORTH);
        
        panelCurrent = new JPanel();
        panelCurrent.setLayout(new BorderLayout());
        
        setCurrDir = new CurrentDirectory(setBreadCrumb.currDirFile,this);
        panelCurrent.add(setCurrDir.currentDirectoryPanel,BorderLayout.CENTER);
        
        JScrollPane scroll = new JScrollPane(setCurrDir.currentDirectoryPanel);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        
        panelCurrent.add(scroll,BorderLayout.CENTER);
        
        breadAndCurr.add(panelBread,BorderLayout.NORTH);
        breadAndCurr.add(panelCurrent,BorderLayout.CENTER);
        
        frameSearchPanel.add(breadAndCurr,BorderLayout.CENTER);
        
        frame.add(frameSearchPanel,BorderLayout.CENTER);
        
        frame.setBounds(new Rectangle(500,250,1000,600));
        frame.setVisible(true);
    }
}
