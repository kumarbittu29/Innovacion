package in.sae_akgec.www.innovacion18;

import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Set;


public class ListDevices extends ListActivity{

    private BluetoothAdapter bluetoothAdapter2 = null;

    static String MAC_ADDRESS = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ArrayAdapter<String> ArrayBluetooth = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        bluetoothAdapter2 = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> devicesPaired = bluetoothAdapter2.getBondedDevices();

        if (devicesPaired.size() > 0) {
            for (BluetoothDevice device : devicesPaired) {
                String nameBt = device.getName();
                String macBt = device.getAddress();
                ArrayBluetooth.add(nameBt + "\n" + macBt);
            }
        }
        setListAdapter(ArrayBluetooth);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        String informationGeneral = ((TextView) v).getText().toString();

        //Toast.makeText(getApplicationContext(), "Info: " + informationGeneral, Toast.LENGTH_LONG).show();

        String macAddress = informationGeneral.substring(informationGeneral.length() - 17);

        Intent returnmac = new Intent();
        returnmac.putExtra(MAC_ADDRESS, macAddress);
        setResult(RESULT_OK, returnmac);
        finish();

    }
}
