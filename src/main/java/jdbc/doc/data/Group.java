package jdbc.doc.data;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

@Data
public class Group {
    private String groupName;
    private List<Pattern> patterns = new ArrayList<>();
    private Set<Table> tables = new HashSet<>();
}
