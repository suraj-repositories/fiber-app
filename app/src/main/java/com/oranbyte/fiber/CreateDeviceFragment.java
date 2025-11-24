package com.oranbyte.fiber;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.oranbyte.fiber.models.ApiResponse;
import com.oranbyte.fiber.models.Device;
import com.oranbyte.fiber.models.DeviceIcon;
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
    List<DeviceIcon> deviceIcons = new ArrayList<>();

    Button saveDeviceBtn;


    Gson gson = new Gson();


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
        saveDeviceBtn = view.findViewById(R.id.save_device_btn);


        this.addDateTimePicker(pointTimeInput);

        pointTimeInput.setVisibility(View.GONE);
        minuteTimeInput.setVisibility(View.GONE);
        pointTimeTitle.setVisibility(View.GONE);
        minuteTimeTitle.setVisibility(View.GONE);

        DeviceService deviceService = new DeviceServiceImpl(requireContext());
        setDeviceTypes(deviceService);
        setDeviceIcons(deviceService);

        saveDeviceBtn.setOnClickListener(v -> {
            // Debug toast
            Toast.makeText(requireContext(), "Button clicked", Toast.LENGTH_SHORT).show();

            Device d = new Device();
            d.setName(deviceNameInput.getText().toString().trim());
            d.setSub_title(subtitleInput.getText().toString().trim());
            d.setDevice_key(deviceKeyInput.getText().toString().trim());
            d.setPoint_time(pointTimeInput.getText().toString().trim());
            d.setMinuteTime(minuteTimeInput.getText().toString().trim());

            String selectedType = selectTypeBtn.getText().toString().trim();

            for (DeviceType deviceType : deviceTypes) {
                if (deviceType.getName().equalsIgnoreCase(selectedType)) {
                    d.setValue_type(deviceType.getType_key());
                    d.setDevice_type_name(deviceType.getName());
                    break;
                }
            }



            // Get selected icon
            DeviceIcon selectedIcon = null;
            for (DeviceIcon icon : deviceIcons) {
                if (icon.isSelected()) {
                    selectedIcon = icon;
                    break;
                }
            }

            if (selectedIcon != null) {
                d.setDevice_icons_id(selectedIcon.getId());
            } else {
                Toast.makeText(requireContext(), "Please select an icon", Toast.LENGTH_SHORT).show();
                return;
            }

            // Validate required fields before sending
            if (d.getDevice_key() == null || d.getValue_type() == null) {
                Toast.makeText(getContext(), "Device Key and Type are required", Toast.LENGTH_SHORT).show();
                return;
            }


            // Call API

            Log.d("PRESAVE_DEVICE", d.toString());
            deviceService.createDevice(d, new Callback<ApiResponse>() {
                @Override
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        ApiResponse api = response.body();
                        if (api.isSuccess()) {
                            Toast.makeText(requireContext(), "Device created successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(requireContext(), api.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(requireContext(), "API error: " + response.code(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse> call, Throwable t) {
                    Toast.makeText(requireContext(), "API failure: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });


        return view;
    }

    private void setDeviceTypes(DeviceService deviceService) {
        deviceService.getDeviceTypes(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {

                Log.d("API_RESPONSE", new Gson().toJson(response.body()));
                if (!response.isSuccessful() || response.body() == null) {
                    Log.i("API_ERROR", "Error: " + response.code());
                    return;
                }
                ;

                ApiResponse api = response.body();

                if (!api.isSuccess()) {
                    Toast.makeText(getContext(), api.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }

                String json = gson.toJson(api.getData());

                deviceTypes = gson.fromJson(
                        json,
                        new TypeToken<List<DeviceType>>() {
                        }.getType()
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

    }

    private void setDeviceIcons(DeviceService deviceService) {
        deviceService.getDeviceIcons(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {

                if (!response.isSuccessful() || response.body() == null) return;

                ApiResponse api = response.body();
                if (!api.isSuccess()) {
                    Toast.makeText(getContext(), api.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }

                String json = gson.toJson(api.getData());
                deviceIcons = gson.fromJson(json, new TypeToken<List<DeviceIcon>>() {
                }.getType());

                if (getView() != null) populateDeviceIcons(deviceIcons);
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.e("API_ERROR", "Failure: " + t.getMessage());
            }
        });
    }


    private void populateDeviceIcons(List<DeviceIcon> icons) {
        LinearLayout container = getView().findViewById(R.id.device_icon_container);
        container.removeAllViews();

        for (int i = 0; i < icons.size(); i++) {
            DeviceIcon icon = icons.get(i);

            CardView card = new CardView(getContext());
            LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                    dpToPx(64),
                    dpToPx(64)
            );
            if (i != icons.size() - 1) {
                cardParams.setMarginEnd(dpToPx(16));
            }
            card.setLayoutParams(cardParams);
            card.setRadius(dpToPx(32));
            card.setCardBackgroundColor(getResources().getColor(
                    icon.isSelected() ? R.color.theme_color : R.color.background_light
            ));
            card.setCardElevation(0);

            ImageView image = new ImageView(getContext());
            FrameLayout.LayoutParams imgParams = new FrameLayout.LayoutParams(
                    dpToPx(35),
                    dpToPx(35)
            );
            imgParams.gravity = Gravity.CENTER;
            image.setLayoutParams(imgParams);

            image.setImageResource(getDrawableByName("icon_" + icon.getIcon_key()));
            image.setColorFilter(getResources().getColor(
                    icon.isSelected() ? R.color.text_white : R.color.theme_color
            ));
            image.setScaleType(ImageView.ScaleType.FIT_CENTER);

            card.addView(image);
            container.addView(card);

            final int index = i;
            card.setOnClickListener(v -> onDeviceIconClicked(index));
        }

    }

    private int dpToPx(int dp) {
        return (int) (dp * getResources().getDisplayMetrics().density);
    }

    private int getDrawableByName(String name) {
        switch (name) {
            case "icon_bulb":
                return R.drawable.icon_bulb;
            case "icon_fan":
                return R.drawable.icon_fan;
            case "icon_power":
                return R.drawable.icon_power;
            default:
                Log.w("DRAWABLE_ERROR", "Drawable not found: " + name);
                return R.drawable.icon_category;
        }
    }


    private void onDeviceIconClicked(int index) {
        for (int i = 0; i < deviceIcons.size(); i++) {
            deviceIcons.get(i).setSelected(i == index);
        }
        populateDeviceIcons(deviceIcons);
    }

    void addDateTimePicker(EditText timeInput) {
        timeInput.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();

            DatePickerDialog datePicker = new DatePickerDialog(
                    getContext(),
                    (view1, year, month, day) -> {

                        TimePickerDialog timePicker = new TimePickerDialog(
                                getContext(),
                                (view2, hour, minute) -> {
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
