package kore.botssdk.audiocodes.webrtcclient.Permissions;


public enum PermissionManagerType
{

    CONTACTS(PermissionWrapper.READ_CONTACTS),
    PHONE(PermissionWrapper.READ_PHONE_STATE),
    CAMERA(PermissionWrapper.CAMERA),
    MICROPHONE(PermissionWrapper.RECORD_AUDIO);
//    STORAGE(PermissionWrapper.READ_EXTERNAL_STORAGE);

    private String typeName;

    PermissionManagerType(String name)
    {
        this.typeName = name;
    }

    public String getTypeName()
    {
        return typeName;
    }
}
