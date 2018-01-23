package it.cnr.iit.contextlabeler.adapters;

import android.content.Context;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import it.cnr.iit.contextlabeler.R;

public class ActivitiesAdapter extends RecyclerView.Adapter<ActivitiesAdapter.MyViewHolder> {

    private List<ActivityElement> activities;
    private Context context;
    private final View.OnClickListener mOnClickListener = new MyOnClickListener();

    private View selectedView = null;

    private int lastPosition = -1;

    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView activityIcon;
        TextView activityLabel;

        MyViewHolder(View view) {
            super(view);
            activityIcon = view.findViewById(R.id.activity_icon);
            activityLabel = view.findViewById(R.id.activity_label);
        }
    }

    public ActivitiesAdapter(Context context, List<ActivityElement> activities) {
        this.context = context;
        this.activities = activities;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_row,
                parent, false);

        itemView.setOnClickListener(mOnClickListener);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ActivityElement element = activities.get(position);
        holder.activityIcon.setImageResource(element.activityIconRes);
        holder.activityLabel.setText(element.activityLabel);
        holder.itemView.setTag(element);

        setLocked(holder.activityIcon);
        setScaleAnimation(holder.itemView);
    }

    @Override
    public int getItemCount() {
        return activities.size();
    }

    class MyOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {

            final ActivityElement element = (ActivityElement) v.getTag();

            if(selectedView != null && ((ActivityElement)selectedView.getTag())
                    .activityLabel.compareTo(element.activityLabel) != 0){
                setViewUnselected(selectedView);
            }

            if(!element.selected) {
                selectedView = v;
                setUnlocked((ImageView) v.findViewById(R.id.activity_icon));

            }else {
                setLocked((ImageView) v.findViewById(R.id.activity_icon));
                selectedView = null;
            }

            element.selected  = !element.selected;
        }
    }

    private void setViewUnselected(View view){
        ActivityElement element = (ActivityElement) view.getTag();
        element.selected = false;
        setLocked((ImageView) view.findViewById(R.id.activity_icon));
    }

    private static void  setLocked(ImageView v) {
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);  //0 means grayscale
        ColorMatrixColorFilter cf = new ColorMatrixColorFilter(matrix);
        v.setColorFilter(cf);
        v.setImageAlpha(128);   // 128 = 0.5
    }

    private static void  setUnlocked(ImageView v) {
        v.setColorFilter(null);
        v.setImageAlpha(255);
    }

    public ActivityElement getSelectedElement(){
        return selectedView != null ? (ActivityElement) selectedView.getTag() : null;
    }

    //==============================================================================================
    // Element Animations
    // https://stackoverflow.com/questions/26724964/how-to-animate-recyclerview-items-when-they-appear
    //==============================================================================================
    private void setFadeAnimation(View view) {
        AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(2000);
        view.startAnimation(anim);
    }

    private void setScaleAnimation(View view) {
        ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        anim.setDuration(500);
        view.startAnimation(anim);
    }
}
