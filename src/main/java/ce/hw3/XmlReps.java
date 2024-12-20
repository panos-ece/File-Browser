package ce.hw3;

import java.util.ArrayList;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author panpa
 */

@XmlRootElement(name = "Favourites")
@XmlAccessorType(XmlAccessType.FIELD)
public class XmlReps {
    
    @XmlElement(name = "Directory")
    public List<XmlRep> xml;
    
    
    public XmlReps(){
        xml = new ArrayList<>();
    }
    
    public List<XmlRep> getReps() {
        return xml;
    }
    
    public void setReps(List<XmlRep> xml) {
        this.xml = xml;
    }

    @Override
    public String toString() {
        return xml.toString();
    }
}
