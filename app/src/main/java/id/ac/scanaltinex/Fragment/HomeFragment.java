package id.ac.scanaltinex.Fragment;

import static androidx.constraintlayout.widget.Constraints.TAG;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import id.ac.scanaltinex.R;
import id.ac.scanaltinex.databinding.FragmentHomeBinding;
import id.ac.scanaltinex.utils.TinyDB;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static TinyDB tinyDB;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FragmentHomeBinding binding;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        tinyDB = new TinyDB(getContext());
        if (tinyDB.getString("session_id").isEmpty()){
            NavHostFragment.findNavController(HomeFragment.this)
                    .navigate(R.id.loginFragment);
//            Fragment fragment = getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT);
//            if(fragment != null)
//                getSupportFragmetManager().beginTransaction().remove(fragment).commit();
        }
        deleteLoginFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_home, container, false);
        binding = FragmentHomeBinding.inflate(inflater, container, false);
//        View view = inflater.inflate(R.layout.fragment_home, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //        hilang,kekanan,zoom,putar
        final Animation animAlpha = AnimationUtils.loadAnimation(getContext(), R.anim.anim_alpha);
        final Animation animTranslate = AnimationUtils.loadAnimation(getContext(), R.anim.anim_translate);
        final Animation animScale = AnimationUtils.loadAnimation(getContext(), R.anim.anim_scale);
        final Animation animRotate = AnimationUtils.loadAnimation(getContext(), R.anim.anim_rotate);

        binding.tvUsername.setText(tinyDB.getString("username"));

        binding.icLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(animTranslate);
                tinyDB.clear();
                NavHostFragment.findNavController(HomeFragment.this).navigate(R.id.loginFragment);
            }
        });

        binding.llScanDaily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(animAlpha);
                NavHostFragment.findNavController(HomeFragment.this).navigate(R.id.list_Wo_Daily);
            }
        });
    }

    public void deleteLoginFragment() {
        LoginFragment loginFragment = new LoginFragment();
        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction trans = manager.beginTransaction();
        trans.remove(loginFragment);
        trans.commit();


    }
}