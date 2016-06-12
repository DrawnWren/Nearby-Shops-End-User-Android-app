package org.nearbyshops.enduser.Carts;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.nearbyshops.enduser.DaggerComponentBuilder;
import org.nearbyshops.enduser.ModelStats.DeliveryAddress;
import org.nearbyshops.enduser.R;
import org.nearbyshops.enduser.RetrofitRESTContract.DeliveryAddressService;
import org.nearbyshops.enduser.Utility.UtilityGeneral;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddAddressActivity extends AppCompatActivity implements View.OnClickListener, Callback<DeliveryAddress> {

    DeliveryAddress deliveryAddress;

    @Inject
    DeliveryAddressService deliveryAddressService;

    TextView addDeliveryAddress;

    // address Fields

    @Bind(R.id.receiversName)
    EditText receiversName;

    @Bind(R.id.receiversPhoneNumber)
    EditText receiversPhoneNumber;

    @Bind(R.id.deliveryAddress)
    EditText deliveryAddressView;

    @Bind(R.id.addressCity)
    EditText city;

    @Bind(R.id.pincode)
    EditText pincode;

    @Bind(R.id.landmark)
    EditText landMark;



    public AddAddressActivity() {

        DaggerComponentBuilder.getInstance()
                .getNetComponent().Inject(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);

        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Bind Views

        addDeliveryAddress = (TextView) findViewById(R.id.addDeliveryAddress);
        addDeliveryAddress.setOnClickListener(this);

    }



    void getDataFromViews()
    {

        deliveryAddress = new DeliveryAddress();

        deliveryAddress.setName(receiversName.getText().toString());
        deliveryAddress.setDeliveryAddress(deliveryAddressView.getText().toString());
        deliveryAddress.setCity(city.getText().toString());

        if(pincode.getText().toString()!="")
        {
            deliveryAddress.setPincode(Long.parseLong(pincode.getText().toString()));
        }

        deliveryAddress.setLandmark(landMark.getText().toString());
        deliveryAddress.setPhoneNumber(Long.parseLong(receiversPhoneNumber.getText().toString()));

        deliveryAddress.setEndUserID(UtilityGeneral.getEndUserID(this));

    }


    @Override
    public void onClick(View v) {

        getDataFromViews();

        if(deliveryAddress!=null)
        {
            Call<DeliveryAddress> call = deliveryAddressService.postAddress(deliveryAddress);
            call.enqueue(this);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        ButterKnife.unbind(this);

    }

    @Override
    public void onResponse(Call<DeliveryAddress> call, Response<DeliveryAddress> response) {

        if (response != null && response.code() == 201) {
            showToastMessage("Added Successfully !");
        }
        else
        {
            showToastMessage("Unsuccessful !");
        }
    }

    @Override
    public void onFailure(Call<DeliveryAddress> call, Throwable t) {

        showToastMessage("Addition failed. Try again !");
    }



    void showToastMessage(String message)
    {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }
}
