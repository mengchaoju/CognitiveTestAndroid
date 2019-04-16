package project.cognitivetest.adapter;

import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

import project.cognitivetest.R;
import project.cognitivetest.history.Activity_history;
import project.cognitivetest.modules.ParticipantBean;

/**
 * Created by 50650 on 2019/4/15
 */
public class HistoryAdaptor extends RecyclerView.Adapter<HistoryAdaptor.PtViewHolder> {

    private final Activity_history mCtx;
    private ArrayList<ParticipantBean.participant> mParticipants;
    private final LayoutInflater mInflater;

    private ArrayList<Integer> mHeights;

    public HistoryAdaptor(Activity_history content, ArrayList<ParticipantBean.participant> ptList){
        mParticipants = ptList;
        mCtx = content;
        mInflater = LayoutInflater.from(mCtx);
    }

    @Override
    public HistoryAdaptor.PtViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclelist_layout,parent,false);
        PtViewHolder viewHolder = new PtViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(PtViewHolder holder, int position) {
        holder.ptIDText.setText(mParticipants.get(position).getParticipantID());
        holder.ptNameText.setText(mParticipants.get(position).getFirstName()+" "+
        mParticipants.get(position).getFamilyName());
        holder.ptGenderText.setText(mParticipants.get(position).getGender());
        holder.ptDoBText.setText(mParticipants.get(position).getDateOfBirth());
    }

    @Override
    public int getItemCount() {
        return mParticipants.size();
    }

    class PtViewHolder extends RecyclerView.ViewHolder{

        TextView ptIDText;
        TextView ptNameText;
        TextView ptGenderText;
        TextView ptDoBText;

        @SuppressLint("WrongViewCast")
        public PtViewHolder(View itemView) {
            super(itemView);

            ptIDText = (EditText)itemView.findViewById(R.id.pt_id_text);
            ptNameText =(EditText)itemView.findViewById(R.id.pt_name_text);
            ptNameText = (EditText)itemView.findViewById(R.id.pt_gender_text);
            ptDoBText =(EditText)itemView.findViewById(R.id.pt_dateofbirth_text);

        }
    }
}
