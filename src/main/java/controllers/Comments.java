package controllers;

import autumn.Request;
import autumn.Result;
import autumn.annotation.*;
import autumn.database.JoinQuery;
import autumn.database.TableQuery;
import autumn.database.jdbc.DBConnection;
import models.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;

/**
 * Created by infinitu on 14. 12. 18..
 */

@Controller
public class Comments {

    private static JoinQuery<ArticleCommentJoinTable> jQuery = new JoinQuery<>(ArticleCommentJoinTable.class);
    private static TableQuery<CommentTable> tQuery = new TableQuery<>(CommentTable.class);
    private static Result notAllowed = Result.Forbidden.plainText("failed");

    @GET("/:article/comments")
    public static Result getComments(@INP("article") String aid, Request req) throws SQLException {
        DBConnection dbConn = req.getDBConnection();
        int inAid =Integer.parseInt(aid);
        Object[] objs = jQuery.where((t) -> (t.aid).isEqualTo(inAid)).list(dbConn);
        ArticleComment[] articleComments = new ArticleComment[objs.length];
        for(int i = 0; i<objs.length; i++)
            articleComments[i]=(ArticleComment)objs[i];


        String title;

        if(objs.length==0){
            Article article = (Article) ArticleManagement.tQuery.where((t) -> t.id .isEqualTo (inAid)).first(dbConn);
            title = article.title;
        }
        else{
            title = articleComments[0].title;
        }

        return Result.Ok.template("comment")
                .withVariable("articles", articleComments)
                .withVariable("aid", aid)
                .withVariable("title",title);
    }

    @POST("/:article/comments")
    public static Result postComments(@INP("article") String aid, Request req) throws IOException {
        DBConnection dbConn = req.getDBConnection();

        BufferedReader in =new BufferedReader(new InputStreamReader(req.payload()));
        String name = in.readLine();
        String pass = in.readLine();
        String text = in.readLine();
        Comment comment = new Comment();
        comment.aid = Integer.parseInt(aid);
        comment.text = text;
        comment.delete_password = pass;
        comment.writer_name = name;

        try {
            tQuery.insert(dbConn, new Comment[]{comment});
        } catch (SQLException e) {
            e.printStackTrace();
            return Result.BadRequest.plainText("failed");
        }
        return Result.Ok.plainText("ok");
    }

    @DELETE("/:article/comments/:comment")
    public static Result deleteComments(@INP("article") String aid,@INP("comment") String cid, Request req) throws SQLException, IOException {
        BufferedReader in =new BufferedReader(new InputStreamReader(req.payload()));
        String pass = in.readLine();

        DBConnection dbConn = req.getDBConnection();

        int i = tQuery
                .where((t) -> (t.cid).isEqualTo(Integer.parseInt(cid)).and(
                        t.delete_password.isEqualTo(pass)
                )).delete(dbConn);
        if(i==0)
            return notAllowed;

        return Result.Ok.plainText("successfully");
    }

}
