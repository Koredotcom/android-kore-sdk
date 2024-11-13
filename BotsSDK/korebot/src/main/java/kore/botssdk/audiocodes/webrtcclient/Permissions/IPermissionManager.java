package kore.botssdk.audiocodes.webrtcclient.Permissions;


import android.content.Context;

public interface IPermissionManager
{
    public boolean checkPermission(Context context, PermissionManagerType permissionType);
}
