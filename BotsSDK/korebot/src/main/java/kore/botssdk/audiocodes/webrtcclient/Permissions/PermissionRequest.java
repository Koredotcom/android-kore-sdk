package kore.botssdk.audiocodes.webrtcclient.Permissions;


public interface PermissionRequest
{

    void granted();

    void revoked();

    void allResults(boolean allGranted);
}
