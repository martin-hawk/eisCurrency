package eiscurrency;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

public class UI {

    private Scanner reader;

    public UI() {
        this.reader = new Scanner(System.in);
    }

    public void run() throws Exception {
        String currency = "";
        String dateFrom = "";
        String dateTo = "";
//         interface logic 
        boolean exit = false;
        while (!exit) {

            System.out.println("Pasirinkite veiksmą:");
            System.out.println("1 - parodyti valiutos kursą datai");
            System.out.println("2 - Parodyti valiutos pokytį per laikotarpį");
            System.out.println("0 - baigti darbą");
            System.out.print(">> ");
            String choise = reader.nextLine();

            WebService ws;
            if (choise.equals("0")) {
                exit = true;
            } else if (choise.equals("1")) {
                System.out.print("Nurodykite valiutą: ");
                System.out.print(">> ");
                currency = reader.nextLine().toUpperCase();

                while (!checkDate(dateFrom)) {
                    System.out.print("Nurodykite datą: ");
                    System.out.print(">> ");
                    dateFrom = reader.nextLine();
                    if (dateFrom.isEmpty()) {
                        dateFrom = returnTodaysDate();
                    }
                }

                ws = request(currency, dateFrom, "");
                ArrayList<CurrencyRate> al = ws.getList();
                for (CurrencyRate cr : al) {
                    System.out.println(cr);
                }
                System.out.println();

            } else if (choise.equals("2")) {
                System.out.print("Nurodykite valiutą: ");
                System.out.print(">> ");
                currency = reader.nextLine().toUpperCase();

                while (!checkDate(dateFrom)) {
                    System.out.print("Nurodykite datą Nuo: ");
                    System.out.print(">> ");
                    dateFrom = reader.nextLine();
                }

                while (!checkDate(dateTo)) {
                    System.out.print("Nurodykite datą Iki: ");
                    System.out.print(">> ");
                    dateTo = reader.nextLine();
                }

                ws = request(currency, dateFrom, dateTo);

                ArrayList<CurrencyRate> al = ws.getList();
                for (CurrencyRate cr : al) {
                    System.out.println(cr);
                }
                System.out.println("Valiutos kurso pokytis: " + returnDifference(al.get(al.size() - 1), al.get(0)));
                System.out.println();
            }
            // clear the values
            currency = "";
            dateFrom = "";
            dateTo = "";
        }
    }

    static WebService request(String currency, String dateFrom, String dateTo) throws Exception {
        WebService http = new WebService(currency, dateFrom, dateTo);
        http.sendGet();
        return http;
    }

    static boolean checkCurrency(String currency) {
// Would be nice to check whether the currency code exists in the list
// http://www.lb.lt/webservices/FxRates/FxRates.asmx?op=getCurrencyList
// else return a message to the user
        return false;
    }

    static boolean checkDate(String date) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("uuuu-MM-dd", Locale.US)
                .withResolverStyle(ResolverStyle.STRICT);
        DateValidator validator = new DateValidator(dateFormatter);

        return validator.isValid(date);
    }

    static float returnDifference(CurrencyRate old, CurrencyRate present) {
        return ((present.getAmtTo() * 10000) - (old.getAmtTo() * 10000)) / 10000;
    }

    static String returnTodaysDate() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.now();

        return dtf.format(localDate);
    }
}
