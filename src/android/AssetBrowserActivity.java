package com.adobe.phonegap.csdk;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.adobe.creativesdk.foundation.internal.utils.AdobeCSDKException;
import com.adobe.creativesdk.foundation.storage.AdobeSelection;
import com.adobe.creativesdk.foundation.storage.AdobeUXAssetBrowser;
import com.adobe.creativesdk.foundation.storage.AdobeUXAssetBrowserConfiguration;
import com.adobe.creativesdk.foundation.storage.AdobeAssetDataSourceFilter;
import com.adobe.creativesdk.foundation.storage.AdobeAssetDataSourceFilterType;
import com.adobe.creativesdk.foundation.storage.AdobeAssetDataSourceType;

import java.util.ArrayList;
import java.util.EnumSet;

public class AssetBrowserActivity extends Activity {
    private static final String LOG_TAG = "CreativeSDK_AssetBrowserActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AdobeUXAssetBrowser assetBrowser = AdobeUXAssetBrowser.getSharedInstance();
        AdobeUXAssetBrowserConfiguration configuration = new AdobeUXAssetBrowserConfiguration();
        configuration.dataSourceFilter = AdobeAssetDataSourceFilter.createFromDataSources(
            EnumSet.of(AdobeAssetDataSourceType.AdobeAssetDataSourceFiles),
            AdobeAssetDataSourceFilterType.ADOBE_ASSET_DATASOURCE_FILTER_INCLUSION
        );

        try {
            assetBrowser.popupFileBrowser(this, 300, configuration);
        }
        catch (AdobeCSDKException e) {
            Log.e(LOG_TAG, e.getLocalizedMessage(), e);
        }
    }

    /**
     *
     * Called when the asset browser exits.
     *
     * @param requestCode       The request code originally supplied to startActivityForResult(),
     *                          allowing you to identify who this result came from.
     * @param resultCode        The integer result code returned by the child activity through its setResult().
     * @param intent            An Intent, which can return result data to the caller (various data can be attached to Intent "extras").
     */
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        Log.d(LOG_TAG, "onActivityResult");
        if (resultCode == Activity.RESULT_OK) {
            Log.d(LOG_TAG, "requestCode: " + requestCode);
            switch (requestCode) {
                case 300:
                    AdobeSelection selection = getSelection(intent);

                    Intent data = new Intent();
                    data.putExtras(intent);
                    setResult(Activity.RESULT_OK,data);
                    finish();

                    break;
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            //this.callbackContext.error("Asset Browser Canceled");
            Log.d(LOG_TAG, "Asset Browser Canceled");
            finish();
        }
    }

    private AdobeSelection getSelection(Intent data) {
        AdobeUXAssetBrowser.ResultProvider assetBrowserResult = new AdobeUXAssetBrowser.ResultProvider(data);
        ArrayList listOfSelectedAssetFiles = assetBrowserResult.getSelectionAssetArray();
        Log.d(LOG_TAG, "list = " + listOfSelectedAssetFiles.size());
        return (AdobeSelection) listOfSelectedAssetFiles.get(0);
    }
}
