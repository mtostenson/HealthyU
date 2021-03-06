package com.exlhealthcare.healthyu.fragments;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.exlhealthcare.healthyu.R;
import com.exlhealthcare.healthyu.adapters.BaseListAdapter;
import com.exlheathcare.healthyu.api.ApiCall;
import com.exlheathcare.healthyu.api.ApiCall.ApiCaller;

public class ProgramListFragment extends ListFragment implements ApiCaller {

    private ListView programList;
    private BaseListAdapter programListAdapter;
    private ProgramListInterface programListInterface;
    private JSONArray programs;    

    public ProgramListFragment(JSONArray programs) {
        this.programs = programs;
    }

    @Override
    public View onCreateView(LayoutInflater pInflater, ViewGroup pContainer,
        Bundle pSavedInstanceState) {
        View rootView = pInflater.inflate(R.layout.base_list_fragment,
            pContainer, false);
        programList = (ListView) rootView.findViewById(android.R.id.list);
        String[] programNames = new String[programs.length()];
        for (int i = 0; i < programNames.length; i++) {
            try {
                programNames[i] = programs.getJSONObject(i)
                    .getJSONObject("program").getString("description");
            } catch (JSONException pException) {
                pException.printStackTrace();
            }
        }
        programListAdapter = new BaseListAdapter(getActivity()
            .getApplicationContext(), programNames);
        programList.setAdapter(programListAdapter);
        return rootView;
    }

    @Override
    public void onListItemClick(ListView pL, View pV, int pPosition, long pId) {
        super.onListItemClick(pL, pV, pPosition, pId);
        String req = "";
        try {
            req = getString(R.string.rest_url) + "CarePlans/"
                + programs.getJSONObject(pPosition).getString("internalId");
        } catch (JSONException pException) {
            pException.printStackTrace();
        }
        new ApiCall(this).execute(req);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((ActionBarActivity) getActivity()).getSupportActionBar().setTitle(
            R.string.label_programs);
    }

    public void setProgramListInterface(
        ProgramListInterface programListInterface) {
        this.programListInterface = programListInterface;
    }

    @Override
    public void apply(JSONArray carePlans) {
        programListInterface.onSelectProgram(carePlans);
    }
    
    public interface ProgramListInterface {
        public void onSelectProgram(JSONArray carePlans);
    }
}
