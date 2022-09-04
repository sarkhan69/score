import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.Scanner;

public class Try {

    @Test
    void test(){
        Scanner scanner = new Scanner(System.in);
        String path = scanner.next();
        System.out.println(Path.of(path));
    }
}
