package id.ac.scanaltinex.Fragment;

import static android.view.View.inflate;
import static androidx.constraintlayout.widget.Constraints.TAG;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.security.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import id.ac.scanaltinex.Adapter.ListWoDailyAdapter;
import id.ac.scanaltinex.Models.ListWoDailyModel;
import id.ac.scanaltinex.R;
import id.ac.scanaltinex.databinding.FragmentWoDailyDetailBinding;
import id.ac.scanaltinex.utils.OdooSetup;
import id.ac.scanaltinex.utils.TinyDB;
import oogbox.api.odoo.OdooClient;
import oogbox.api.odoo.client.OdooVersion;
import oogbox.api.odoo.client.helper.OdooErrorException;
import oogbox.api.odoo.client.helper.data.OdooRecord;
import oogbox.api.odoo.client.helper.data.OdooResult;
import oogbox.api.odoo.client.helper.utils.OArguments;
import oogbox.api.odoo.client.helper.utils.ODomain;
import oogbox.api.odoo.client.helper.utils.OdooFields;
import oogbox.api.odoo.client.listeners.IOdooResponse;
import oogbox.api.odoo.client.listeners.OdooConnectListener;
import oogbox.api.odoo.client.listeners.OdooErrorListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Wo_Daily_Detail#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Wo_Daily_Detail extends Fragment implements AdapterView.OnItemSelectedListener{

    private static final String[] Date = new String[] {"date 1", "date 2", "date 3"};
    private ArrayList<String> listMachine = new ArrayList<>();
    private static final String[] Shift = new String[] {"", "Shift 1", "Shift 2", "Shift 3"};
    private ArrayList<String> addArray = new ArrayList<>();


    TextView nameText,dateText, employee_idText;
    String name,date, employee_id;
    int woDailyId;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static TinyDB tinyDB;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OdooClient client;
    ListView list;
    private FragmentWoDailyDetailBinding binding;
    private IntentIntegrator integrator;

    public Wo_Daily_Detail() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Wo_Daily_Detail.
     */
    // TODO: Rename and change types and number of parameters
    public static Wo_Daily_Detail newInstance(String param1, String param2) {
        Wo_Daily_Detail fragment = new Wo_Daily_Detail();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        initOdoo();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentWoDailyDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //        hilang,kekanan,zoom,putar
        final Animation animAlpha = AnimationUtils.loadAnimation(getContext(), R.anim.anim_alpha);
        final Animation animTranslate = AnimationUtils.loadAnimation(getContext(), R.anim.anim_translate);
        final Animation animScale = AnimationUtils.loadAnimation(getContext(), R.anim.anim_scale);
        final Animation animRotate = AnimationUtils.loadAnimation(getContext(), R.anim.anim_rotate);

        nameText = view.findViewById(R.id.nameDetail);
        dateText = view.findViewById(R.id.tp_label_date);
        employee_idText = view.findViewById(R.id.tp_label_employee);

        name = getArguments().getString("name");
        date = getArguments().getString("date");
        employee_id= getArguments().getString("employee_id");
        woDailyId = getArguments().getInt("wo_daily_id");

        nameText.setText(name);
        dateText.setText(date);
        employee_idText.setText(employee_id);

        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(animAlpha);
                NavHostFragment.findNavController(Wo_Daily_Detail.this).navigate(R.id.list_Wo_Daily);
            }
        });


        binding.btnScan.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            view.startAnimation(animAlpha);
            Log.d(TAG, "onClick: ");
            integrator = new IntentIntegrator(getActivity()).forSupportFragment(Wo_Daily_Detail.this);
            integrator.initiateScan();
        }
    });
}
    public void initOdoo() {
        tinyDB = new TinyDB(getContext());
        Log.d(TAG, "initOdoo: session_id" + tinyDB.getString("session_id"));
        client = new OdooClient.Builder(getContext())
                .setHost(OdooSetup.SERVER)
                .setSession(tinyDB.getString("session_id"))
                .setConnectListener(new OdooConnectListener() {
                    @Override
                    public void onConnected(OdooVersion version) {
                        Log.d(TAG, "onConnected: " + version.toString());
                        getMachine();
                    }
                })
                .setErrorListener(new OdooErrorListener() {
                    @Override
                    public void onError(OdooErrorException error) {
                        Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show();
                    }
                })
                .build();
    }

    public void getMachine(){
        tinyDB = new TinyDB(getContext());
        client = new OdooClient.Builder(getContext())
                .setHost(OdooSetup.SERVER)
                .setSession(tinyDB.getString("session_id"))
                .build();

        Log.d(TAG, "getManufacturingOrder: ");
        ODomain domain = new ODomain();
        OdooFields fields = new OdooFields("id", "name");
        client.searchRead("mrp.machine", domain, fields, 0, 999, "", new IOdooResponse() {
            @Override
            public void onResult(OdooResult result) {
                OdooRecord[] odooRecords = result.getRecords();
                for (OdooRecord record : odooRecords) {
                    int id = record.getInt("id");
                    String name = record.getString("name");
                    listMachine.add(name);
                }
            }
        });
    }

    public void onScanBarcode(String barcode) {
        tinyDB = new TinyDB(getContext());
        client = new OdooClient.Builder(getContext())
                .setHost(OdooSetup.SERVER)
                .setSession(tinyDB.getString("session_id"))
                .build();

        OArguments arguments = new OArguments();
        arguments.add(barcode);
        client.call_kw("workorder.daily", "scan_is_start", arguments, new IOdooResponse() {
            @Override
            public void onResult(OdooResult result) {
                Boolean isStart = result.getBoolean("result");
                final Dialog dialog = new Dialog(getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
// before
                if (isStart) {
//                    Toast.makeText(getContext(),"START", Toast.LENGTH_LONG).show();
                    dialog.setContentView(R.layout.dialog_start);
                    dialog.setCancelable(true);

                    WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                    lp.copyFrom(dialog.getWindow().getAttributes());
                    lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
                    lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

                    Calendar calendar = Calendar.getInstance();
                    String date = DateFormat.getDateInstance(android.icu.text.DateFormat.FULL).format(calendar.getTime());
                    String timeStamp = new SimpleDateFormat("yyyy:MM:dd:HH:mm:ss").format(new java.util.Date());

//                    TextView dateTime = dialog.findViewById(R.id.date_time);
                    TextView clock = dialog.findViewById(R.id.clock);
                    clock.setText(timeStamp);
//                    dateTime.setText(date);

                    (dialog.findViewById(R.id.btn_oke)).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                    dialog.getWindow().setAttributes(lp);

                } else {
                    dialog.setContentView(R.layout.dialog_scan);
                    dialog.setCancelable(true);

                    WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                    lp.copyFrom(dialog.getWindow().getAttributes());
                    lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
                    lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

                    final AutoCompleteTextView machine = (AutoCompleteTextView) dialog.findViewById(R.id.tv_machine);
                    final TextView et_title_dialog = (TextView) dialog.findViewById(R.id.et_title_dialog);
                    final EditText qty = (EditText) dialog.findViewById(R.id.qty);
                    final Spinner spinner = (Spinner) dialog.findViewById(R.id.add_defect);
                    final Button btnSave = (Button) dialog.findViewById(R.id.btn_save1);
                    final ListView listView1 = (ListView) dialog.findViewById(R.id.listView1);
                    final ListView listView2 = (ListView) dialog.findViewById(R.id.listView2);
                    final Button btnSubmit = (Button) dialog.findViewById(R.id.bt_submit);
                    Spinner spin = (Spinner) dialog.findViewById(R.id.simpleSpinner);

//                    spin.setOnItemSelectedListener(this);
                    ArrayAdapter aa = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_item,Shift);
                    aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spin.setAdapter(aa);

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, listMachine);
                    machine.setAdapter(adapter);
                    et_title_dialog.setText(barcode);

                    ArrayAdapter defect = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_item,Shift);
                    spinner.setAdapter(defect);

//        btnSave.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String getInput = add_qty.getText().toString();
//                listView1.setAdapter(defect);
//                if (addArray.contains(getInput)) {
//                    Toast.makeText(getContext(), "Item Already", Toast.LENGTH_LONG).show();
//                } else if (getInput.trim().equals("")){
//                    Toast.makeText(getContext(), "Input field is empty", Toast.LENGTH_LONG).show();
//                } else {
//                    addArray.add(getInput);
//                    ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1,addArray);
//                    listView2.setAdapter(adapter1);
//                    ((EditText) dialog.findViewById(R.id.add_qty)).setText("");
//                }
//            }
//        });
                    (dialog.findViewById(R.id.bt_cancel)).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    btnSubmit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            OArguments arguments = new OArguments();
                            arguments.add(et_title_dialog.getText());
                            arguments.add(machine.getText());
                            arguments.add(spin.getSelectedItem());
                            arguments.add(qty.getText());
                            arguments.add(woDailyId);

                            client.call_kw("workorder.daily", "input_wo_daily", arguments, new IOdooResponse() {
                                @Override
                                public void onResult(OdooResult result) {
                                    Log.d(TAG, "onResult: " + result);
                                }
                            });
                            dialog.dismiss();
                            String msg = result.getString("result");
                            Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();
                        }
                    });
                    dialog.show();
                    dialog.getWindow().setAttributes(lp);
                }
            }
        });
    }

    //Performing action onItemSelected and onNothing selected
    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position,long id) {
        Toast.makeText(getContext(), Shift[position], Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
// TODO Auto-generated method stub

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (intentResult != null) {
            if (intentResult.getContents() == null) {
                Log.d("Scanner", "Cancelled");
                Toast.makeText(getContext(), "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                Log.d(TAG, "onActivityResult: " + intentResult.getContents());
                onScanBarcode(intentResult.getContents());
            }
        }
    }

}