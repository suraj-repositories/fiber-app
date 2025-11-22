package com.oranbyte.fiber;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.oranbyte.fiber.adapters.DeviceAdapter;
import com.oranbyte.fiber.databinding.FragmentDeviceControlBinding;
import com.oranbyte.fiber.models.ApiResponse;
import com.oranbyte.fiber.models.Device;
import com.oranbyte.fiber.services.DeviceService;
import com.oranbyte.fiber.services.impl.DeviceServiceImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeviceControlFragment extends Fragment {

    private FragmentDeviceControlBinding binding;
    private DeviceService deviceService;
    private Gson gson = new Gson();
    private List<Device> devices;
    private DeviceAdapter adapter;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentDeviceControlBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        deviceService = new DeviceServiceImpl(requireContext());

        // Setup RecyclerView and Adapter
        devices = new java.util.ArrayList<>();
        adapter = new DeviceAdapter(devices, new DeviceAdapter.DeviceListener() {
            @Override
            public void onToggle(Device device, boolean value) {
                device.setValue(value ? "on" : "off");
                updateDeviceValue(device.getDevice_key(), device.getValue());
            }

            @Override
            public void onProgressChanged(Device device, int value) {
                device.setValue(String.valueOf(value));
                updateDeviceValue(device.getDevice_key(), device.getValue());
            }

            @Override
            public void onRadioSelected(Device device, String selected) {
                device.setValue(selected);
                updateDeviceValue(device.getDevice_key(), device.getValue());
            }

            @Override
            public void onCheckboxSelected(Device device, List<String> selected) {
                device.setValue(String.join(",", selected));
                updateDeviceValue(device.getDevice_key(), device.getValue());
            }

            @Override
            public void onOpenDetail(Device device) {

                Fragment f = NextPageDetailFragment.newInstance(device.getDevice_key());
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, f)
                        .addToBackStack(null)
                        .commitAllowingStateLoss();
            }
        });

        binding.deviceRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.deviceRecycler.setAdapter(adapter);

        binding.swipeRefresh.setOnRefreshListener(this::loadDevices);

        loadDevices();
    }

    private void loadDevices() {
        binding.swipeRefresh.setRefreshing(true);

        deviceService.getDevices(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (!isAdded()) return;
                binding.swipeRefresh.setRefreshing(false);


                if (!response.isSuccessful() || response.body() == null) {
                    Log.e("API_ERROR", "Error: " + response.code());
                    return;
                }

                ApiResponse api = response.body();
                Log.d("DEVICES-API_RESPONSE", api.toString());
                if (!api.isSuccess()) {
                    Toast.makeText(getContext(), api.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }

                String json = gson.toJson(api.getData());
                Log.d("DEVICES-JSON", json);

                devices.clear();
                devices.addAll(gson.fromJson(json, new TypeToken<List<Device>>() {}.getType()));

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                binding.swipeRefresh.setRefreshing(false);
                Log.e("API_ERROR", t.getMessage());
            }
        });
    }

    private void updateDeviceValue(String key, String newValue) {


        deviceService.updateDeviceValue(key, newValue, new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                Log.d("DEVICE_UPDATE", "Updated " + key + " to " + newValue);
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.e("DEVICE_UPDATE", t.getMessage());
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
