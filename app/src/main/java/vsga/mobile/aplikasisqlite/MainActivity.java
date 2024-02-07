package vsga.mobile.aplikasisqlite;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import vsga.mobile.aplikasisqlite.adapter.MyAdapter;
import vsga.mobile.aplikasisqlite.helper.DbHelper;
import vsga.mobile.aplikasisqlite.model.Data;

public class MainActivity extends AppCompatActivity {

    //mendeklarasikan variabel
    ListView listView;
    AlertDialog.Builder dialog;
    List<Data> itemList = new ArrayList<>();
    MyAdapter adapter;
    DbHelper SQLite = new DbHelper(this);

    public static final String TAG_ID = "id";
    public static final String TAG_NAME = "name";
    public static final String TAG_ADDRESS = "address";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Tambah SQLite
        SQLite = new DbHelper(getApplicationContext());

        FloatingActionButton fab = findViewById(R.id.fab);

        //Tambah ListView
        listView = findViewById(R.id.list_view);

        fab.setOnClickListener(v -> {
            //Tambah Intent untuk pindah ke halaman add dan edit
            Intent intent = new Intent(MainActivity.this, AddEdit.class);
            startActivity(intent);
        });

        //Tambah adapter dan listview
        adapter = new MyAdapter(MainActivity.this, itemList);
        listView.setAdapter((ListAdapter) adapter);

        //tekan lama untuk menampilkan edit dan hapus
        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            /* Todo Auto-generated method stud */
            final String idx = itemList.get(position).getId();
            final String name = itemList.get(position).getName();
            final String address = itemList.get(position).getAddress();

            final CharSequence[] dialogitem = {"Edit", "Delete"};
            dialog = new AlertDialog.Builder(MainActivity.this);
            dialog.setCancelable(true);
            dialog.setItems(dialogitem, (dialog, which) -> {
                /* TODO Auto-generated method stud */
                switch (which) {
                    case 0:
                        Intent intent = new Intent(MainActivity.this, AddEdit.class);
                        intent.putExtra(TAG_ID, idx);
                        intent.putExtra(TAG_NAME, name);
                        intent.putExtra(TAG_ADDRESS, address);
                        startActivity(intent);
                        break;
                    case 1:
                        SQLite.delete(Integer.parseInt(idx));
                        itemList.clear();
                        getAllData();
                        break;

                }
            }).show();
            return false;
        });
        getAllData();
    }

    //Fungsi ini digunakan untuk mengambil semua data yang ada pada database
    private void getAllData() {
        ArrayList<HashMap<String, String>> row = SQLite.getAllData();

        for (int i = 0; i < row.size(); i++) {
            String id = row.get(i).get(TAG_ID);
            String poster = row.get(i).get(TAG_NAME);
            String title = row.get(i).get(TAG_ADDRESS);

            Data data = new Data();

            data.setId(id);
            data.setName(poster);
            data.setAddress(title);

            itemList.add(data);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        itemList.clear();
        getAllData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
