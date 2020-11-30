package uz.alexits.cargostar.push;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "notification")
public class Notification {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long id;

    @ColumnInfo(name = "title")
    private final String title;

    //id of request, invoice or transportation
    @ColumnInfo(name = "body")
    private final String body;

    @ColumnInfo(name = "is_read", defaultValue = "false")
    private boolean isRead;

    @ColumnInfo(name = "activity")
    private final String activity;

    @ColumnInfo(name = "link")
    private final String link;

    @ColumnInfo(name = "receive_date")
    private final Date receiveDate;

    public Notification(final String title,
                        final String body,
                        final boolean isRead,
                        final String activity,
                        final String link,
                        final Date receiveDate) {
        this.id = id;
        this.title = title;
        this.body = body;
        this.isRead = isRead;
        this.activity = activity;
        this.link = link;
        this.receiveDate = receiveDate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public String getActivity() {
        return activity;
    }

    public String getLink() {
        return link;
    }

    public Date getReceiveDate() {
        return receiveDate;
    }

    @NonNull
    @Override
    public String toString() {
        return "Notification{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", body='" + body + '\'' +
                ", isRead=" + isRead +
                ", activity='" + activity + '\'' +
                ", link='" + link + '\'' +
                ", receiveDate=" + receiveDate +
                '}';
    }
}
