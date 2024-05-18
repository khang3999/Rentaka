package vn.edu.tdc.rentaka.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import vn.edu.tdc.rentaka.R;
import vn.edu.tdc.rentaka.databinding.BottomSheetQuestionMarkTimeRentBinding;
import vn.edu.tdc.rentaka.databinding.ChooseDateLayoutBinding;
import vn.edu.tdc.rentaka.models.Date;

public class ChooseDateActivity extends AppCompatActivity {
    // Properties
    private ChooseDateLayoutBinding binding;
    private Date dateStart;
    private Date dateEnd;
    private LocalTime timeStart;
    private LocalTime timeEnd;
    private int totalDays = 0;

    // Khi thay doi
    private LocalTime timePickUp = LocalTime.of(5, 0, 0);
    private LocalTime timeReturn = LocalTime.of(23, 0, 0);
    // Khi nao chon ngay thang, gio giac moi cho search
    private int statusChooseCondition = 0;
    // Mang chua cac ngay đã có lịch đặt trước get từ databse Co kieu du lieu là Date
    // Su dung khi dat xe, taij man hinh nay khong su dung
    //private ArrayList<LocalDate> disabledDates = new ArrayList<LocalDate>();
    private final long MILISOFDAY = 86400000;
    private ArrayList<LocalTime> listTimeStart;
    private ArrayList<LocalTime> listTimeEnd;
    private ArrayAdapter<LocalTime> adapterTimeSpinnerStart;
    private ArrayAdapter<LocalTime> adapterTimeSpinnerEnd;
    private BottomSheetDialog bottomSheetDialog;
    private BottomSheetQuestionMarkTimeRentBinding bottomSheetDialogBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Gan bingding
        binding = ChooseDateLayoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Khoi tao bottom sheet dialog
        bottomSheetDialogBinding = BottomSheetQuestionMarkTimeRentBinding.inflate(getLayoutInflater(), null, false);
        bottomSheetDialog = new BottomSheetDialog(ChooseDateActivity.this, R.style.BottomSheetDialogTheme);
        bottomSheetDialog.setContentView(bottomSheetDialogBinding.getRoot());

        // Do du lieu cho bottom sheet dialog
        bottomSheetDialogBinding.tvTitle.setText("Thời gian thuê xe");
        bottomSheetDialogBinding.tvContent.setText("Giá thuê xe được tính theo ngày. Nếu bạn thuê xe dưới 24h sẽ được tính giá tròn 1 ngày.");

        // Khoi tao mang chuan bi cho adapter
        listTimeStart = new ArrayList<>();
        listTimeEnd = new ArrayList<>();

        // Bat su kien
        /// SHOW DateRangePicker when click on image calendar button
        binding.btnChooseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateRangePicker();
            }
        });

        // Bat su kien cho spinner timeStart
        binding.spinnerTimeStart.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Ngay nhan = ngay tra
                if(dateStart.getDay() == dateEnd.getDay() && dateStart.getMonth() == dateEnd.getMonth()){
                    // Thoi gian tra luon = thoi gian co the nhan + 1h ->
                    createTimeList(listTimeStart.get(position).plusHours(1),timeReturn,listTimeEnd);
                    // Cap nhat adapter
                    adapterTimeSpinnerEnd.notifyDataSetChanged();
//                    // hien thi o spinner
//                    binding.spinnerTimeEnd.setSelection(0);
//                    // Cap nhat bien timeEnd == timeEndMax = timeReturn
//                    timeEnd = listTimeEnd.get(0);
                } else{ // Ngay nhan != ngay tra
                    createTimeList(timePickUp,timeReturn,listTimeEnd);
                    // Cap nhat adapter
                    adapterTimeSpinnerEnd.notifyDataSetChanged();
//                    // hien thi o spinner
//                    binding.spinnerTimeEnd.setSelection(0);
                    // Cap nhat tong ngay
                    totalDays = dateEnd.getDay()-dateStart.getDay();
                    binding.tvTotalDay.setText(dateEnd.getDay()-dateStart.getDay()+" ngày");
//                    // Cap nhat bien timeEnd = timePickup => luon <= so ngay chenh lech
//                    timeEnd = listTimeEnd.get(0);
                }

                // Cap nhật bien timeStart = gia tri cua item tai vi tri dc tap vao trong listTimeStart
                timeStart = listTimeStart.get(position);

                // hien thi o spinner
                binding.spinnerTimeEnd.setSelection(0);
                // Cap nhat bien timeEnd == timeEndMax = timeReturn
                timeEnd = listTimeEnd.get(0);

                // Update confirm area
                binding.cfTimeStart.setText(timeStart.getHour()+"h00, ");
                binding.cfTimeEnd.setText(timeEnd.getHour()+"h00, ");
//                Log.d("TAGSTART", "onPositiveButtonClick: " + timeStart);
//                Log.d("TAGSTART", "onPositiveButtonClick: " + timeEnd);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Bat su kien cho spinner timeEnd
        binding.spinnerTimeEnd.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Set default totalDays
                totalDays = dateEnd.getDay() - dateStart.getDay();
                // Ngay nhan = ngay tra
                if (dateStart.getDay() == dateEnd.getDay() && dateStart.getMonth() == dateEnd.getMonth()){
                    timeEnd = listTimeEnd.get(position);
                } else { // Ngay nhan != ngay tra
                    if(listTimeEnd.get(position).isAfter(timeStart)){
                        totalDays += 1;
                    }
                    timeEnd = listTimeEnd.get(position);
                    binding.tvTotalDay.setText(totalDays+" ngày");
                }
                binding.cfTimeEnd.setText(timeEnd.getHour()+"h00, ");
               // Log.d("TAGEND", "choose time end: " + timeEnd);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Bat su kien cho ic_question_mark
        binding.imgQuestionMark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show dialog
                bottomSheetDialog.show();
            }
        });

        // Bat su kien cho nut close
        bottomSheetDialogBinding.btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });
        // Bat su kien cho button continue
        binding.btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.isEnabled()) {
                    Intent intent = new Intent(ChooseDateActivity.this, MainActivity.class);
                    String date = timeStart.getHour()+"h00, " + dateStart.toString() + " - "
                            + timeEnd.getHour()+"h00, " + dateEnd.toString();
                    intent.putExtra("date", date);
                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Dua ve default
        binding.tvDateStart.setText("Chưa chọn");
        binding.tvDateEnd.setText("Chưa chọn");
        dateStart = new Date();
        dateEnd = new Date();
        binding.cfDateStart.setText("? - ");
        binding.cfDateEnd.setText("?");

        binding.spinnerTimeStart.setEnabled(false);
        binding.spinnerTimeEnd.setEnabled(false);
        binding.btnContinue.setEnabled(false);

        // Setting for spinner
        // Khoi tao mang array list cho thoi gian nhan xe: listTimeStart
        createTimeList(timePickUp, timeReturn, listTimeStart);
        // Khoi tao mang array list cho thoi gian tra xe: listTimeEnd
        createTimeList(timePickUp, timeReturn, listTimeEnd);

        adapterTimeSpinnerStart = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, listTimeStart);
        binding.spinnerTimeStart.setAdapter(adapterTimeSpinnerStart);

        adapterTimeSpinnerEnd = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, listTimeEnd);
        binding.spinnerTimeEnd.setAdapter(adapterTimeSpinnerEnd);

    }

    // DATE RANGE PICKER
    private void showDateRangePicker() {
        // Tạo một CalendarConstraints.Builder -----Start
        CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder();
        // declare: Date
        LocalDate today = LocalDate.now();// Lấy ngày hôm nay
        // decalre: Time
        LocalTime currentTime = LocalTime.now();// Lấy thời gian hiện tại
        // Set ngay hien thi tren textHeader dung logic: Neu tre hon endTime => set ngay mai
        // Vi Localdate khong cho set lai nen can tao moi
        LocalDate defaultDatePicked = currentTime.isAfter(timeReturn) ? today.plusDays(1) : today;
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

        // Tạo ràng buộc lịch từ builder
        CalendarConstraints constraints = constraintsBuilder.build();
        // ------END
        // 1. Tao builder
        MaterialDatePicker<Pair<Long, Long>> dateRangePicker = MaterialDatePicker.Builder.dateRangePicker()
                .setTitleText("Select Date")
                // Set ngay mac dinh duoc chon và set text tren textHeader
                .setSelection(Pair.create((defaultDatePicked.toEpochDay() * MILISOFDAY), (defaultDatePicked.toEpochDay() * MILISOFDAY)))
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
                    // Tạo Instant từ số mili giây
                    Instant instantStart = Instant.ofEpochMilli(startDateLong);
                    Instant instantEnd = Instant.ofEpochMilli(endDateLong);
                    LocalDate startLocalDate = instantStart.atZone(ZoneId.of("UTC")).toLocalDate();
                    LocalDate endLocalDate = instantEnd.atZone(ZoneId.of("UTC")).toLocalDate();

                    // Luu ngay thang duoc chon lai
                    dateStart = new Date(startLocalDate);
                    dateEnd = new Date(endLocalDate);

                    // Chuyen trang thai
                    binding.spinnerTimeStart.setEnabled(true);
                    binding.spinnerTimeEnd.setEnabled(true);

                    // XU LY UPDATE Choose date UI vaf luu gia tri cac bien
                    // Ngay bat dau = today
                    if (startLocalDate.isEqual(LocalDate.now())){
                        // Gio nhan xe xu li chung
                        LocalTime currentTime = LocalTime.now();
                        // 1.1 Xu li thoi gian nhan xe
                        if (currentTime.isBefore(timePickUp)){ // Book truoc 5h sang
                            timeStart = timePickUp;
                        } else { // Book sau 5h sang, luon luon truoc timeEnd vi neu sau timeEnd => truong hop khac da xu ly
                            timeStart = currentTime.plusHours(1);
                        }
                        // Ham createTimeList nhan 3 tham so: minTime, maxTime, ArrayList cho adapter
                        // maxTimeStart luon = timeReturn - 1
                        createTimeList(timeStart, timeReturn.minusHours(1),listTimeStart);
//                        Log.d("TAG", "onPositiveButtonClick: " + listTimeStart);

                        // 1.2 Xu li thoi gian tra xe
                        // Tinh list gio nhan xe
                        // Gio tra xe min xu li theo ngay tra
                        if (endLocalDate.equals(LocalDate.now())){ // Tra xe trong ngay hom nay, timeEndMin = timeStartMin + 1:  (timeStartMin = timeStart, end tuong tu)
                            timeEnd = timeStart.plusHours(1);
                        } else { // Tra xe trong ngay khac
                            timeEnd = timePickUp;
                        }
                        // Tinh list gio tra xe
                        // TimeEndMin thay doi, timeEndMax khong thay doi luon = timeReturn
                        createTimeList(timeEnd, timeReturn,listTimeEnd);
//                        Log.d("TAG", "onPositiveButtonClick: " + listTimeEnd);

                    } else { // Ngay bat dau != hom nay
                        // 1. Xu li list time start
                        // Tinh timeStartMin = timeStart = timePickup
                        timeStart = timePickUp;
                        // Ham createTimeList nhan 3 tham so: minTime, maxTime, ArrayList cho adapter
                        // minTime luon = timePickup,maxTimeStart luon = timeReturn - 1
                        createTimeList(timeStart,timeReturn.minusHours(1), listTimeStart);

                        // Xu li list time end
                        if (endLocalDate.equals(startLocalDate)){ // Ngay bat dau == ngay ket thuc
                            timeEnd = timeStart.plusHours(1);
                            // tra trong ngay: list
                            createTimeList(timeEnd, timeReturn, listTimeEnd);
                        }else {
                            timeEnd = timeStart;
                            createTimeList(timeEnd, timeReturn, listTimeEnd);
                        }
                    }

                    adapterTimeSpinnerStart.notifyDataSetChanged();
                    adapterTimeSpinnerEnd.notifyDataSetChanged();
                    binding.spinnerTimeStart.setSelection(0);
                    binding.spinnerTimeEnd.setSelection(0);

                    // Update tv choose date area
                    binding.tvDateStart.setText(startLocalDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                    binding.tvDateEnd.setText(endLocalDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                    // Update confirm time
                    binding.cfTimeStart.setText(timeStart.getHour() + "h00, ");
                    binding.cfTimeEnd.setText(timeEnd.getHour() +"h00, ");
                    // Update confirm date
                    binding.cfDateStart.setText(startLocalDate.format(DateTimeFormatter.ofPattern("dd/MM")) + " - ");
                    binding.cfDateEnd.setText(endLocalDate.format(DateTimeFormatter.ofPattern("dd/MM")));

                    // Update total date
                    if (startLocalDate.equals(endLocalDate)){
                        totalDays = 1;
                    } else {
                        totalDays = endLocalDate.getDayOfMonth() - startLocalDate.getDayOfMonth();
                    }
                    binding.tvTotalDay.setText(totalDays + " ngày");

                    // Chuyen trang thai de hien thi button continue
                    binding.btnContinue.setEnabled(true);
//                    Log.d("TAGSAVE", "onPositiveButtonClick: " + timeStart);
//                    Log.d("TAGSAVE", "onPositiveButtonClick: " + timeEnd);
                }
            }
        });
        // Show dialog
        dateRangePicker.show(getSupportFragmentManager(), "Material_Range");
    }


    private void createTimeList(LocalTime minTime, LocalTime maxTime, ArrayList<LocalTime> list){
        list.clear();
        for (int i = minTime.getHour(); i <= maxTime.getHour(); i++ ){
            LocalTime t = LocalTime.of(i,0,0);
            list.add(t);
        }
    }
}