package models;

import autumn.annotation.Model;
import autumn.database.Column;
import autumn.database.Table;

/**
 * Created by infinitu on 14. 12. 18..
 */

@Model("comments")
public class CommentTable extends Table<Comment> {

    public Column<Integer>  cid             = intColumn("cid");
    public Column<Integer>  aid             = intColumn("aid");
    public Column<String>   writer_name     = stringColumn("writer_name");
    public Column<String>   delete_password = stringColumn("delete_password");
    public Column<String>   text            = stringColumn("text");

    public CommentTable() throws NoSuchFieldException {
        super(Comment.class);
    }
}
