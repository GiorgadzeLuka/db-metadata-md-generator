package jdbc.doc.data;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Reference {
    private String referencedTable;
    private String referencedColumn;
}
