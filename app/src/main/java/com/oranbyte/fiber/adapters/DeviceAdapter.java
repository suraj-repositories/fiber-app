package com.oranbyte.fiber.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.oranbyte.fiber.R;
import com.oranbyte.fiber.models.Device;

import java.util.ArrayList;
import java.util.List;
public class DeviceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_TOGGLE = 1;
    private static final int TYPE_PROGRESS = 2;
    private static final int TYPE_RADIO = 3;
    private static final int TYPE_CHECKBOX = 4;
    private static final int TYPE_NEXT_PAGE = 10;

    private List<Device> devices;
    private DeviceListener listener;

    public interface DeviceListener {
        void onToggle(Device device, boolean value);
        void onProgressChanged(Device device, int value);
        void onRadioSelected(Device device, String selected);
        void onCheckboxSelected(Device device, List<String> selected);
        void onOpenDetail(Device device);
    }

    public DeviceAdapter(List<Device> devices, DeviceListener listener) {
        this.devices = devices;
        this.listener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        Device d = devices.get(position);
        switch (d.getDevice_type_id()) {
            case "toggle": return TYPE_TOGGLE;
            case "progress": return TYPE_PROGRESS;
            case "radio": return TYPE_RADIO;
            case "checkbox": return TYPE_CHECKBOX;
            default: return TYPE_NEXT_PAGE;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case TYPE_TOGGLE:
                return new ToggleViewHolder(inflater.inflate(R.layout.item_toggle, parent, false));
            case TYPE_PROGRESS:
                return new ProgressViewHolder(inflater.inflate(R.layout.item_progress, parent, false));
            case TYPE_RADIO:
                return new RadioViewHolder(inflater.inflate(R.layout.item_radio, parent, false));
            case TYPE_CHECKBOX:
                return new CheckboxViewHolder(inflater.inflate(R.layout.item_checkbox, parent, false));
            default:
                return new NextPageViewHolder(inflater.inflate(R.layout.item_next_page, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Device d = devices.get(position);

        if (holder instanceof ToggleViewHolder) {
            ToggleViewHolder h = (ToggleViewHolder) holder;
            h.title.setText(d.getName());
            h.toggle.setChecked("on".equals(d.getValue()));
            h.toggle.setOnCheckedChangeListener((btn, checked) -> listener.onToggle(d, checked));
        } else if (holder instanceof ProgressViewHolder) {
            ProgressViewHolder h = (ProgressViewHolder) holder;
            h.title.setText(d.getName());
            h.seekBar.setProgress(Integer.parseInt(d.getValue()));
            h.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    listener.onProgressChanged(d, progress);
                }
                @Override public void onStartTrackingTouch(SeekBar seekBar) {}
                @Override public void onStopTrackingTouch(SeekBar seekBar) {}
            });
        } else if (holder instanceof RadioViewHolder) {
            RadioViewHolder h = (RadioViewHolder) holder;
            h.title.setText(d.getName());
            h.group.removeAllViews();
            for (String option : d.getAllowed_values()) {
                RadioButton rb = new RadioButton(h.itemView.getContext());
                rb.setText(option);
                if (option.equals(d.getValue())) rb.setChecked(true);
                rb.setOnClickListener(v -> listener.onRadioSelected(d, option));
                h.group.addView(rb);
            }
        } else if (holder instanceof CheckboxViewHolder) {
            CheckboxViewHolder h = (CheckboxViewHolder) holder;
            h.title.setText(d.getName());
            h.layout.removeAllViews();
            List<String> selected = new ArrayList<>();
            for (String option : d.getAllowed_values()) {
                CheckBox cb = new CheckBox(h.itemView.getContext());
                cb.setText(option);
                if (d.getValue() != null && d.getValue().contains(option)) cb.setChecked(true);
                cb.setOnCheckedChangeListener((btn, isChecked) -> {
                    if (isChecked) selected.add(option); else selected.remove(option);
                    listener.onCheckboxSelected(d, selected);
                });
                h.layout.addView(cb);
            }
        } else if (holder instanceof NextPageViewHolder) {
            NextPageViewHolder h = (NextPageViewHolder) holder;
            h.title.setText(d.getName());
            h.button.setOnClickListener(v -> listener.onOpenDetail(d));
        }
    }

    @Override
    public int getItemCount() { return devices.size(); }

    // ViewHolders
    static class ToggleViewHolder extends RecyclerView.ViewHolder {
        TextView title; Switch toggle;
        ToggleViewHolder(View v) { super(v); title=v.findViewById(R.id.title); toggle=v.findViewById(R.id.toggle);}
    }
    static class ProgressViewHolder extends RecyclerView.ViewHolder {
        TextView title; SeekBar seekBar;
        ProgressViewHolder(View v){ super(v); title=v.findViewById(R.id.title); seekBar=v.findViewById(R.id.seekBar);}
    }
    static class RadioViewHolder extends RecyclerView.ViewHolder {
        TextView title; RadioGroup group;
        RadioViewHolder(View v){ super(v); title=v.findViewById(R.id.title); group=v.findViewById(R.id.group);}
    }
    static class CheckboxViewHolder extends RecyclerView.ViewHolder {
        TextView title; LinearLayout layout;
        CheckboxViewHolder(View v){ super(v); title=v.findViewById(R.id.title); layout=v.findViewById(R.id.layout);}
    }
    static class NextPageViewHolder extends RecyclerView.ViewHolder {
        TextView title; Button button;
        NextPageViewHolder(View v){ super(v); title=v.findViewById(R.id.title); button=v.findViewById(R.id.button);}
    }
}
