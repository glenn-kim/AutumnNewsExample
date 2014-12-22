package controllers;

import autumn.Request;
import autumn.Result;
import autumn.annotation.Controller;
import autumn.annotation.GET;
import autumn.annotation.POST;
import autumn.database.TableQuery;
import autumn.database.jdbc.DBConnection;
import models.Article;
import models.ArticleTable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * Created by infinitu on 14. 12. 18..
 */

@Controller
public class ArticleManagement {

    private static Result notAllowed = Result.Forbidden.plainText("login necessary");
    protected static TableQuery<ArticleTable> tQuery = new TableQuery<>(ArticleTable.class);

    @GET("/")
    public static Result redirectToArticlePage(){
        return Result.TemporaryRedirect("/article").plainText("gogogo");
    }

    @POST("/article")
    public static Result postArticle(Request req) throws IOException {
        String reporter = (String) req.getSession(ReporterLogin.LoginSessionKey);
        if(reporter == null)
            return notAllowed;
        BufferedReader in =new BufferedReader(new InputStreamReader(req.payload()));
        String title = in.readLine();
        String body = ReadBigStringIn(in);

        Article article = new Article();
        article.title = title;
        article.body = body;
        article.create_time = new Timestamp(System.currentTimeMillis());
        article.last_modified = article.create_time;
        article.comment_cnt = 0;
        article.writer = reporter;

        DBConnection dbConn = req.getDBConnection();

        try {
            tQuery.insert(dbConn, new Article[]{article});
        } catch (SQLException e) {
            e.printStackTrace();
            return Result.BadRequest.plainText("failed");
        }
        return Result.Ok.plainText("post successfully");
    }

    @GET("/article")
    public static Result articleUI(Request req) throws IOException, SQLException {

        DBConnection dbConn = req.getDBConnection();
        Object[] objs = tQuery.list(dbConn);
        Article[] articles = new Article[objs.length];
        for(int i = 0; i<objs.length; i++)
            articles[i]=(Article)objs[i];

        String login = (String) req.getSession(ReporterLogin.LoginSessionKey);
        if(login == null)
            login = "false";

        return Result.Ok.template("article")
                .withVariable("articles",articles)
                .withVariable("loginData", login);
    }

    private static String ReadBigStringIn(BufferedReader buffIn) throws IOException {
        StringBuilder everything = new StringBuilder();
        String line;
        while( (line = buffIn.readLine()) != null) {
            everything.append(line);
        }
        return everything.toString();
    }

    @GET("/article/post")
    public static Result articlePostUI(Request req) throws IOException, SQLException {

        String login = (String) req.getSession(ReporterLogin.LoginSessionKey);
        if(login == null)
            Result.TemporaryRedirect("/").plainText("not logined");

        return Result.Ok.template("postArticle");
    }

}
