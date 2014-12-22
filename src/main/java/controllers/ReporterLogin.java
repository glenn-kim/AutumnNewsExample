package controllers;

import autumn.Request;
import autumn.Result;
import autumn.annotation.Controller;
import autumn.annotation.GET;
import autumn.annotation.PATH;
import autumn.annotation.POST;
import autumn.database.TableQuery;
import autumn.database.jdbc.DBConnection;
import models.Reporter;
import models.ReporterTable;

import java.io.*;
import java.sql.SQLException;

/**
 * Created by infinitu on 14. 12. 18..
 */

@Controller
@PATH("/reporter")
public class ReporterLogin {

    private static Result loginFailed = Result.Forbidden.plainText("login Failed");

    protected final static String LoginSessionKey = "reporter";
    private final static String emailRegex = "^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,6}$";

    private static final TableQuery<ReporterTable> tQuery =  new TableQuery<>(ReporterTable.class);

    @POST("/login")
    public static Result Login(Request req) throws IOException {
        BufferedReader in =new BufferedReader(new InputStreamReader(req.payload()));
        String email = in.readLine();
        String pass = in.readLine();

        if(email==null||pass==null)
            return loginFailed;

        if(!email.toLowerCase().matches(emailRegex))
            return loginFailed;

        final String finalPass = Encrypter.getInstance().password(pass);

        DBConnection dbConn = req.getDBConnection();

        Reporter reporter;
        try {
            reporter = (Reporter)
                    tQuery.where((t) ->
                    (t.email).isEqualTo(email).and(
                            (t.passwd).isEqualTo(finalPass)))
                    .first(dbConn);
        } catch (SQLException e) {
            e.printStackTrace();
            return loginFailed;
        }

        if(reporter == null)
            return loginFailed;


        return Result.Ok.plainText("login OK").withNewSession(LoginSessionKey,reporter.email);
    }



    @POST("/register")
    public static Result register(Request req) throws IOException {
        BufferedReader in =new BufferedReader(new InputStreamReader(req.payload()));


        Reporter reporter = new Reporter();
        reporter.email = in.readLine();
        reporter.passwd = in.readLine();
        reporter.passwd = Encrypter.getInstance().password(reporter.passwd);
        reporter.name = in.readLine();
        reporter.depart = in.readLine();
        reporter.telephone = in.readLine();


        if(!reporter.email.toLowerCase().matches(emailRegex))
            return Result.BadRequest.plainText("no email type");

        DBConnection dbConn = req.getDBConnection();


        try {
            tQuery.insert(dbConn, new Reporter[]{reporter});
        } catch (SQLException e) {
            e.printStackTrace();
            return Result.BadRequest.plainText("failed");
        }

        return Result.Ok.plainText("register OK");
    }



    @GET("/login")
    public static Result loginUI(Request req) {
        String loginData = (String)req.getSession(LoginSessionKey);
        if(loginData!=null)
            return Result.TemporaryRedirect("/").plainText("already Logined");
        return Result.Ok.template("login");
    }

    @GET("/register")
    public static Result registerUI(Request req) {
        String loginData = (String)req.getSession(LoginSessionKey);
        return Result.Ok.template("register");
    }

    @GET("/logout")
    public static Result logout(Request req) {
        return Result.Ok.plainText("successfully").withNewSession();
    }
}
