package in.wingstud.grocitoseller.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.view.View;

import in.wingstud.grocitoseller.R;
import in.wingstud.grocitoseller.databinding.WaitingBinding;

public class Waiting extends AppCompatActivity {

    WaitingBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.waiting);

        binding.toolbar.bellRL.setVisibility(View.GONE);
        binding.toolbar.switchActive.setVisibility(View.GONE);
        binding.toolbar.activityTitle.setText(getResources().getString(R.string.waiting));
        initialize();
    }

    private void initialize() {
        setSupportActionBar( binding.toolbar.toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        binding.toolbar.toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back_white));
        binding.toolbar.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
