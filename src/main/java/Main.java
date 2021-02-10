import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class Main {

    static final Logger LOGGER = LogManager.getLogger(Main.class);
    static final Marker EXCEPTION_MARKER = MarkerManager.getMarker("EXCEPTION");
    static final Marker PRINT_MARKER = MarkerManager.getMarker("PRINT");

    static final String saveTarget = "src/main/resources/data/";
    static final String parseWebTarget = "https://lenta.ru";

    public static void main(String[] args) {

        try {
            Document document = Jsoup.connect(parseWebTarget).get();
            Elements elements = document.select("img.g-picture");  //img[src$=.png]
            elements
                    .stream()
                    .map(element -> element.attr("abs:src"))     //abs - получает Абсолютный путь
                    .forEach(Main::downloadFile);
        } catch (Exception ex) {
            LOGGER.error(EXCEPTION_MARKER, (Object) ex);
        }
    }

//    private static void downloadFile(String url) {   //один try()
//
//        checkFoulder(saveTarget);
//
//        try (InputStream inputStream = new URL(url).openStream()) {
//            Files.copy(inputStream, Path.of(
//                    String.valueOf(new File(saveTarget + new File(url).getName()).toPath())),
//                    StandardCopyOption.REPLACE_EXISTING);
//            LOGGER.info(PRINT_MARKER, "Скачен файл : " + saveTarget + new File(url).getName());
//        } catch (Exception ex) {
//            LOGGER.error(EXCEPTION_MARKER, (Object) ex);
//        }
//    }

    public static void checkFoulder(String foulderDir){
        File folder = new File(foulderDir);
        if (!folder.exists()) {
            folder.mkdir();
        }
    }
    private static void downloadFile(String file) {

        checkFoulder(saveTarget);

        try {
            URL url = new URL(file);
            try (InputStream inputStream = url.openStream()) {
                Files.copy(inputStream, Path.of(String.valueOf(
                        new File(saveTarget + new File(file).getName()).toPath())), StandardCopyOption.REPLACE_EXISTING);
                LOGGER.info(PRINT_MARKER, "Скачен файл : " + saveTarget + new File(file).getName());
            }
        } catch (Exception ex) {
            LOGGER.error(EXCEPTION_MARKER, (Object) ex);
        }
    }
}
