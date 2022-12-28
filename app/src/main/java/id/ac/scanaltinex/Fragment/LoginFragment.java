package id.ac.scanaltinex.Fragment;

import static androidx.constraintlayout.widget.Constraints.TAG;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.Locale;

import id.ac.scanaltinex.R;
import id.ac.scanaltinex.databinding.FragmentLoginBinding;
import id.ac.scanaltinex.utils.OdooSetup;
import id.ac.scanaltinex.utils.TinyDB;
import oogbox.api.odoo.OdooClient;
import oogbox.api.odoo.OdooUser;
import oogbox.api.odoo.client.AuthError;
import oogbox.api.odoo.client.OdooVersion;
import oogbox.api.odoo.client.helper.OdooErrorException;
import oogbox.api.odoo.client.helper.data.OdooRecord;
import oogbox.api.odoo.client.helper.data.OdooResult;
import oogbox.api.odoo.client.helper.utils.ODomain;
import oogbox.api.odoo.client.helper.utils.OdooFields;
import oogbox.api.odoo.client.listeners.AuthenticateListener;
import oogbox.api.odoo.client.listeners.IOdooResponse;
import oogbox.api.odoo.client.listeners.OdooConnectListener;
import oogbox.api.odoo.client.listeners.OdooErrorListener;


/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;
    private OdooClient client;
    private TinyDB tinyDB;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_login, container, false);
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        tinyDB = new TinyDB(getContext());
        initOdoo();
        return binding.getRoot();
    }

    public void initOdoo() {
        client = new OdooClient.Builder(getContext())
                .setHost(OdooSetup.SERVER)
                .setConnectListener(new OdooConnectListener() {
                    @Override
                    public void onConnected(OdooVersion version) {
                        Log.d(TAG, "onConnected: " + version.toString());
                    }
                })
                .setErrorListener(new OdooErrorListener() {
                    @Override
                    public void onError(OdooErrorException error) {
                        Log.e(TAG, "onError: " + error.toString());
                    }
                }).build();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        hilang,kekanan,zoom,putar
        final Animation animAlpha = AnimationUtils.loadAnimation(getContext(), R.anim.anim_alpha);
        final Animation animTranslate = AnimationUtils.loadAnimation(getContext(), R.anim.anim_translate);
        final Animation animScale = AnimationUtils.loadAnimation(getContext(), R.anim.anim_scale);
        final Animation animRotate = AnimationUtils.loadAnimation(getContext(), R.anim.anim_rotate);
        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(animRotate);
                String user = String.valueOf(binding.edEmail.getText());
                String pw = String.valueOf(binding.edPass.getText());
                processLogin(user, pw);
            }
        });

}
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    public void processLogin(String user, String pw) {


        client.authenticate(user, pw, OdooSetup.DB , new AuthenticateListener() {
            @Override
            public void onLoginSuccess(OdooUser user) {
                tinyDB.putString("session_id", user.sessionId);
                tinyDB.putString("username", user.username);
//                tinyDB.putInt("user_id", user.uid);
                NavHostFragment.findNavController(LoginFragment.this).navigate(R.id.action_signInFragment_to_homeFragment);


//                tinyDB = new TinyDB(getContext());
//                client = new OdooClient.Builder(getContext())
//                        .setHost(OdooSetup.SERVER)
//                        .setSession(tinyDB.getString("session_id"))
//                        .build();
//
//                ODomain domain = new ODomain();
//                OdooFields fields = new OdooFields("id", "workcenter_id");
//                client.searchRead("res.users", domain, fields, 0, 999, "", new IOdooResponse() {
//                    @Override
//                    public void onResult(OdooResult result) {
//                        OdooRecord[] odooRecords = result.getRecords();
//                        for (OdooRecord record : odooRecords) {
//                            int id = record.getInt("id");
//                            Integer workcenterId = record.getInt("workcenter_id");
//                            tinyDB.putInt("workcenter_id", workcenterId);
//
//                            Log.d(TAG, "bisa bro " + record.getInt("workcenter_id"));
//                        }
//                    }
//                });

            }
            @Override
            public void onLoginFail(AuthError error) {
                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }
}