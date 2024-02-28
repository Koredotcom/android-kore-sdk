package kore.botssdk.audiocodes.webrtcclient.Permissions;


public class PermissionWrapper
{
    private static String TAG = "PermissionWrapper";

    public static final String READ_CONTACTS = "android.permission.READ_CONTACTS";
    public static final String READ_PHONE_STATE = "android.permission.READ_PHONE_STATE";
    public static final String BODY_SENSORS = "android.permission.BODY_SENSORS";
    public static final String WRITE_CALENDAR = "android.permission.WRITE_CALENDAR";
    public static final String CAMERA = "android.permission.CAMERA";
  //  public static final String ACCESS_FINE_LOCATION = "android.permission.ACCESS_FINE_LOCATION";
    public static final String RECORD_AUDIO = "android.permission.RECORD_AUDIO";
    public static final String READ_SMS = "android.permission.READ_SMS";
    public static final String READ_EXTERNAL_STORAGE = "android.permission.READ_EXTERNAL_STORAGE";

    private static PermissionWrapper instance;
    private IPermissionManager iPermissionManager;

    public static PermissionWrapper getInstance()
    {
        if (instance == null)
        {
            instance = new PermissionWrapper();
        }
        return instance;
    }

    public void setiPermissionManager(IPermissionManager iPermissionManager)
    {
        this.iPermissionManager = iPermissionManager;
    }

    public boolean checkPermission(PermissionManagerType permissionType)
    {
        if(iPermissionManager == null)
        {
            //we don't have implemntetion for iPermissionManager return true
            return true;
        }
        return iPermissionManager.checkPermission(permissionType);
    }
}
