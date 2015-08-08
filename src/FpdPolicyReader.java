import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FpdPolicyReader {

    private static byte[] data;
    static {
        try {
            Path path = Paths.get(ClassLoader.getSystemResource("policy.xml").toURI());
            data = Files.readAllBytes(path);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Couldn't read policy file.");
        }
    }

    public static byte[] getPolicy() {
        return data;
    }

}
