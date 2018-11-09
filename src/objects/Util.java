package objects;

import javafx.collections.ObservableList;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;

public final class Util {

    public static String resDays(LocalDate date) {
        int day = date.getDayOfMonth();
        int dayOfWeek = date.getDayOfWeek().getValue();
        int month = date.getMonthValue();
        int year = date.getYear();

        String monthName = Util.convertMonthToString(month).substring(0,3);
        String weekDayName = Util.convertWeekDayToString(dayOfWeek);

        return weekDayName + ", " + day + " " + monthName + ", " + year;
    }

    public static String convertMonthToString(int n) {
        String monthName = "";
        switch (n) {
            case 1:
                monthName = "января";
                break;
            case 2:
                monthName = "февраля";
                break;
            case 3:
                monthName = "марта";
                break;
            case 4:
                monthName = "апреля";
                break;
            case 5:
                monthName = "мая";
                break;
            case 6:
                monthName = "июня";
                break;
            case 7:
                monthName = "июля";
                break;
            case 8:
                monthName = "августа";
                break;
            case 9:
                monthName = "сентября";
                break;
            case 10:
                monthName = "октября";
                break;
            case 11:
                monthName = "ноября";
                break;
            case 12:
                monthName = "декабря";
                break;
        }

        return monthName;
    }

    public static String convertWeekDayToString(int n) {
        String weekDayName = "";
        switch (n) {
            case 1:
                weekDayName = "Пн";
                break;
            case 2:
                weekDayName = "Вт";
                break;
            case 3:
                weekDayName = "Ср";
                break;
            case 4:
                weekDayName = "Чт";
                break;
            case 5:
                weekDayName = "Пт";
                break;
            case 6:
                weekDayName = "Сб";
                break;
            case 7:
                weekDayName = "Вс";
                break;
        }
        return weekDayName;
    }

    public static void rewriteData(ObservableList<Order> orders, File file) {
        try (FileWriter writer = new FileWriter(file)) {
            StringBuilder data = new StringBuilder();
            for (Order order : orders) {
                data.append(order.toString() + "\n");
            }
            writer.write(data.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
