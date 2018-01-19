package matbell.it.contextlabeler.setup.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dd.morphingbutton.MorphingButton;

import matbell.it.contextlabeler.R;
import matbell.it.contextlabeler.setup.SetupActivity;
import matbell.it.contextlabeler.setup.Utils;

/**
 * Created by mattia on 18/01/18.
 */
public class SetupAppStatisticsFragment extends Fragment {

    private MorphingButton grantButton;
    public static final int BUTTON_ANIM_DURATION = 500;
    private boolean finish = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.setup_appstats_fragment, container, false);

        grantButton = rootView.findViewById(R.id.grant_permission_button);

        grantButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!finish) {

                    openSettingsActivity();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Utils.enableGreen(getActivity(), grantButton, getContext().getResources()
                                    .getString(R.string.finish));
                            finish = true;
                        }
                    }, BUTTON_ANIM_DURATION);

                }else{
                    ((SetupActivity)getActivity()).onSetupComplete();
                }
            }
        });

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getUserVisibleHint()) {
            Utils.morphToSquare(getActivity(), grantButton, BUTTON_ANIM_DURATION,
                    getContext().getResources().getString(R.string.grant_permissions));
        }
    }

    public MorphingButton getGrantButton(){
        return this.grantButton;
    }

    private void openSettingsActivity(){

        Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
        startActivity(intent);
    }

}
