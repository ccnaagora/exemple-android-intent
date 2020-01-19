package agora.ccna.tstnavigationintent;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.documentfile.provider.DocumentFile;

import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //membres pour transmettre un texte vers l'intent sans resultat
    Button bt1;
    EditText ed1;
    //membres pour l'intent avec résultat : addition de 2 nombres
    public static int idIntentRetour_addition = 200;//peu importe, c'est pour identifier l'intent qui retourne un résultat
    public static int idIntentRetour_pickFichier = 250;//peu importe, c'est pour identifier l'intent qui retourne un résultat

    Button bt2;
    TextView tv2;
    //membres pour l'intent implicite
    Button bt3;
    Spinner spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        //traitement pour déclencher l'intent sans résultat
        bt1 = findViewById(R.id.bt1);
        ed1 = findViewById(R.id.ed1);
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //création de l'intention
                Intent it = new Intent(getApplicationContext() , intentSansREsultat.class);
                //ajout du texte à transmettre
                it.putExtra("TEXTE" , ed1.getText().toString());
                //démarrage
                startActivity(it);
            }
        });
        //traitement de l'intent avec résultat
        bt2 = findViewById(R.id.bt2);
        tv2 = findViewById(R.id.tv2);
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getApplicationContext() , intentAvecResultat.class);
                startActivityForResult(it , MainActivity.idIntentRetour_addition);
            }
        });
        //code de gestion de l'intent implicite
        ArrayList<String> lst = new ArrayList<String>();
        lst.add("Recherche sur le web");
        lst.add("recherche sur le smartphone");
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, lst);
        spinner = findViewById(R.id.spinner);
        spinner.setAdapter(adapter);
        bt3 = findViewById(R.id.bt3);
        /*
        déclenchement de 2 intent possible:
                recherche sur le web => android démarre ou propose de choisir un navigateur
         */
        bt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int p = spinner.getSelectedItemPosition();
                //intent fonction du choix dans le spinner
                Intent it = new Intent();
                switch (p){
                    //action : recherche sur le web
                    case 0 :    it.setAction(Intent.ACTION_WEB_SEARCH);
                                //on démarre l'intent, pas de résultat attendu
                                startActivity(it);
                                break;
                    //action : picker un fichier via un explorateur android
                    case 1 :    it.setAction(Intent.ACTION_GET_CONTENT);
                                //on définit le chemain de recherche (external storage ), nécessite les autorisations dans le manifeste
                                Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getPath());
                                it.setData(uri);
                                it.setType("*/*");//   */*
                                it.addCategory(Intent.CATEGORY_OPENABLE);
                                Intent i = Intent.createChooser(it, "ChooseFile");//File
                                //déclenchement de l'intent, on attend en résultat un nom de fichier
                                startActivityForResult(i , MainActivity.idIntentRetour_pickFichier);
                                break;
                    default : break;
                }
            }
        });
    }
    //gestionnaire de l'événement déclenché lorsqu'une activité retourne un résultat.
    @Override
    public void onActivityResult(int  idItRetour, int codeRetour, Intent itretour){
        super.onActivityResult(idItRetour , codeRetour , itretour);
        //si l'identifiant de l'intent est celui de l'addition
        //  on affiche le texte transmis par l'intent (résultat de l'addition)
        if(idItRetour==MainActivity.idIntentRetour_addition){
            tv2.setText("Résultat = " + String.valueOf(itretour.getFloatExtra("RESULTAT",-1)));
        }
        //si l'identifiant est celui de l'intent implicite "explorateur de fichier"
        //  on crée, on ouvre, on lit et on affiche le flux associé au fichier retourné
        if(idItRetour==MainActivity.idIntentRetour_pickFichier) {
            //on extrait l'uri du fichier
            Uri uri = itretour.getData();
            Log.i("INTENT", "Retour intent  : " + codeRetour + "    idretour:" + idItRetour);
            Log.i("INTENT", "Retour intent uri: " + uri);
            try {
                //on ouvre et on lit le fichier correspondant à l'uri retourné dans l'intent implicite
                DocumentFile f = DocumentFile.fromFile(new File(uri.getPath()));
                Log.i("INTENT", "Retour type uri: " + uri.getScheme());
                InputStream in =   getApplicationContext().getContentResolver().openInputStream(uri);
                byte[] b = new byte[1024];
                in.read(b);
                b[100] = 0;
                String txt = new String(b);
                Log.i("INTENT", "Retour text : " + txt);
                //FileReader in = new FileReader(f.getName());
            } catch ( FileNotFoundException e) {
                Log.i("INTENT", "Exception file open: " + e.getMessage());
            }catch (IOException  e) {
                Log.i("INTENT", "Exception IO: " + e.getMessage());
            }

        }
    }

    //******************************************************
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
