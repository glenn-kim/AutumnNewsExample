package models;

import autumn.annotation.Model;
import autumn.database.Column;
import autumn.database.Table;

/**
 * Created by infinitu on 14. 12. 18..
 */

@Model("reporters")
public class ReporterTable extends Table<Reporter> {

    public Column<String> email     = stringColumn("email");
    public Column<String> passwd    = stringColumn("password");
    public Column<String> name      = stringColumn("name");
    public Column<String> depart    = stringColumn("depart");
    public Column<String> telephone = stringColumn("telephone");

    public ReporterTable() throws NoSuchFieldException {
        super(Reporter.class);
    }
}
