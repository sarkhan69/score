import entity.Entity;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;

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
            HSSFCellStyle style = book.createCellStyle();
            style.setBorderTop(BorderStyle.MEDIUM);
            style.setBorderBottom(BorderStyle.MEDIUM);
            style.setBorderLeft(BorderStyle.MEDIUM);
            style.setBorderRight(BorderStyle.MEDIUM);

            int rowNum = 0;
            int countColumnD = 1;
            // Нумерация начинается с нуля
            HSSFRow row0 = sheet.createRow(rowNum);

            //колонка время
            HSSFCell name0 = row0.createCell(0);
            name0.setCellValue("Время");
            name0.setCellStyle(style);
            //колонка событие
            HSSFCell name1 = row0.createCell(1);
            name1.setCellValue("Событие");
            name1.setCellStyle(style);
            //критерии
            HSSFCell name2 = row0.createCell(2);
            name2.setCellValue("Критерии");
            name2.setCellStyle(style);

            HSSFCell name3 = row0.createCell(3);
            name3.setCellValue("Индекс");
            name3.setCellStyle(style);

            HSSFCell name4 = row0.createCell(4);
            name4.setCellValue("Ставка");
            name4.setCellStyle(style);

            HSSFCell name5 = row0.createCell(5);
            name5.setCellValue("Тайм");
            name5.setCellStyle(style);

            HSSFCell name6 = row0.createCell(6);
            name6.setCellValue("Итог");
            name6.setCellStyle(style);

            HSSFCell name11 = row0.createCell(11);
            name11.setCellValue("гол К1");

            HSSFCell name12 = row0.createCell(12);
            name12.setCellValue("гол К2");

            for (Entity entity : entityList) {
                HSSFCell cell;
                HSSFRow row;
                rowNum++;
                countColumnD++;
                row = sheet.createRow(rowNum);


                //date
                cell = row.createCell(0);
                cell.setCellValue(entity.getDate());
                cell.setCellStyle(style);
                //событие
                cell = row.createCell(1);
                cell.setCellValue(String.join("|", entity.getCountry(), entity.getLeague(), entity.getTeam1(), entity.getTeam2()));
                cell.setCellStyle(style);
                //критерии
                cell = row.createCell(2);
                cell.setCellValue(String.join(",", entity.getChance1(), entity.getChance2()));
                cell.setCellStyle(style);
                //индекс
                cell = row.createCell(3, CellType.NUMERIC);
                cell.setCellValue(Double.parseDouble(entity.getKf()));
                cell.setCellStyle(style);
                //ставка
                cell = row.createCell(4);

                String formulaTemplate = "if(D%d<0,$L$1,if(D%d>0,$M$1))";
                cell.setCellFormula(String.format(formulaTemplate, countColumnD, countColumnD));
                cell.setCellStyle(style);
            }

            sheet.autoSizeColumn(1);
            book.write(new FileOutputStream(file));
            book.close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
