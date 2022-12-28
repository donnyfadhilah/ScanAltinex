package id.ac.scanaltinex.Fragment;

import static androidx.constraintlayout.widget.Constraints.TAG;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;

import java.util.ArrayList;
import java.util.Calendar;

import id.ac.scanaltinex.Adapter.ListWoDailyAdapter;
import id.ac.scanaltinex.Models.ListWoDailyModel;
import id.ac.scanaltinex.R;
import id.ac.scanaltinex.databinding.FragmentHomeBinding;
import id.ac.scanaltinex.databinding.FragmentListWoDailyBinding;
import id.ac.scanaltinex.utils.OdooSetup;
import id.ac.scanaltinex.utils.TinyDB;
import oogbox.api.odoo.OdooClient;
import oogbox.api.odoo.client.helper.data.OdooRecord;
import oogbox.api.odoo.client.helper.data.OdooResult;
import oogbox.api.odoo.client.helper.utils.OArguments;
import oogbox.api.odoo.client.helper.utils.ODomain;
import oogbox.api.odoo.client.helper.utils.OdooFields;
import oogbox.api.odoo.client.listeners.IOdooResponse;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link List_Wo_Daily#newInstance} factory method to
 * create an instance of this fragment.
 */
public class List_Wo_Daily extends Fragment {

    public ArrayList<ListWoDailyModel> ListWoDailyModelArrayList = new ArrayList<ListWoDailyModel>();
    private ArrayList<String> listoperator = new ArrayList<>();

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
    private FragmentListWoDailyBinding binding;
    private IntentIntegrator integrator;

    public List_Wo_Daily() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment List_Wo_Daily.
     */
    // TODO: Rename and change types and number of parameters
    public static List_Wo_Daily newInstance(String param1, String param2) {
        List_Wo_Daily fragment = new List_Wo_Daily();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_list__wo__daily, container, false);
        binding = FragmentListWoDailyBinding.inflate(inflater, container, false);
        getManufacturingOrder(inflater);
//        getMachine();
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //        hilang,kekanan,zoom,putar
        final Animation animAlpha = AnimationUtils.loadAnimation(getContext(), R.anim.anim_alpha);
        final Animation animTranslate = AnimationUtils.loadAnimation(getContext(), R.anim.anim_translate);
        final Animation animScale = AnimationUtils.loadAnimation(getContext(), R.anim.anim_scale);
        final Animation animRotate = AnimationUtils.loadAnimation(getContext(), R.anim.anim_rotate);

        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(animAlpha);
                NavHostFragment.findNavController(List_Wo_Daily.this).navigate(R.id.homeFragment);
            }
        });

        binding.btnTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(animAlpha);
                Log.d(TAG, "onClick: ");
                integrator = new IntentIntegrator(getActivity()).forSupportFragment(List_Wo_Daily.this);
                integrator.initiateScan();
            }
        });


//        binding.btnTambah.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                final Dialog dialog = new Dialog(getContext());
//                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
//                dialog.setContentView(R.layout.form_tambah);
//                dialog.setCancelable(true);
//
//                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
//                lp.copyFrom(dialog.getWindow().getAttributes());
//                lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
//                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
//
//                final Calendar calendar = Calendar.getInstance();
//                final int year = calendar.get(calendar.YEAR);
//                final int month = calendar.get(calendar.MONTH);
//                final int day = calendar.get(calendar.DAY_OF_MONTH);
//
//                final EditText mDate = (EditText) dialog.findViewById(R.id.date);
//                final AutoCompleteTextView mOperator = (AutoCompleteTextView) dialog.findViewById(R.id.operator);
//                final Button mSave = (Button) dialog.findViewById(R.id.btn_save);
//
//                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, listoperator);
//                mOperator.setAdapter(adapter);
//
//                mDate.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        DatePickerDialog dialog = new DatePickerDialog(getLayoutInflater().getContext(), new DatePickerDialog.OnDateSetListener() {
//                            @Override
//                            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
//                                month = month+1;
//                                String date = day+"/"+month+"/"+year;
//                                mDate.setText(date);
//                            }
//                        },year,month,day);
//                        dialog.show();
//                    }
//                });
//                mSave.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
////
////                        Toast.makeText(getContext(),"Berhasil Save",Toast.LENGTH_SHORT).show();
////                        dialog.dismiss();
//
//                        OArguments arguments = new OArguments();
//                        arguments.add(20);
//                        client.call_kw("mrp.workorder", "button_start", arguments, new IOdooResponse() {
//                            @Override
//                            public void onResult(OdooResult result) {
//                                Log.e(TAG, "onResult: jalan coy" + result);
//                            }
//                        });
//                    }
//                });
//                dialog.show();
//            }
//        });
    }

//    public void getMachine(){
//        tinyDB = new TinyDB(getContext());
//        client = new OdooClient.Builder(getContext())
//                .setHost(OdooSetup.SERVER)
//                .setSession(tinyDB.getString("session_id"))
//                .build();
//
//
//        Log.d(TAG, "getManufacturingOrder: ");
//        ODomain domain = new ODomain();
//        OdooFields fields = new OdooFields("id", "name");
//        client.searchRead("hr.employee", domain, fields, 0, 999, "", new IOdooResponse() {
//            @Override
//            public void onResult(OdooResult result) {
//                OdooRecord[] odooRecords = result.getRecords();
//                for (OdooRecord record : odooRecords) {
//                    int id = record.getInt("id");
//                    String name = record.getString("name");
//                    listoperator.add(name);
//                }
//            }
//        });
//    }

    public void getManufacturingOrder(LayoutInflater inflater) {
        tinyDB = new TinyDB(getContext());
        client = new OdooClient.Builder(getContext())
                .setHost(OdooSetup.SERVER)
                .setSession(tinyDB.getString("session_id"))
                .build();


        Log.d(TAG, "getManufacturingOrder: ");
        ListWoDailyModelArrayList.clear();
        ODomain domain = new ODomain();
        list = binding.listView;
        OdooFields fields = new OdooFields("id", "name", "date", "employee_id");
        client.searchRead("workorder.daily", domain, fields, 0, 999, "", new IOdooResponse() {
            @Override
            public void onResult(OdooResult result) {
                OdooRecord[] odooRecords = result.getRecords();
                for (OdooRecord record : odooRecords) {
                    int id = record.getInt("id");
                    String name = record.getString("name");
                    String date = record.getString("date");
                    String employeeId = record.getString("employee_id");


                    ListWoDailyModel listWoDailyModel = new ListWoDailyModel();
                    listWoDailyModel.OpnameBarcpdeModel(id, name, date, employeeId);
                    ListWoDailyModelArrayList.add(listWoDailyModel);
                    ListWoDailyAdapter listWoDailyAdapter = new ListWoDailyAdapter();
                    listWoDailyAdapter.ListWoDailyAdapter(ListWoDailyModelArrayList, inflater);
                    list.setAdapter(listWoDailyAdapter);

                }
            }
        });
        binding.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Bundle bundle = new Bundle();
                bundle.putInt("wo_daily_id", ListWoDailyModelArrayList.get(i).getId());
                bundle.putString("name",ListWoDailyModelArrayList.get(i).getName());
                bundle.putString("date", ListWoDailyModelArrayList.get(i).getDate());
                bundle.putString("employee_id", ListWoDailyModelArrayList.get(i).getEmployeeId());
                NavHostFragment.findNavController(List_Wo_Daily.this).navigate(R.id.wo_Daily_Detail, bundle);
                setArguments(bundle);
            }

        });

    }
}