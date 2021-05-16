package ce326.hw3;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import javax.swing.filechooser.FileSystemView;
import java.io.*;
/**
 *
 * @author panpa
 */
public class FileSystem extends FileSystemView {
    
    File fileSystemRoot;
    public FileSystem(File f) {
        super();
        fileSystemRoot =  super.createFileSystemRoot(f);
    }
    
    @Override
    public File createNewFolder(File containingDir) {
        containingDir.mkdir();
        return(containingDir);
    }
}
