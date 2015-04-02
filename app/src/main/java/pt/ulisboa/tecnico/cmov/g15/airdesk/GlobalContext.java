package pt.ulisboa.tecnico.cmov.g15.airdesk;

import android.app.Application;

/**
 * Created by joao on 02-04-2015.
 */
public class GlobalContext extends Application {

    private AirDesk mAirDesk = new AirDesk();

    public AirDesk getAirDesk() { return mAirDesk; }
}
