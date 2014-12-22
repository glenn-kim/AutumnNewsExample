package models;

import autumn.annotation.Model;
import autumn.database.Column;
import autumn.database.Table;

import java.sql.Timestamp;

/**
 * Created by infinitu on 14. 12. 18..
 */

@Model("articles")
public class ArticleTable extends Table<Article> {

    public Column<Integer>      id              = intColumn("aid");
    public Column<String>       writer          = stringColumn("writer");
    public Column<String>       title           = stringColumn("title");
    public Column<String>       body            = stringColumn("body");
    public Column<Timestamp>    create_time     = timestampColumn("create_time");
    public Column<Timestamp>    last_modified   = timestampColumn("last_modified");
    public Column<Integer>      comment_cnt     = intColumn("comment_cnt");

    public ArticleTable() throws NoSuchFieldException {
        super(Article.class);
    }
}
