import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParseHtml {
    static JdbcTemplate jdbcTemplate = new JdbcTemplate(getDataSource());
    public static void main(String[] args) throws IOException {
        delete("mailrucostnaso");
        createTable("mailrucostnaso");
        setDataSource(connect());
    }



    public static void setDataSource(StringBuilder sb) {
        ArrayList<Table> list = new ArrayList<Table>();
        Document document = Jsoup.parse(sb.toString());
        String AfterParse = document.text();
        Pattern pattern = Pattern.compile(",");
        Matcher matcher = pattern.matcher(AfterParse);
        String document1 = matcher.replaceAll(".");
        Pattern pattern1 = Pattern.compile("(0[1-9]|[12][0-9]|3[01])\\.(0[1-9]|1[012])\\.((20)\\d\\d)\\s\\d\\d\\S\\d\\d\\d\\d\\s\\d\\d\\S\\d\\d\\d\\d\\s\\d\\d\\S\\d\\d\\d\\d\\s\\d\\d\\S\\d\\d\\d\\d\\s");
        Matcher matcher1 = pattern1.matcher(document1);
        while (matcher1.find()) {
            String numbers = matcher1.group();
            String[] substr = numbers.split(" ");


            Table table = new Table();

            table.setData(substr[0]);

            double open = Double.parseDouble(substr[1]);
            table.setOpen(open);

            double close = Double.parseDouble(substr[2]);
            table.setClose(close);

            double min = Double.parseDouble(substr[3]);
            table.setMin(min);

            double max = Double.parseDouble(substr[4]);
            table.setMax(max);

            list.add(table);
            save(table);

        }
        for (int i = 0; i <list.size() ; i++) {
            {
                Table currentCode = list.get(i);
                System.out.println("Date:       "+currentCode.getData());
                System.out.println("Opening:    "+currentCode.getOpen());
                System.out.println("Closing:    "+currentCode.getClose());
                System.out.println("Maximum:    "+String.format("%.0f",currentCode.getMax()));
                System.out.println("Minimum:    "+currentCode.getMin());
                System.out.println("Numbers:    "+currentCode.getNumbers());
            }

        }
    }





    public static DataSource getDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl("jdbc:mysql://localhost:3306/dbtest?autoReconnect=true&useSSL=false");
        dataSource.setUsername("root");
        dataSource.setPassword("root");
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        return dataSource;

    }

    public static void save(Table table) {
        // String sql = "INSERT  into mailrucostnaso
        String sql = "INSERT into  mailrucostnaso (DMY,Opening,Closing,Maximum,Minimum) values (?,?,?,?,?)";
        jdbcTemplate.update(sql, table.getData(), table.getOpen(), table.getClose(), table.getMax(), table.getMin());
    }

    public static void delete(String tableName) {
        jdbcTemplate.execute("DROP TABLE if EXISTS "+tableName) ;
    }

    public static void createTable(String tableName) {

        jdbcTemplate.execute( "CREATE TABLE IF NOT EXISTS " + tableName
                + " ( ID INTEGER PRIMARY KEY AUTO_INCREMENT,"
                + "  DMY           VARCHAR(10),"
                + "   Opening            DOUBLE ,"
                + "   Closing          DOUBLE ,"
                + "   Maximum           DOUBLE ,"
                + "   Minimum           DOUBLE )");

    }
    public static StringBuilder connect() throws IOException {
        String urlParameters = "param1=a&param2=b&param3=c";
        byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);
        int postDataLength = postData.length;
        String request = "https://www.finanz.ru/Ajax/SharesController_HistoricPriceList/mail-ru/NASO/6.3.2018_6.4.2018";
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) new URL(request).openConnection();

            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setInstanceFollowRedirects(false);
            conn.setRequestProperty( "__atcrv", "71567996");
            conn.setRequestProperty( "__ath", "4MiYHcTwODUJoXK1T0fcvQ5GL4C+I0DzQJHnK/aro5Q=");
            conn.setRequestProperty( "__atts", "2018-04-06-17-49-35");
            conn.setRequestProperty("Accept", "*/*");
            conn.setRequestProperty("Accept-Encoding", "ggzip, deflate, br");
            conn.setRequestProperty("Accept-Language", "ru-RU,ru;q=0.9,en-US;q=0.8,en;q=0.7");
            conn.setRequestProperty("ConnectToURL", "keep-alive");
            conn.setRequestProperty("Cache-Control", "no-cache");
            conn.setRequestProperty("ConnectToURL", "keep-alive");
            conn.setRequestProperty("Cookie", "__utmz=2796199.1522165749.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); _ga=GA1.2.353552866.1522165749; _gid=GA1.2.892980135.1522165749; _omappvp=MfzFuy1N6pwEmJBzuxXLZcXmjIpzTzL13RANCeOF8BS21VjEjFpmuZAvn6jELIauOOmlPcdmlJ5PmcMEt33SybiZHrlS2yCz; om-710224=true; __utma=2796199.353552866.1522165749.1522165749.1522217556.2; __utmc=2796199; _gat_UA-80065238-1=1; LentaInformStorage=%7B%220%22%3A%7B%22svspr%22%3A%22%22%2C%22svsds%22%3A13%2C%22TejndEEDj%22%3A%22TvCzZho9N%22%7D%2C%22C610897%22%3A%7B%22page%22%3A1%2C%22time%22%3A1522221484287%7D%2C%22C628816%22%3A%7B%22page%22%3A1%2C%22time%22%3A1522221020778%7D%7D");
            conn.setRequestProperty("Host", "www.finanz.ru");
            conn.setRequestProperty("Origin", "http://www.finanz.ru");
            conn.setRequestProperty("Pragma", "no-cache");
            conn.setRequestProperty("Referer", "http://www.finanz.ru/aktsii/arhiv-torgov/mail-ru/NASO/28.2.2018_28.3.2018");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36");
            conn.setRequestProperty("X-NewRelic-ID", "UQQOUV9QGwcFUFFbBQk=");
            conn.setRequestProperty("X-Requested-With", "XMLHttpRequest");
            conn.setRequestProperty("Content-Length", Integer.toString(postDataLength));
            try (DataOutputStream wr = new DataOutputStream(conn.getOutputStream())) {
                wr.write(postData);
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

        StringBuilder sb = new StringBuilder();

        if (HttpURLConnection.HTTP_OK == conn.getResponseCode()) {
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String string;
            while ((string = in.readLine()) != null) {
                sb.append(string);
                sb.append("\n");
            }
        } else {
            System.out.println("fail " + conn.getResponseCode() + "," + conn.getResponseMessage());
        }
        return sb;
    }
}
