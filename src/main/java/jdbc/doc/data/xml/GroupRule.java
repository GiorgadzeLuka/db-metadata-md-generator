package jdbc.doc.data.xml;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.util.List;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class GroupRule {
    private String groupName;
    @XmlElementWrapper(name = "patterns")
    @XmlElement(name = "pattern")
    private List<String> patterns;
}
