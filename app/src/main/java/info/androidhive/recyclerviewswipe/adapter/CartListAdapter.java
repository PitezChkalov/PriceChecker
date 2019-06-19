package info.androidhive.recyclerviewswipe.adapter;

/**
 * Created by ravi on 26/09/17.
 */

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.List;

import info.androidhive.baltsilverapp.R;
import info.androidhive.recyclerviewswipe.MainActivity;
import info.androidhive.recyclerviewswipe.entity.Jewelry;
import info.androidhive.recyclerviewswipe.service.FTPService;


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
        public TextView name, description, price, barcode;
        public ImageView thumbnail;
        public RelativeLayout viewBackground, viewForeground;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            description = view.findViewById(R.id.description);
            price = view.findViewById(R.id.price);
            thumbnail = view.findViewById(R.id.thumbnail);
            barcode = view.findViewById(R.id.barcode);

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
        holder.description.setText(item.getDescription() + " Вес: " + item.getWeight() + " гр.");
        holder.barcode.setText(item.getBarCode());
        String price = item.getCost()*MainActivity.priceMultiply.intValue() + " р";
        if(item.getDiscount() > 0 )
            price += "(скидка "+  (100-((int) Math.round(item.getDiscount()*100))) + "%)";

         holder.price.setText(price);

        holder.bind(position, listener);
        try {
            //new MyTask().execute(new NewType(holder, item));
            Glide.with(context)
                    .load(R.drawable.small_logo)
                    .into(holder.thumbnail);
        }
        catch (Exception e){

        }

    }

    class NewType{
        NewType(MyViewHolder holder, Jewelry item){
            this.holder = holder;
            this.item = item;
        }
        MyViewHolder holder;
        Jewelry item;
    }

    class MyTask extends AsyncTask<NewType, Void, String> {

        MyViewHolder holder;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(NewType... params) {
            this.holder = params[0].holder;
            try {
                 FTPService.downloadAndSaveFile("78.46.228.244", 21, "ftp_baltsilvercom_upl_files",
                        "ftp1btslr1j08c7a6fb4a0","2.jpg", new File(context.getFilesDir(),"2.jpg"));
            return context.getFilesDir()+"2.jpg";
            }
            catch (Exception e){
                return null;
            }
        }

        @Override
        protected void onPostExecute(String filePath) {
            Glide.with(context)
                    .load(filePath)
                    .into(holder.thumbnail);
            super.onPostExecute(filePath);
        }
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
