package kore.botssdk.models;

import java.util.List;

public class BotOptionsModel
{
    private List<BotOptionModel> tasks;

    private String heading;

    public void setTasks(List<BotOptionModel> tasks)
    {
        this.tasks = tasks;
    }

    public List<BotOptionModel> getTasks()
    {
        return tasks;
    }

    public void setHeading(String heading)
    {
        this.heading = heading;
    }

    public String getHeading()
    {
        return heading;
    }
}
