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
 * Created by 50650 on 2019/4/16
 */
public class ParticipantStaggeredAdapter extends RecyclerView.Adapter<ParticipantStaggeredAdapter.MyViewHolder> {
    private final ArrayList<ParticipantBean.participant> mParticipants;
    private final LayoutInflater mInflater;
    private final Activity_history mContent;

    private ArrayList<Integer> mHerghts;

    public ParticipantStaggeredAdapter(ArrayList<ParticipantBean.participant> results,
                                       Activity_history content){
        mParticipants=results;
        mContent =content;
        mInflater = LayoutInflater.from(mContent);

        mHerghts = new ArrayList<>();
        for (int i =0; i<mParticipants.size();i++){
            mHerghts.add((int) (100 + Math.random() * 300));
        }

        }

    @Override
    public ParticipantStaggeredAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclelist_layout,
                parent,false);
        MyViewHolder viewHolder = new MyViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ViewGroup.LayoutParams layoutParams =
                holder.itemView.getLayoutParams();
        layoutParams.height=mHerghts.get(position);
        holder.itemView.setLayoutParams(layoutParams);

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

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView ptIDText;
        TextView ptNameText;
        TextView ptGenderText;
        TextView ptDoBText;

        @SuppressLint("WrongViewCast")
        public MyViewHolder(View itemView) {
            super(itemView);

            ptIDText = (EditText)itemView.findViewById(R.id.pt_id_text);
            ptNameText =(EditText)itemView.findViewById(R.id.pt_name_text);
            ptNameText = (EditText)itemView.findViewById(R.id.pt_gender_text);
            ptDoBText =(EditText)itemView.findViewById(R.id.pt_dateofbirth_text);

        }

}}
