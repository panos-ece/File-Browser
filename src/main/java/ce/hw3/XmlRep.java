package ce.hw3;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author panpa
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Directory")
public class XmlRep {
    
    @XmlAttribute
    public String directoryName;
    @XmlAttribute
    public String dirPathName;
    
    public XmlRep() {
    }
    
    public XmlRep(String directoryName, String dirPathName) {
        this.directoryName = directoryName;
        this.dirPathName = dirPathName;
    }

    @Override
    public String toString() {
        return "XmlRep{" + "directoryName=" + directoryName + ", DirPathName=" + dirPathName;
    }
}
