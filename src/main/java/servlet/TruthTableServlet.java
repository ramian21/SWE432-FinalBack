package servlet;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

@WebServlet(name = "MyServlet", urlPatterns = { "/tt" })

public class TruthTableServlet extends HttpServlet {

    private ArrayList<Integer> ops = new ArrayList<Integer>();
    // 0 for or
    // 1 for and
    // 2 for xor

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        res.setContentType("text/html");
        PrintWriter out = res.getWriter();

        out.println("<HTML>");
        out.println("<HEAD>");
        out.println("<TITLE>Invalid request</TITLE>");
        out.println("</HEAD>");

        out.println("<BODY>");
        out.println("<CENTER>");
        out.println("<P>Invalid GET request: This service only accepts POST requests</P>");
        out.println("</CENTER>");
        out.println("</BODY>");

        out.println("</HTML>");
        out.flush();

        out.close();
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("text/html");
        // res.setContentType("application/json");
        res.setHeader("Access-Control-Allow-Origin", "*");
        res.setHeader("Access-Control-Allow-Methods", "POST");
        res.setHeader("Access-Control-Allow-Headers", "*");
        PrintWriter out = res.getWriter();

        ArrayList<String> clauses = new ArrayList<String>();
        Map<String, String[]> params = req.getParameterMap();
 
        if (params.get("inputString") == null) {
            System.out.println("error");
            out.println("<span>No expression detected.</span>");
            out.close();
            return;
            // error
        }       
        String s = params.get("inputString")[0];
        String[] args = s.split(" ");
        for (String tmp : args) {
            if (tmp.equals("AND")) {
                ops.add(1);
            } else if (tmp.equals("OR")) {
                ops.add(0);
            } else if (tmp.equals("XOR")) {
                ops.add(2);
            } else {
                clauses.add(tmp);
            }
            System.out.printf("%s\n", tmp);
        }
        int numClauses = clauses.size();
        boolean[] truthVals = new boolean[numClauses];

        System.out.println(clauses.size());
        System.out.println(ops.size());
        for (String string : clauses) {
            System.out.printf("%s\n", string);
            
        }
        for (Integer string : ops) {
            System.out.printf("%d\n", string);
            
        }
        if ((clauses.size() - ops.size() != 1) || ops.size() == 0 || clauses.size() == 0) {
            out.println("<span>The predicate cannot be parsed, please check the logic operators and clauses.</span>");
            out.close();
            return;
            // error
        }

        out.println("<table>");
        out.println("<tbody>");

        out.println("<tr>");
        for (int i = 0; i < numClauses; i++) {
            out.print("<th>");
            out.print(clauses.get(i));
            out.print("</th>");
        }
        out.print("<th>");
        out.print("Result");
        out.print("</th>");
        out.println("</tr>");

        generateTableData(out, numClauses, 0, truthVals);
        out.println("</tbody>");
        out.println("</table>");

        out.close();
        ops = new ArrayList<Integer>();

        // res.setContentType("application/json");
        // res.setHeader("Access-Control-Allow-Origin", "");
        // res.setHeader("Access-Control-Allow-Methods", "POST");
        // res.setHeader("Access-Control-Allow-Headers", "");

        // retVals.put("result" + i, resultValue);

    }

    private void generateTableData(PrintWriter out, int numClauses, int index, boolean[] truthVals) {
        if (index == numClauses) {
            out.println("<tr>");
            for (int i = 0; i < numClauses; i++)
                out.println("<td>" + (truthVals[i] ? "T" : "F") + "</td>");
            if (ops.get(0) == 0) {
                out.println("<td>" + ((truthVals[0] || truthVals[1]) ? "T" : "F") + "</td>");
            } else if (ops.get(0) == 1) {
                out.println("<td>" + ((truthVals[0] && truthVals[1]) ? "T" : "F") + "</td>");
            } else {
                out.println("<td>" + ((truthVals[0] ^ truthVals[1]) ? "T" : "F") + "</td>");
            }
            out.print("</tr>");
        } else {
            for (int i = 0; i < 2; i++) {
                truthVals[index] = (i == 0 ? false : true);
                generateTableData(out, numClauses, index + 1, truthVals);
            }
        }

    }
}
