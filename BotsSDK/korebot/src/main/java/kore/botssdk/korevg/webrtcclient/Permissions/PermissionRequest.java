package kore.botssdk.korevg.webrtcclient.Permissions;


public interface PermissionRequest
{

    void granted();

    void revoked();

    void allResults(boolean allGranted);
}
