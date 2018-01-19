package matbell.it.contextlabeler;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.shashank.sony.fancygifdialoglib.FancyGifDialog;
import com.shashank.sony.fancygifdialoglib.FancyGifDialogListener;

import java.util.Calendar;
import java.util.List;

import it.matbell.ask.ASK;
import matbell.it.contextlabeler.adapters.ActivitiesAdapter;
import matbell.it.contextlabeler.adapters.ActivityElement;
import matbell.it.contextlabeler.controllers.ActivitiesController;
import matbell.it.contextlabeler.setup.SetupActivity;

public class MainActivity extends AppCompatActivity {

    private Button button;
    private ProgressBar progressBar;
    private ASK ask;

    private List<ActivityElement> activities = ActivitiesController.getActivities();
    private ActivitiesAdapter adapter;
    private RecyclerView recyclerView;
    private ImageView currentActivityImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        button = findViewById(R.id.control_button);
        progressBar = findViewById(R.id.spin_kit);
        currentActivityImageView = findViewById(R.id.current_activity_iv);

        // set up the RecyclerView
        recyclerView = findViewById(R.id.activities_recycle_view);
        int numberOfColumns = 4;
        adapter = new ActivitiesAdapter(this, activities);
        recyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
        recyclerView.setAdapter(adapter);
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

    private boolean checkWifiAndBluetooth(){
        WifiManager wifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        return wifi != null && wifi.isWifiEnabled() && mBluetoothAdapter != null &&
                mBluetoothAdapter.isEnabled();
    }

    private void showWifiAndBtDialog(){

        new FancyGifDialog.Builder(this)
                .setTitle("Wi-Fi or Bluetooth disabled?")
                .setMessage("Please, enable both Wi-Fi and Bluetooth to start a new reading.")
                .setPositiveBtnBackground("#966E5C")
                .setPositiveBtnText("Ok")
                .setNegativeBtnText("Cancel")
                .setGifResource(R.drawable.no_wifi)
                .isCancellable(true)
                .OnNegativeClicked(new FancyGifDialogListener() {
                    @Override
                    public void OnClick() {}
                })
                .build();
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

        if(!PreferencesController.readingIsActive(this)){

            if(adapter.getSelectedElement() == null){
                showMissingSelectedActivityDialog();

            }else {

                if (checkWifiAndBluetooth()) {

                    String activity = adapter.getSelectedElement().activityLabel;
                    int imageResource = adapter.getSelectedElement().activityIconRes;

                    PreferencesController.setNewActivity(this,
                        Calendar.getInstance().getTime().getTime(), activity, imageResource);

                    NotificationController.showNotification(this, activity);

                    startReadingUI();

                    ask.start();

                } else
                    showWifiAndBtDialog();
            }

        }else{

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

        NotificationController.showNotification(this,
                PreferencesController.getActivityName(this));

        recyclerView.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        if(PreferencesController.getActivityResourceIcon(this) != -1){
            currentActivityImageView.setImageResource(PreferencesController.getActivityResourceIcon(
                    this));
            currentActivityImageView.setVisibility(View.VISIBLE);
        }

        button.setText(getResources().getText(R.string.button_stop));
    }

    private void stopReadingUI(){

        progressBar.setVisibility(View.INVISIBLE);
        currentActivityImageView.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
        button.setText(getResources().getText(R.string.button_start));
    }

    private void showMissingSelectedActivityDialog(){
        new FancyGifDialog.Builder(this)
                .setTitle("What are you doing now?")
                .setMessage("Please, select your current activity to start a new reading.")
                .setPositiveBtnBackground("#75ceab")
                .setPositiveBtnText("Ok")
                .setNegativeBtnText("Cancel")
                .setGifResource(R.drawable.pandas)
                .isCancellable(true)
                .OnNegativeClicked(new FancyGifDialogListener() {
                    @Override
                    public void OnClick() {}
                })
                .build();
    }
}
