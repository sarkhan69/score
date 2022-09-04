import entity.Entity;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
public class Service {

    public Document getDocumentFromHtml(String path) {
        //ожидается полный путь до файла с расширением, например A:\BOT\soccer_rating
        File file = new File(path);
        Document document = null;
        try {
            document = Jsoup.parse(file, "UTF-8", "");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return document;
    }

    public List<Entity> getListOfMatches(Document document) {
        return document.getElementsByClass("text")
                .stream()
                .map(Element::text)
                .filter(v -> v.endsWith("/10"))
                .map(v -> new Entity(v.split("\\|")))
                .collect(Collectors.toList());
    }

    public void set(List<Entity> entityList, String path) {
        try {

            File file = new File(path);
            HSSFWorkbook book = new HSSFWorkbook();
            HSSFSheet sheet = book.createSheet("scoreRatings");


            int rowNum = 0;
            // Нумерация начинается с нуля
            HSSFRow row0 = sheet.createRow(rowNum);

            //колонка время
            HSSFCell name0 = row0.createCell(0);
            name0.setCellValue("Время");
            //колонка событие
            HSSFCell name1 = row0.createCell(1);
            name1.setCellValue("Событие");
            //критерии
            HSSFCell name2 = row0.createCell(2);
            name2.setCellValue("Критерии");

            HSSFCell name3 = row0.createCell(3);
            name3.setCellValue("Индекс");

            HSSFCell name4 = row0.createCell(4);
            name4.setCellValue("Ставка");

            HSSFCell name5 = row0.createCell(5);
            name5.setCellValue("Тайм");

            HSSFCell name6 = row0.createCell(6);
            name4.setCellValue("Итог");

            // Мы запишем имя и дату в два столбца
            // имя будет String, а дата рождения --- Date,
            // формата dd.mm.yyyy
            for (Entity entity : entityList) {
                HSSFCell cell;
                HSSFRow row;
                rowNum++;
                row = sheet.createRow(rowNum);


                //date
                cell = row.createCell(0);
                cell.setCellValue(entity.getDate());
                //событие
                cell = row.createCell(1);
                cell.setCellValue(String.join("|", entity.getCountry(), entity.getLeague(), entity.getTeam1(), entity.getTeam2()));

                //критерии
                cell = row.createCell(2);
                cell.setCellValue(String.join(",", entity.getChance1(), entity.getChance2()));

                //индекс
                cell = row.createCell(3);
                cell.setCellValue(entity.getKf());

                cell = row.createCell(4);

                cell = row.createCell(5);
                cell = row.createCell(6);


            }

            // Меняем размер столбца
            sheet.autoSizeColumn(1);

            // Записываем всё в файл
            book.write(new FileOutputStream(file));
            book.close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
