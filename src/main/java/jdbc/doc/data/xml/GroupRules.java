package jdbc.doc.data.xml;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@Data
@XmlRootElement(name = "groups")
@XmlAccessorType(XmlAccessType.FIELD)
public class GroupRules {
    @XmlElement(name = "group")
    private List<GroupRule> groupRules;
}


