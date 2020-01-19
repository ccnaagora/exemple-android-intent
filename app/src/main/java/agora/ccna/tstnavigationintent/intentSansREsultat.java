package agora.ccna.tstnavigationintent;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class intentSansREsultat extends AppCompatActivity {

    Button btisr;
    TextView tvisr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intent_sans_resultat);
        Intent it = getIntent();
        String texte = it.getStringExtra("TEXTE");
        tvisr = findViewById(R.id.tvisr);
        tvisr.setText(texte);
        //gestion du bouton de retour
        btisr = findViewById(R.id.btisr);
        btisr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //retour à l'activity appelante
                finish();
            }
        });
    }
}
