package matbell.it.contextlabeler;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.snatik.storage.Storage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    private HistoryAdapter historyAdapter;
    private RecyclerView recyclerView;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        this.context = this;

        recyclerView = findViewById(R.id.my_recycler_view);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

    }

    @Override
    protected void onResume() {
        super.onResume();

        new FileReaderTask().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.history_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_clear:

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Do you want to remove all the saved activities?")
                        .setTitle("Clear history")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                LogManager.removeLogFile(context);
                                fillList(new ArrayList<HistoryElement>());
                            }
                        })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {}
                        });
                AlertDialog alert = builder.create();
                alert.show();


                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    private void fillList(List<HistoryElement> history){

        historyAdapter = new HistoryAdapter(this, history);
        recyclerView.setAdapter(historyAdapter);
    }

    private class FileReaderTask extends AsyncTask<Void, Void, List<HistoryElement>>{

        @Override
        protected List<HistoryElement> doInBackground(Void... voids) {

            FileInputStream is;
            BufferedReader reader;

            Storage storage = new Storage(getApplicationContext());
            String path = storage.getExternalStorageDirectory() + File.separator
                    + LogManager.BASE_DIR + File.separator + LogManager.FILE_NAME;

            final File file = new File(path);

            List<HistoryElement> history = new ArrayList<>();

            try{
                if (file.exists()) {

                    is = new FileInputStream(file);
                    reader = new BufferedReader(new InputStreamReader(is));
                    String line = reader.readLine();
                    while(line != null){

                        long start = Long.parseLong(line.split(LogManager.LOG_SEP)[0]);
                        long end = Long.parseLong(line.split(LogManager.LOG_SEP)[1]);
                        String activity = line.split(LogManager.LOG_SEP)[2];

                        history.add(new HistoryElement(activity, start, end));

                        line = reader.readLine();
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            Collections.sort(history, new Comparator<HistoryElement>() {
                @Override
                public int compare(HistoryElement o1, HistoryElement o2) {
                    return o1.start.compareTo(o2.start) * (-1);
                }
            });

            return history;
        }

        @Override
        protected void onPostExecute(List<HistoryElement> history) {
            fillList(history);
        }
    }
}
