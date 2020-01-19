package agora.ccna.tstnavigationintent;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class intentAvecResultat extends AppCompatActivity {

    Button bt;
    EditText ed1;
    EditText ed2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intent_avec_resultat);

        ed1 = findViewById(R.id.ednb1);
        ed2 = findViewById(R.id.ednb2);
        bt = findViewById(R.id.btiar);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //création de l'intent de retour
                Intent it = new Intent();
                //calcul du résultat de l'addition
                float resultat = Float.valueOf(ed1.getText().toString()).floatValue()+Float.valueOf(ed2.getText().toString()).floatValue();
                //ajout du résultat dans le bundle associé à l'intent'
                it.putExtra("RESULTAT" , resultat);
                //association intent et le code de retour
                setResult(MainActivity.idIntentRetour_addition , it);
                //on retourne à l'activity appelante
                finish();
            }
        });
    }
}
