/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eiscurrency;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 *
 * @author marty
 */
public class WebService {

    private final String USER_AGENT = "Mozilla/5.0";
    String type = "EU";
    String dateFrom;
    String dateTo;
    String currency;
    StringBuilder response;
    private ArrayList<CurrencyRate> list = new ArrayList<CurrencyRate>();

    public WebService(String currency, String dateFrom, String dateTo) {
        this.currency = currency;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        response = new StringBuilder();
        list = new ArrayList<CurrencyRate>();
    }

    // HTTP GET request
    public void sendGet() throws Exception {

        String url = "https://www.lb.lt/webservices/FxRates/FxRates.asmx/getFxRatesForCurrency?tp="
                + this.type + "&ccy=" + this.currency + "&dtFrom=" + this.dateFrom + "&dtTo=" + this.dateTo;

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // optional default is GET
        con.setRequestMethod("GET");

        //add request header
        con.setRequestProperty("User-Agent", USER_AGENT);

        int responseCode = con.getResponseCode();
        //System.out.println("\nSending 'GET' request to URL : " + url);
        //System.out.println("Response Code : " + responseCode);
        if (responseCode == 200) {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;

            String type = "";
            String date = "";
            String currencyFrom = "";
            float rateFrom = 0;
            String currencyTo = "";
            float rateTo = 0;

            while ((inputLine = in.readLine()) != null) {

                if (inputLine.contains("FxRate") || inputLine.contains("<CcyAmt>") || inputLine.contains("</CcyAmt>")) {
                    continue;
                }

                if (inputLine.contains("<Tp>")) {
                    type = inputLine.trim().replaceAll("<[^>]+>", "");
                }
                if (inputLine.contains("<Dt>")) {
                    date = inputLine.trim().replaceAll("<[^>]+>", "");
                }
                if (inputLine.contains("<Ccy>") && currencyFrom.isEmpty()) {
                    currencyFrom = inputLine.trim().replaceAll("<[^>]+>", "");
                } else if (inputLine.contains("<Ccy>") && !currencyFrom.isEmpty()) {
                    currencyTo = inputLine.trim().replaceAll("<[^>]+>", "");
                }
                if (inputLine.contains("<Amt>") && rateFrom == 0) {
                    rateFrom = Float.parseFloat(inputLine.trim().replaceAll("<[^>]+>", ""));
                } else if (inputLine.contains("<Amt>") && rateFrom != 0) {
                    rateTo = Float.parseFloat(inputLine.trim().replaceAll("<[^>]+>", ""));
                }

                if (!type.isEmpty() && !date.isEmpty() && !currencyFrom.isEmpty() && rateFrom != 0 && !currencyTo.isEmpty() && rateTo != 0) {
                    CurrencyRate cr = new CurrencyRate(type, date, currencyFrom, rateFrom, currencyTo, rateTo);
                    list.add(cr);
//                    System.out.println(cr);
                    type = "";
                    date = "";
                    currencyFrom = "";
                    rateFrom = 0;
                    currencyTo = "";
                    rateTo = 0;
                }

                response.append(inputLine);
            }
            in.close();

            //print result
            //System.out.println(response);
        }
    }

    public ArrayList<CurrencyRate> getList() {
        return list;
    }
}
