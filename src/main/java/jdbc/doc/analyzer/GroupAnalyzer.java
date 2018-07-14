package jdbc.doc.analyzer;

import jdbc.doc.data.Group;
import jdbc.doc.data.Table;
import jdbc.doc.data.xml.GroupRule;
import jdbc.doc.data.xml.GroupRules;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Component
public class GroupAnalyzer {
    private static final Logger LOGGER = LogManager.getLogger(GroupAnalyzer.class);
    private List<Group> groups = new ArrayList<>();

    @PostConstruct
    public void initializeGroups() {
        try {
            JAXBContext context = JAXBContext.newInstance(GroupRules.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            InputStream groupsXml = GroupAnalyzer.class.getClassLoader().getResourceAsStream("groups.xml");
            if (groupsXml != null) {
                GroupRules groupRules = (GroupRules) unmarshaller.unmarshal(groupsXml);
                for (GroupRule groupRule : groupRules.getGroupRules()) {
                    Group group = new Group();
                    group.setGroupName(groupRule.getGroupName());
                    for (String pattern : groupRule.getPatterns()) {
                        group.getPatterns().add(Pattern.compile(pattern.trim()));
                    }
                    groups.add(group);
                }
            }
        } catch (JAXBException e) {
            LOGGER.error("Unable to initialize groups", e);
        }
    }

    public void matchTableToGroup(Table table) {
        groups.forEach(group -> group.getPatterns()
                .stream()
                .filter(pattern -> pattern.matcher(table.getTableName()).matches())
                .forEach(pattern -> group.getTables().add(table)));
    }

    public List<Group> getGroups() {
        return groups;
    }
}
