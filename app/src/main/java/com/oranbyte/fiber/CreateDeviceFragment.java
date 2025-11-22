package com.oranbyte.fiber;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.oranbyte.fiber.models.ApiResponse;
import com.oranbyte.fiber.models.DeviceType;
import com.oranbyte.fiber.services.DeviceService;
import com.oranbyte.fiber.services.impl.DeviceServiceImpl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateDeviceFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private EditText deviceNameInput, subtitleInput, deviceKeyInput, pointTimeInput, minuteTimeInput;
    private TextView pointTimeTitle, minuteTimeTitle, selectTypeBtn;
    List<DeviceType> deviceTypes = new ArrayList<>();



    public CreateDeviceFragment() {
    }

    public static CreateDeviceFragment newInstance(String param1, String param2) {
        CreateDeviceFragment fragment = new CreateDeviceFragment();
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

        View view = inflater.inflate(R.layout.fragment_create_device, container, false);

        deviceNameInput = view.findViewById(R.id.device_name_input);
        subtitleInput = view.findViewById(R.id.subtitle_input);
        deviceKeyInput = view.findViewById(R.id.device_key_input);
        pointTimeInput = view.findViewById(R.id.point_time_input);
        minuteTimeInput = view.findViewById(R.id.minute_time_input);
        pointTimeTitle = view.findViewById(R.id.point_time_title);
        minuteTimeTitle = view.findViewById(R.id.minute_time_title);
        selectTypeBtn = view.findViewById(R.id.select_type_btn);

        this.addDateTimePicker(pointTimeInput);

        pointTimeInput.setVisibility(View.GONE);
        minuteTimeInput.setVisibility(View.GONE);
        pointTimeTitle.setVisibility(View.GONE);
        minuteTimeTitle.setVisibility(View.GONE);


        DeviceService deviceService = new DeviceServiceImpl(requireContext());
        Gson gson = new Gson();

        deviceService.getDeviceTypes(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {

                Log.d("API_RESPONSE", new Gson().toJson(response.body()));
                if (!response.isSuccessful() || response.body() == null) {
                    Log.i("API_ERROR", "Error: " + response.code());
                    return;
                };

                ApiResponse api = response.body();

                if (!api.isSuccess()) {
                    Toast.makeText(getContext(), api.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }

                String json = gson.toJson(api.getData());

                deviceTypes = gson.fromJson(
                        json,
                        new TypeToken<List<DeviceType>>(){}.getType()
                );

                Log.d("deviceTypes", deviceTypes.toString());

                selectTypeBtn.setOnClickListener(v -> {

                    PopupMenu popup = new PopupMenu(getContext(), selectTypeBtn);

                    for (int i = 0; i < deviceTypes.size(); i++) {
                        popup.getMenu().add(0, i, i, deviceTypes.get(i).name);
                    }


                    popup.setOnMenuItemClickListener(item -> {
                        String text = deviceTypes.get(item.getItemId()).name;
                        selectTypeBtn.setText(text);

                        if (text.toLowerCase().contains("interval")) {
                            pointTimeInput.setVisibility(View.VISIBLE);
                            pointTimeTitle.setVisibility(View.VISIBLE);
                            minuteTimeInput.setVisibility(View.VISIBLE);
                            minuteTimeTitle.setVisibility(View.VISIBLE);
                        } else if (text.toLowerCase().contains("timeout")) {
                            pointTimeInput.setVisibility(View.VISIBLE);
                            pointTimeTitle.setVisibility(View.VISIBLE);
                            minuteTimeInput.setVisibility(View.GONE);
                            minuteTimeTitle.setVisibility(View.GONE);
                        } else {
                            pointTimeInput.setVisibility(View.GONE);
                            pointTimeTitle.setVisibility(View.GONE);
                            minuteTimeInput.setVisibility(View.GONE);
                            minuteTimeTitle.setVisibility(View.GONE);
                        }

                        return true;
                    });

                    popup.show();
                });


            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.e("API_ERROR", "Failure: " + t.getMessage());
            }
        });





        return view;
    }

    void addDateTimePicker(EditText timeInput){
        timeInput.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();

            DatePickerDialog datePicker = new DatePickerDialog(
                    getContext(),
                    (view1, year, month, day) -> {

                        TimePickerDialog timePicker = new TimePickerDialog(
                                getContext(),
                                (view2, hour, minute) -> {
                                    // Format output y-m-d h:m
                                    String formatted =
                                            String.format("%04d-%02d-%02d %02d:%02d",
                                                    year, (month + 1), day, hour, minute);

                                    timeInput.setText(formatted);
                                },
                                calendar.get(Calendar.HOUR_OF_DAY),
                                calendar.get(Calendar.MINUTE),
                                true
                        );

                        timePicker.show();
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            );

            datePicker.show();
        });
    }


}
