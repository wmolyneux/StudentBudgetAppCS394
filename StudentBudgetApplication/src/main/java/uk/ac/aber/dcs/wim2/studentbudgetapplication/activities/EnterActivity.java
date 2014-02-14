package uk.ac.aber.dcs.wim2.studentbudgetapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import uk.ac.aber.dcs.wim2.studentbudgetapplication.R;

public class EnterActivity extends Activity implements View.OnClickListener{

    Button entryButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter);
        entryButton = (Button)findViewById(R.id.enterButton);
        entryButton.setOnClickListener(this);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.enter, menu);
        return true;
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.enterButton:
                Intent intent = new Intent(this, AccountsActivity.class);
                startActivity(intent);
        }
    }
}
