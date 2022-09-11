package com.score;

import org.jsoup.nodes.Document;

import java.util.Scanner;

public class Application {
    public static void main(String[] args) {
        System.out.println("Укажите путь до необходимого файла вместе с расширением, например : C:\\Users\\file.html :");
        Scanner scanner = new Scanner(System.in);
        String path = scanner.nextLine();

        Service service = new Service();


        Document document = service.getDocumentFromHtml(path);

        System.out.println("Укажите путь до файла  в который мы сохраним итоговый отчет, например C:\\Users\\package\\test.xls");
        String path1 = scanner.nextLine();
        service.set(service.getListOfMatches(document), path1);
    }
}
