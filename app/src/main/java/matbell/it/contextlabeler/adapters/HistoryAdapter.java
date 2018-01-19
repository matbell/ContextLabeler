package matbell.it.contextlabeler.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import matbell.it.contextlabeler.LogManager;
import matbell.it.contextlabeler.R;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MyViewHolder> {

    private List<HistoryElement> history;
    private Context context;
    private final View.OnClickListener mOnClickListener = new MyOnClickListener();

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView duration, activity, start;

        MyViewHolder(View view) {
            super(view);
            duration = view.findViewById(R.id.duration);
            activity = view.findViewById(R.id.activity);
            start = view.findViewById(R.id.start);;
        }
    }


    public HistoryAdapter(Context context, List<HistoryElement> history) {
        this.context = context;
        this.history = history;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_row,
                parent, false);

        itemView.setOnClickListener(mOnClickListener);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        HistoryElement element = history.get(position);
        holder.duration.setText(element.duration);
        holder.activity.setText(element.activityName);
        holder.start.setText(element.start);
        holder.itemView.setTag(element);
    }

    @Override
    public int getItemCount() {
        return history.size();
    }

    class MyOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {

            final HistoryElement element = (HistoryElement) v.getTag();
            final int position = history.indexOf(element);

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage("Do you want to remove this element?")
                    .setTitle("Remove "+element.activityName)
                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            history.remove(position);
                            notifyItemRemoved(position);
                            LogManager.removeActivity(context, element);
                        }
                    })
                    .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {}
                    });
            AlertDialog alert = builder.create();
            alert.show();

        }
    }
}
