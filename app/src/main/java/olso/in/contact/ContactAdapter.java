package olso.in.contact;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import static olso.in.contact.Contact.*;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder>{

    private List<Contact> contactVOList;
    private Context mContext;

    public ContactAdapter(List<Contact> contactVOList, Context mContext){
        this.contactVOList = contactVOList;
        this.mContext = mContext;
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.simple_contact_view, null);
        ContactViewHolder contactViewHolder = new ContactViewHolder(view);
        return contactViewHolder;
    }

    @Override
    public void onBindViewHolder(ContactViewHolder holder, int position) {
        final Contact contactVO = contactVOList.get(position);
        holder.tvContactName.setText(contactVO.getContactName());
        holder.tvPhoneNumber.setText(contactVO.getContactNumber());
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext,ContactDetailActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("name",contactVO.getContactName());
                intent.putExtra("phone",contactVO.getContactNumber());

mContext.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return contactVOList.size();
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder{

        ImageView ivContactImage;
        TextView tvContactName;
        TextView tvPhoneNumber;
        LinearLayout linearLayout;

        public ContactViewHolder(View itemView) {
            super(itemView);
            ivContactImage = (ImageView) itemView.findViewById(R.id.ivContactImage);
            tvContactName = (TextView) itemView.findViewById(R.id.tvContactName);
            tvPhoneNumber = (TextView) itemView.findViewById(R.id.tvPhoneNumber);
            linearLayout=(LinearLayout)itemView.findViewById(R.id.linear);
        }
    }
}

