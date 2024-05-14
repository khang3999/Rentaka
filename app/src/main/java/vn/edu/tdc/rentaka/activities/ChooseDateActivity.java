package vn.edu.tdc.rentaka.activities;

import android.content.DialogInterface;
import android.icu.util.LocaleData;
import android.os.Bundle;
import android.os.Parcel;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;

import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import org.checkerframework.checker.units.qual.C;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import vn.edu.tdc.rentaka.R;
import vn.edu.tdc.rentaka.databinding.ChooseDateLayoutBinding;

public class ChooseDateActivity extends AppCompatActivity {
    // Properties
    private ChooseDateLayoutBinding binding;
    private LocalDate dateStart;
    private LocalDate dateEnd;
    private LocalTime timeStart = LocalTime.of(5, 0, 0);
    private LocalTime timeEnd = LocalTime.of(21, 0, 0);
    // Khi nao chon ngay thang, gio giac moi cho search
    private boolean isChooseCondition;
    // Mang chua cac ngay đã có lịch đặt trước get từ databse Co kieu du lieu là Date
    private ArrayList<LocalDate> disabledDates = new ArrayList<LocalDate>();
    private final long MILISOFDAY = 86400000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ChooseDateLayoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnChooseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateRangePicker();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Set is choose condition
        isChooseCondition = false;
        // Dua ve default
        setDateAndTimeDefault();
    }

    private void showDateRangePicker() {
        // Tạo một CalendarConstraints.Builder -----Start
        CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder();
        // declare: Date
        LocalDate today = LocalDate.now();// Lấy ngày hôm nay
        // decalre: Time
        LocalTime currentTime = LocalTime.now();// Lấy thời gian hiện tại
        // Set ngay hien thi tren textHeader dung logic: Neu tre hon endTime => set ngay mai
        // Vi Localdate khong cho set lai nen can tao moi
        LocalDate defaultDatePicked = currentTime.isAfter(timeEnd) ? today.plusDays(1) : today;
        LocalDate endMonthDisplay = today.plusMonths(3);

//        // Thiết lập hiển thị 4 thang. Vi du: thang 5 thi hien thi thang 5 6 7 8;
        constraintsBuilder.setStart(defaultDatePicked.toEpochDay() * MILISOFDAY);
        constraintsBuilder.setEnd(endMonthDisplay.toEpochDay() * MILISOFDAY);

        // Du lieu gia khi dung cho chon ngay thue, o man hinh nay khong xai
//        LocalDate lc1 = LocalDate.of(2024, 5, 17);
//        LocalDate lc2 = LocalDate.of(2024, 5, 18);
//        disabledDates.add(lc1);
//        disabledDates.add(lc2);
        // Set Validator de hien thi ngay chon theo logic
        constraintsBuilder.setValidator(new CalendarConstraints.DateValidator() {
            @Override
            public boolean isValid(long dateOfCalendar) {
                // Thuat toan dung khi chon ngay thue
//                LocalDate localDate = convertToLocalDate(dateOfCalendar);
//                if (localDate.compareTo(defaultDatePicked) >= 0) {
//                    for (LocalDate date : disabledDates) {
//                        if (localDate.isEqual(date)) {
//                            return false;
//                        }
//                    }
//                    return true;
//                }
//                return false;
                return dateOfCalendar >= defaultDatePicked.toEpochDay() * MILISOFDAY;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(@NonNull Parcel dest, int flags) {

            }
        });
        // Tạo date format
//        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        // Tạo ràng buộc lịch từ builder
        CalendarConstraints constraints = constraintsBuilder.build();
        // ------End
        // 1. Tao builder
        MaterialDatePicker<Pair<Long, Long>> dateRangePicker = MaterialDatePicker.Builder.dateRangePicker()
                .setTitleText("Select Date")
                // Set ngay mac dinh duoc chon và set text tren textHeader
                .setSelection(Pair.create((defaultDatePicked.toEpochDay() * MILISOFDAY), (defaultDatePicked.plusDays(1).toEpochDay() * MILISOFDAY)))
                .setCalendarConstraints(constraints)
                .build();
        // Khi nhan button save
        dateRangePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object o) {
                // Lấy ngày bắt đầu và kết thúc từ selection
                Pair<Long, Long> selectedDates = dateRangePicker.getSelection();
                // Kiểm tra xem selectedDates có khác null hay không
                if (selectedDates != null) {
                    long startDateLong = selectedDates.first;
                    long endDateLong = selectedDates.second;
                    LocalDate startDate = convertToLocalDate(startDateLong);
                    LocalDate endDate = convertToLocalDate(endDateLong);
                    // Luu ngay thang duoc chon lai
                    dateStart = startDate;
                    dateEnd = endDate;
                    // Khi nhan save, khong goi onResume cua Activity => update giao dien ngay khi ban button save
                    // Chuyển trang thai da cap nhat
                    isChooseCondition = true;
                }
            }
        });
        // Show dialog
        dateRangePicker.show(getSupportFragmentManager(), "Material_Range");
    }

//    private void updateDateAndTime(long startDate, long endDate) {
//        // Update at confirm area
//        binding.cfTimeStart.setText(timeCheckIn + ", ");
//        binding.cfDateStart.setText(dateStart.substring(0, 5) + " - ");
//        binding.cfTimeEnd.setText(timeCheckOut + ", ");
//        binding.cfDateEnd.setText(dateEnd.substring(0, 5));
//
//        // Update total day
//        long differenceInMilliSecond = Math.abs(endDate - startDate);
//        long days = (long) Math.ceil(TimeUnit.MILLISECONDS.toDays(differenceInMilliSecond));
//        binding.tvTotalDay.setText(days + " ngày ");
//    }

    private void setDateAndTimeDefault() {
        LocalDate today = LocalDate.now();
        LocalTime currentTime = LocalTime.now();
        // Choose date area
        binding.tvDateStart.setText(today.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        binding.tvDateEnd.setText(today.plusDays(1).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));


        // Confirm area
        // Date
        binding.cfDateStart.setText(today.format(DateTimeFormatter.ofPattern("dd/MM")));
        binding.cfDateEnd.setText(today.plusDays(1).format(DateTimeFormatter.ofPattern("dd/MM")));
        // Time
        if (currentTime.getHour() < timeEnd.getHour()){ // Van con trong khoang thoi gian hop le
            // Set date
            binding.cfDateStart.setText(today.format(DateTimeFormatter.ofPattern("dd/MM")));
            binding.cfDateEnd.setText(today.plusDays(1).format(DateTimeFormatter.ofPattern("dd/MM")));
            // Set time
            if (currentTime.getMinute() <= 30) {
                binding.cfTimeStart.setText(currentTime.format(DateTimeFormatter.ofPattern("HH'h'mm")));
            } else {
                binding.cfTimeEnd.setText(currentTime.plusHours(1).format(DateTimeFormatter.ofPattern("HH'h'mm")));
            }
        } else {
            // Set date
            binding.cfDateStart.setText(today.plusDays(1).format(DateTimeFormatter.ofPattern("dd/MM")));
            binding.cfDateEnd.setText(today.plusDays(2).format(DateTimeFormatter.ofPattern("dd/MM")));
            binding.cfTimeStart.setText(timeStart.format(DateTimeFormatter.ofPattern("HH'h'mm")));
            binding.cfTimeStart.setText(timeStart.format(DateTimeFormatter.ofPattern("HH'h'mm")));
        }

//        SimpleDateFormat dateDefault = new SimpleDateFormat("dd/MM", Locale.getDefault());
//        SimpleDateFormat timeDefault = new SimpleDateFormat("HH'h'mm", Locale.getDefault());
//        binding.cfTimeStart.setText(timeDefault.format(new Date()) + ", ");
//        binding.cfDateStart.setText(dateDefault.format(new Date()) + " - ");
//        binding.cfTimeEnd.setText(", ");
//        binding.cfDateEnd.setText(dateDefault.format(new Date()));
//        binding.tvTotalDay.setText("1 ngày");
    }

    private LocalDate convertToLocalDate(long milliseconds) {
        // Tạo Instant từ số mili giây
        Instant instant = Instant.ofEpochMilli(milliseconds);
        // Chuyển đổi Instant thành LocalDate với múi giờ UTC
        LocalDate localDate = instant.atZone(ZoneId.of("UTC")).toLocalDate();
        return localDate;
    }
}