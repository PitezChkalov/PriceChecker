package info.androidhive.recyclerviewswipe.adapter;

/**
 * Created by ravi on 26/09/17.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import info.androidhive.baltsilverapp.R;
import info.androidhive.recyclerviewswipe.entity.Jewelry;


public class CartListAdapter extends RecyclerView.Adapter<CartListAdapter.MyViewHolder> {
    private Context context;
    private List<Jewelry> cartList;
    private OnItemClickListener listener;
    private Double discount;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public List<Jewelry> getCartList() {
        return cartList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, description, price;
        public ImageView thumbnail;
        public RelativeLayout viewBackground, viewForeground;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            description = view.findViewById(R.id.description);
            price = view.findViewById(R.id.price);
            thumbnail = view.findViewById(R.id.thumbnail);
            viewBackground = view.findViewById(R.id.view_background);
            viewForeground = view.findViewById(R.id.view_foreground);
        }

        public void bind(final int position, final OnItemClickListener listener) {
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override public boolean onLongClick(View v) {
                    listener.onItemClick(position);

                    return true;
                }
            });
        }
    }


    public CartListAdapter(Context context, List<Jewelry> cartList, OnItemClickListener listener) {
        this.context = context;
        this.cartList = cartList;
        this.listener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cart_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Jewelry item = cartList.get(position);
        holder.name.setText(item.getArticle());
        holder.description.setText(item.getDescription());
        String price = item.getCost() + " р";
        if(item.getDiscount() > 0 )
            price += "(скидка "+  (100-((int) Math.round(item.getDiscount()*100))) + "%)";

         holder.price.setText(price);

        holder.bind(position, listener);

        Glide.with(context)
                .load("http://www.baltsilver.com/node_model_img?w=300&h=300&mode=scale&zoom=1&file=%D0%BA210038.jpg")
                .into(holder.thumbnail);
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public void removeItem(int position) {
        cartList.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
    }

    public void restoreItem(Jewelry item, int position) {
        cartList.add(position, item);
        // notify item added by position
        notifyItemInserted(position);
    }

    public void setDiscount(int position, double val){
        Integer cost = cartList.get(position).getCost();
        Double s = cost - cost * val;
        cost = s.intValue();
        Double temp = cartList.get(position).getDiscount();
        if(temp!=0)
        cartList.get(position).setDiscount(temp*(1-val));

        else         cartList.get(position).setDiscount(1-val);

        cartList.get(position).setCost(cost);
    }

    public void setGlobalDiscount(double val){
        for(Jewelry j: cartList) {
            setDiscount(cartList.indexOf(j), val);
        }
    }
    public int  getTotalCost(){
        int total = 0;
        for(Jewelry j: cartList) {
            total += j.getCost();
        }
      return   total;
    }
}
