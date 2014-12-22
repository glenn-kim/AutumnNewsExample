package models;

import java.sql.Timestamp;

/**
 * Created by infinitu on 14. 12. 18..
 */
public class Article {
    public int          id              ;
    public String       writer          ;
    public String       title           ;
    public String       body            ;
    public Timestamp    create_time     ;
    public Timestamp    last_modified   ;
    public int          comment_cnt     ;
}
