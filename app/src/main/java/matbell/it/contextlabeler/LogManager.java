package matbell.it.contextlabeler;

import android.content.Context;

import com.snatik.storage.Storage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;

import matbell.it.contextlabeler.adapters.HistoryElement;

public class LogManager {

    static final String BASE_DIR = "ContextLabeler";
    static final String FILE_NAME = "activities.csv";
    public static final String LOG_SEP = "\t";

    static void storeNewActivity(final Context context, final String activityName,
                                 final long start){

        new Thread(new Runnable() {
            @Override
            public void run() {

                Storage storage = new Storage(context.getApplicationContext());
                String basePath = storage.getExternalStorageDirectory() + File.separator + BASE_DIR;

                if(!storage.isDirectoryExists(basePath)) storage.createDirectory(basePath);

                String path = basePath+ File.separator+FILE_NAME;

                long stop = Calendar.getInstance().getTime().getTime();

                String content = start + LOG_SEP + stop + LOG_SEP + activityName;

                if(!storage.isFileExist(path))
                    storage.createFile(path, content+"\n");
                else
                    storage.appendFile(path, content);

            }
        }).start();

    }

    public static void removeActivity(final Context context, final HistoryElement element){

        new Thread(new Runnable() {
            @Override
            public void run() {

                Storage storage = new Storage(context.getApplicationContext());
                String basePath = storage.getExternalStorageDirectory() + File.separator + BASE_DIR;

                String path = basePath+ File.separator+FILE_NAME;
                String tempPath = basePath+ File.separator+"temp.txt";

                File inputFile = new File(path);
                File tempFile = new File(tempPath);

                try{

                    BufferedReader reader = new BufferedReader(new FileReader(inputFile));
                    BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

                    String lineToRemove = element.toString();
                    String currentLine;

                    while((currentLine = reader.readLine()) != null) {
                        // trim newline when comparing with lineToRemove
                        String trimmedLine = currentLine.trim();
                        if(trimmedLine.equals(lineToRemove)) continue;
                        writer.write(currentLine + System.getProperty("line.separator"));
                    }
                    writer.close();
                    reader.close();

                    tempFile.renameTo(inputFile);

                }catch (IOException e){
                    System.out.println(e.toString());
                }

            }
        }).start();
    }

    static void removeLogFile(final Context context){

        new Thread(new Runnable() {
            @Override
            public void run() {

                Storage storage = new Storage(context.getApplicationContext());
                String basePath = storage.getExternalStorageDirectory() + File.separator + BASE_DIR;

                storage.deleteFile(basePath+File.separator+FILE_NAME);

            }
        }).start();

    }
}
