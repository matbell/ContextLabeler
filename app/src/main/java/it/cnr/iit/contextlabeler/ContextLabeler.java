package it.cnr.iit.contextlabeler;

import android.app.Application;
import android.content.Context;

import org.acra.ACRA;
import org.acra.annotation.AcraCore;
import org.acra.annotation.AcraHttpSender;
import org.acra.data.StringFormat;
import org.acra.sender.HttpSender;


@AcraCore(buildConfigClass = BuildConfig.class, reportFormat= StringFormat.JSON)
@AcraHttpSender(
        httpMethod = HttpSender.Method.PUT,
        uri = "http://mcampana.iit.cnr.it:5984/acra-contextlabeler/_design/acra-storage/_update/report",
        basicAuthLogin = "contextlabeler",
        basicAuthPassword = "contextlabeler"
)
public class ContextLabeler extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

        ACRA.init(this);
    }
}
