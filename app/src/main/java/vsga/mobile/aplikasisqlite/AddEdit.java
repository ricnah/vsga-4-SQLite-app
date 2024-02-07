package vsga.mobile.aplikasisqlite;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import vsga.mobile.aplikasisqlite.helper.DbHelper;

public class AddEdit extends AppCompatActivity {
    //Deklarasikan variabel
    EditText txt_id, txt_name, txt_address;
    Button btn_submit, btn_cancel;
    DbHelper SQLite = new DbHelper(this);
    String id, name, address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //mengambil value berdasarkan id
        txt_id = findViewById(R.id.txt_id);
        txt_name = findViewById(R.id.txt_name);
        txt_address = findViewById(R.id.txt_address);
        btn_submit = findViewById(R.id.btnSubmit);
        btn_cancel = findViewById(R.id.btnCancel);

        //menampilkan value yang diinput
        id = getIntent().getStringExtra(MainActivity.TAG_ID);
        name = getIntent().getStringExtra(MainActivity.TAG_NAME);
        address = getIntent().getStringExtra(MainActivity.TAG_ADDRESS);

        //membuat kondisi untuk add data dan edit
        if (id == null || id == ""){
            setTitle("Add Data");
        } else {
            setTitle("Edit Data");
            txt_id.setText(id);
            txt_name.setText(name);
            txt_address.setText(address);
        }

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (txt_id.getText().toString().equals("")){
                        save();
                    } else {
                        edit();
                    }
                } catch (Exception e){
                    Log.e("Submit", e.toString());
                }
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                blank();
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    //memilih item
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                blank();
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //mengosongkan value pada form
    private void blank(){
        txt_name.requestFocus();
        txt_id.setText(null);
        txt_name.setText(null);
        txt_address.setText(null);
    }

    //menyimpan data ke database
    private void save(){
        if (String.valueOf(txt_name.getText()).equals(null) || String.valueOf(txt_name.getText()).equals("") || String.valueOf(txt_address.getText()).equals(null) || String.valueOf(txt_address.getText()).equals("")) {
            Toast.makeText(getApplicationContext(),
                    "Please input name or address..", Toast.LENGTH_SHORT).show();
        } else {
            SQLite.insert(txt_name.getText().toString().trim(), txt_address.getText().toString().trim());
            blank();
            finish();
        }
    }

    //update data kedalam database
    private void edit(){
        if ((String.valueOf(txt_name.getText()).equals(null) || String.valueOf(txt_name.getText()).equals("")) || String.valueOf(txt_address.getText()).equals(null) || String.valueOf(txt_address.getText()).equals("")) {
            Toast.makeText(getApplicationContext(),
                    "Please input name or address..", Toast.LENGTH_SHORT).show();
        } else {
            SQLite.update(Integer.parseInt(txt_id.getText().toString().trim()), txt_name.getText().toString().trim(), txt_address.getText().toString().trim());
            blank();
            finish();
        }
    }
}
