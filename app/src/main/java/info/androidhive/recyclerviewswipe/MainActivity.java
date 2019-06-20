package info.androidhive.recyclerviewswipe;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.springframework.http.HttpStatus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import info.androidhive.baltsilverapp.R;
import info.androidhive.recyclerviewswipe.adapter.CartListAdapter;
import info.androidhive.recyclerviewswipe.adapter.RecyclerItemTouchHelper;
import info.androidhive.recyclerviewswipe.entity.Jewelry;
import info.androidhive.recyclerviewswipe.service.FTPService;
import info.androidhive.recyclerviewswipe.service.IUserService;
import info.androidhive.recyclerviewswipe.service.UserService;
import info.androidhive.recyclerviewswipe.service.UserServiceXML;
import timber.log.Timber;


public class MainActivity extends AppCompatActivity implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private RecyclerView recyclerView;
    private List<Jewelry> cartList;
    private CartListAdapter mAdapter;
    private CoordinatorLayout coordinatorLayout;
    private IUserService userService = new UserService();
    private EditText userInput;
    public static Double priceMultiply = 1.0;
    public Double disc;
    public String user = "";
    final Context context = this;
    ProgressBar progressBar;
    UserServiceXML userServiceXML;
    List<Jewelry> jewelries;
    // url to fetch menu json
    private static final String URL = "https://api.androidhive.info/json/menu.json";
    View promptsView;



    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.INVISIBLE);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LayoutInflater li = LayoutInflater.from(getApplicationContext());
        promptsView = li.inflate(R.layout.prompt, null);
        setUser();
        userServiceXML = MyApplication.getUserServiceXML();
        disc = new Double(0);
        recyclerView = findViewById(R.id.recycler_view);
        coordinatorLayout = findViewById(R.id.coordinator_layout);
        cartList = new ArrayList<>();
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        mAdapter = new CartListAdapter(this, cartList, new CartListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
               setDiscount(position);
            }
        });
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(mAdapter);


        // adding item touch helper
        // only ItemTouchHelper.LEFT added to detect Right to Left swipe
        // if you want both Right -> Left and Left -> Right
        // add pass ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT as param
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);


        ItemTouchHelper.SimpleCallback itemTouchHelperCallback1 = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT ) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                // Row is swiped from recycler view
                // remove it from adapter
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            }
        };

        // attaching the touch helper to recycler view
        new ItemTouchHelper(itemTouchHelperCallback1).attachToRecyclerView(recyclerView);

        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

    }
  public  void setDiscount(final int position){
      LayoutInflater li = LayoutInflater.from(context);
      View qwe = li.inflate(R.layout.prompt, null);
      AlertDialog.Builder builder =
              new AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle);
      builder.setView(qwe);
      userInput = (EditText) qwe.findViewById(R.id.input_text);

                builder.setPositiveButton("Применить",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                //Вводим текст и отображаем в строке ввода на основном экране:
                                try {
                                    disc = (Double.parseDouble(userInput.getText().toString()));
                                          if(disc>0&&disc<100){
                                        mAdapter.setDiscount(position,disc/100);
                                        mAdapter.notifyDataSetChanged();}
                                }
                                catch (NumberFormatException e){
                                    Timber.e("setDiscount "+ e.getMessage());

                                }

                            }
                        })
                        .setNegativeButton("Отмена",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        dialog.cancel();
                                    }
                                });
      AlertDialog alertDialog = builder.create();
      alertDialog.show();

  }

  public void setUser(){
      LayoutInflater li = LayoutInflater.from(context);
      View qwe = li.inflate(R.layout.set_user, null);

      //Вводим текст и отображаем в строке ввода на основном экране:
      Toolbar toolbar = findViewById(R.id.toolbar);
      setSupportActionBar(toolbar);
      //getSupportActionBar().setLogo(R.drawable.logo);
  }
    /**
     * method make volley network call and parses json
     */
    public void prepareCart(View v) {

        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
        integrator.setPrompt("Scan");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(true);
        integrator.setCaptureActivity(info.androidhive.recyclerviewswipe.CaptureActivityPortrait.class);
        integrator.setBarcodeImageEnabled(true);
        integrator.initiateScan();

        }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        progressBar.setVisibility(View.VISIBLE);
        if (resultCode != Activity.RESULT_CANCELED){
            int SDK_INT = android.os.Build.VERSION.SDK_INT;
            if (SDK_INT > 8)
            {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

                IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

                if (result != null && result.getContents().length()> 12) {
                    if (result.getContents() != null) {
                  Timber.i("Scan result: "+result.getContents());
                      Jewelry newJewelry = userServiceXML.findJewelry(result.getContents());
                      if(newJewelry!=null) {
                          cartList.add(0, newJewelry);
                          mAdapter.notifyDataSetChanged();
                      }
                      else {
                          Timber.i("Scan result: Изделие не найдено!");
                          Snackbar snackbar = Snackbar
                              .make(coordinatorLayout, " Изделие не найдено!", Snackbar.LENGTH_SHORT);
                      snackbar.setActionTextColor(Color.YELLOW);
                      snackbar.show();
                  }}
                    else{
                        Timber.i("Scan result: No scan data received!");
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "No scan data received!", Toast.LENGTH_SHORT);
                        toast.show();
                    }

                    progressBar.setVisibility(View.INVISIBLE);

        }}}}


    /**
     * callback when recycler view is swiped
     * item will be removed on swiped
     * undo option will be provided in snackbar to restore the item
     */
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {

        if (direction == ItemTouchHelper.LEFT){
        if (viewHolder instanceof CartListAdapter.MyViewHolder) {
            // get the removed item name to display it in snack bar
            String name = cartList.get(viewHolder.getAdapterPosition()).getArticle();

            // backup of removed item for undo purpose
            final Jewelry deletedItem = cartList.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();

            // remove the item from recycler view
            mAdapter.removeItem(viewHolder.getAdapterPosition());

            // showing snack bar with Undo option
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, name + " Удалён из корзины!", Snackbar.LENGTH_LONG);
            snackbar.setAction("ВЕРНУТЬ", new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // undo is selected, restore the deleted item
                    mAdapter.restoreItem(deletedItem, deletedIndex);
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }}
        else  if(direction == ItemTouchHelper.RIGHT){

            
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }


    public class SendOrder extends AsyncTask<Void, Void, HttpStatus> {
        IUserService userService = new UserService();

        @Override
        protected HttpStatus doInBackground(Void... params) {

            return userService.sendOrder(mAdapter.getCartList(), user, getApplicationContext());
        }
        @Override
        protected void onPostExecute(HttpStatus user) {
            sendComplete(user);
        }
    }

    public void sendComplete(HttpStatus status){
        progressBar.setVisibility(ProgressBar.INVISIBLE);

        if (status == HttpStatus.OK) {
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, " Письмо успешно отправлено!", Snackbar.LENGTH_LONG);
            snackbar.show();

        }
        else {
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "Что-то пошло не так.", Snackbar.LENGTH_LONG);
            snackbar.show();
        }
        final AlertDialog.Builder builder1 =
                new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
        builder1.setTitle("Создать новый заказ?");
        builder1.setPositiveButton("Да", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(context, MainActivity.class);
                startActivity(intent);
            }
        });
        builder1.setNegativeButton("нет", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder1.show();

    }
}
