package kore.botssdk.models;

import java.util.ArrayList;

public class BrandingEntitiesModel
{
    private String _id;
    private String label;
    private String hint;
    private String entity_id;
    private int version;
    private String type;
    private String def_value;
    private ArrayList<BrandingDependenciesModel> dependencies;
    private ArrayList<BrandingSubEntitiesModel> options;
    private String def_option_id;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public String getEntity_id() {
        return entity_id;
    }

    public void setEntity_id(String entity_id) {
        this.entity_id = entity_id;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDef_value() {
        return def_value;
    }

    public void setDef_value(String def_value) {
        this.def_value = def_value;
    }

    public ArrayList<BrandingDependenciesModel> getDependencies() {
        return dependencies;
    }

    public void setDependencies(ArrayList<BrandingDependenciesModel> dependencies) {
        this.dependencies = dependencies;
    }

    public void setOptions(ArrayList<BrandingSubEntitiesModel> options) {
        this.options = options;
    }

    public ArrayList<BrandingSubEntitiesModel> getOptions() {
        return options;
    }

    public void setDef_option_id(String def_option_id) {
        this.def_option_id = def_option_id;
    }

    public String getDef_option_id() {
        return def_option_id;
    }
}
