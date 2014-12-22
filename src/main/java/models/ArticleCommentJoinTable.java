package models;

import autumn.annotation.Model;
import autumn.database.Column;
import autumn.database.Condition;
import autumn.database.JoinTable;


/**
 * Created by infinitu on 14. 12. 18..
 */

public class ArticleCommentJoinTable extends JoinTable<ArticleTable,CommentTable,ArticleComment> {

    public Column<Integer>  aid             = left.id;
    public Column<String>   title           = left.title;

    public Column<Integer>  cid             = right.cid;
    public Column<String>   writer_name     = right.writer_name;
    public Column<String>   text            = right.text;


    public ArticleCommentJoinTable() throws NoSuchFieldException {
        super(new ArticleTable(),new CommentTable(),ArticleComment.class);
    }

    @Override
    public Condition on(ArticleTable articleTable, CommentTable commentTable) {
        return (left.id) .isEqualTo (right.aid);
    }
}
