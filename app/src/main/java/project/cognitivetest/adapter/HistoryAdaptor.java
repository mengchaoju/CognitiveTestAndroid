package project.cognitivetest.adapter;

import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import project.cognitivetest.R;
import project.cognitivetest.history.Activity_history;
import project.cognitivetest.modules.Participant;

/**
 * Created by 50650 on 2019/4/15
 */
public class HistoryAdaptor extends RecyclerView.Adapter<HistoryAdaptor.PtViewHolder> {

    private final Activity_history mCtx;
    private ArrayList<Participant> mParticipants;
    private final LayoutInflater mInflater;

    private ArrayList<Integer> mHeights;

    private OnItemClickListener mOnItemClickListener;


    public HistoryAdaptor(Activity_history content, ArrayList<Participant> ptList){
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
    public void onBindViewHolder(final PtViewHolder holder, final int position) {
        holder.ptIDText.setText(mParticipants.get(position).getParticipantID());
        holder.ptNameText.setText(mParticipants.get(position).getFirstName()+" "+
                mParticipants.get(position).getFamilyName());
        String test = mParticipants.get(position).getGender();
        holder.ptGenderText.setText(mParticipants.get(position).getGender());
        holder.ptDoBText.setText(mParticipants.get(position).getDateOfBirth());

        if (mOnItemClickListener != null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnItemClickListener.onItemClick(holder.itemView,position);
                }
            });
        }

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mOnItemClickListener.onItemLongClick(holder.itemView,position);
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mParticipants.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.mOnItemClickListener = listener;
    }


    public interface OnItemClickListener {
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }

    class PtViewHolder extends RecyclerView.ViewHolder{

        TextView ptIDText;
        TextView ptNameText;
        TextView ptGenderText;
        TextView ptDoBText;

        @SuppressLint("WrongViewCast")
        public PtViewHolder(View itemView) {
            super(itemView);

            ptIDText = (TextView)itemView.findViewById(R.id.pt_id_text);
            ptNameText =(TextView)itemView.findViewById(R.id.pt_name_text);
            ptGenderText = (TextView)itemView.findViewById(R.id.pt_gender_text);
            ptDoBText =(TextView)itemView.findViewById(R.id.pt_dateofbirth_text);

        }
    }
}
