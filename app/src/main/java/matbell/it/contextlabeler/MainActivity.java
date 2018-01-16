package matbell.it.contextlabeler;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Calendar;

import it.matbell.ask.ASK;
import matbell.it.contextlabeler.setup.SetupActivity;

public class MainActivity extends AppCompatActivity {

    private AutoCompleteTextView editText;
    private TextView actionText;
    private Button button;
    private ProgressBar progressBar;
    private ArrayAdapter<String> adapter;
    private ASK ask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.editText);
        actionText = findViewById(R.id.action_text);
        button = findViewById(R.id.control_button);
        progressBar = findViewById(R.id.spin_kit);
    }

    @Override
    protected void onResume() {
        super.onResume();

        ask = new ASK(this, getResources().getString(R.string.ask_conf));

        if(!isSetupComplete()){

            Intent intent = new Intent(this, SetupActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();

        }else {

            if (!PreferencesController.readingIsActive(this)) {
                NotificationController.removeNotification(this);
                stopReadingUI();

            } else {
                startReadingUI();
            }

            adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line,
                    PreferencesController.getActivitiesHistory(this));
            editText.setAdapter(adapter);
            editText.setThreshold(1);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_history:
                startActivity(new Intent(this, HistoryActivity.class));
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    private boolean isSetupComplete(){

        boolean check = false;

        if((getIntent() != null && getIntent().hasExtra(SetupActivity.SETUP_PREF_KEY))
                || PreferencesController.isSetupComplete(this)){
            check = true;
        }

        return check;
    }

    public void onControlClicked(View view){

        if(!PreferencesController.readingIsActive(this) && editText.getText() != null &&
                editText.getText().toString().length() > 0){

            String activity = editText.getText().toString();

            PreferencesController.setNewActivity(this,
                    Calendar.getInstance().getTime().getTime(), activity);
            NotificationController.showNotification(this, activity);

            PreferencesController.setActivityHistory(this, activity);
            adapter.add(activity);

            startReadingUI();

            ask.start();

        }else if(PreferencesController.readingIsActive(this)){

            LogManager.storeNewActivity(this,
                    PreferencesController.getActivityName(this),
                    PreferencesController.getStartReading(this));

            PreferencesController.stopReading(this);
            NotificationController.removeNotification(this);

            stopReadingUI();

            ask.stop();
        }
    }

    private void startReadingUI(){

        String action = getResources().getText(R.string.action_text).toString()
                .concat(" ")
                .concat(PreferencesController.getActivityName(this));

        actionText.setText(action);
        progressBar.setVisibility(View.VISIBLE);
        editText.getText().clear();
        editText.setVisibility(View.INVISIBLE);
        button.setText(getResources().getText(R.string.button_stop));
    }

    private void stopReadingUI(){

        actionText.setText("");
        editText.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
        button.setText(getResources().getText(R.string.button_start));
    }
}
