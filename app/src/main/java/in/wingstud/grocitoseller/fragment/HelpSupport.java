package in.wingstud.grocitoseller.fragment;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import in.wingstud.grocitoseller.Common.Constrants;
import in.wingstud.grocitoseller.Common.SharedPrefManager;
import in.wingstud.grocitoseller.R;
import in.wingstud.grocitoseller.databinding.FragmentHelpSupportBinding;

import static android.Manifest.permission.CALL_PHONE;

/**
 * A simple {@link Fragment} subclass.
 */
public class HelpSupport extends Fragment {


    public HelpSupport() {
        // Required empty public constructor
    }

    private FragmentHelpSupportBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_help_support, container, false);

//        binding.emailTV.setText(SharedPrefManager.getUserEmail(Constrants.UserEmail));
//        binding.mobileTv.setText(SharedPrefManager.getMobile(Constrants.UserMobile));

        binding.emailLL.setOnClickListener(v -> {
            Intent intent=new Intent(Intent.ACTION_SEND);
                String[] recipients={"Grocitoonline@Gmail.Com"};
            intent.putExtra(Intent.EXTRA_EMAIL, recipients);
            intent.putExtra(Intent.EXTRA_SUBJECT,"Subject text here...");
            intent.putExtra(Intent.EXTRA_TEXT,"Body of the content here...");
//            intent.putExtra(Intent.EXTRA_CC,"mailcc@gmail.com");
            intent.setType("text/html");
            intent.setPackage("com.google.android.gm");
            startActivity(Intent.createChooser(intent, "Send mail"));
        });




        binding.mobileLL.setOnClickListener(v -> {
            String number = "9672345662";
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" +number));
            if (ContextCompat.checkSelfPermission(getActivity(), CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                startActivity(intent);
            } else {
                requestPermissions(new String[]{CALL_PHONE}, 1);
            }
//            startActivity(intent);
        });
        return binding.getRoot();
    }

}
