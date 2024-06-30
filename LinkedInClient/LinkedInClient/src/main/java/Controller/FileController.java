package Controller;

import okhttp3.ResponseBody;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileController {

    private static final int BUFFER_SIZE = 4096;

    public static boolean writeResponseBodyToDisk(ResponseBody body, String filename) {
        try {
            File file = new File("src/main/resources/" + filename);
            InputStream inputStream = null;
            FileOutputStream outputStream = null;

            try {
                byte[] buffer = new byte[BUFFER_SIZE];
                inputStream = body.byteStream();
                outputStream = new FileOutputStream(file);

                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }

                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }
}
