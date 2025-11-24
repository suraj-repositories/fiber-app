package com.oranbyte.fiber;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.oranbyte.fiber.models.ApiResponse;
import com.oranbyte.fiber.models.Device;
import com.oranbyte.fiber.services.DeviceService;
import com.oranbyte.fiber.services.impl.DeviceServiceImpl;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NextPageDetailFragment extends Fragment {

    private static final String ARG_DEVICE_KEY = "device_key";
    private String deviceKey;
    private final Gson gson = new Gson();

    DeviceService deviceService;

    public static NextPageDetailFragment newInstance(String deviceKey){
        NextPageDetailFragment fragment = new NextPageDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_DEVICE_KEY, deviceKey);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            deviceKey = getArguments().getString(ARG_DEVICE_KEY);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_next_page_detail, container, false);
        deviceService = new DeviceServiceImpl(requireContext());

        final TextView tvName = view.findViewById(R.id.device_key_tv);
        final TextView tvSubTitle = view.findViewById(R.id.sub_title);
        final EditText deviceValueInput = view.findViewById(R.id.device_name_input);
        Button updateButton = view.findViewById(R.id.btn_save_device_state);

         updateButton.setOnClickListener(e->{
             String deviceValue = deviceValueInput.getText().toString();
             updateStateOfDevice(deviceKey, deviceValue);
         });

        fetchDevice(tvName, tvSubTitle, deviceValueInput);

        return view;
    }

    private void fetchDevice(TextView tvName, TextView tvSubTitle, EditText deviceValueInput) {

        deviceService.getDevice(deviceKey, new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (!isAdded()) return;

                if (!response.isSuccessful() || response.body() == null) {
                    Log.e("API_ERROR", "Error: " + response.code());
                    return;
                }

                ApiResponse api = response.body();
                Log.d("DEVICE-API_RESPONSE", api.toString());

                if (!api.isSuccess()) {
                    Toast.makeText(getContext(), api.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }

                Device device = parseDevice(api);
                if (device != null) {
                    tvName.setText(device.getName());
                    tvSubTitle.setText(device.getSub_title());
                    deviceValueInput.setText(device.getValue());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.e("API_ERROR", t.getMessage(), t);
            }
        });
    }

    private Device parseDevice(ApiResponse api) {
        if (api.getData() == null) return null;
        String json = gson.toJson(api.getData());
        Log.d("DEVICE-JSON", json);
        return gson.fromJson(json, Device.class);
    }

    private void updateStateOfDevice(String key,String newValue){
        deviceService.updateDeviceValue(key, newValue, new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                Log.d("DEVICE_UPDATE", "Updated " + key + " to " + newValue);
                if(response.isSuccessful()){
                    Toast.makeText(getContext(), "Updated " + key + " to " + newValue, Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.e("DEVICE_UPDATE", t.getMessage());
            }
        });
    }

}
