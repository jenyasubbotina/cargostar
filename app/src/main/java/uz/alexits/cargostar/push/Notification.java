package uz.alexits.cargostar.push;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import uz.alexits.cargostar.model.shipping.Invoice;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Entity(tableName = "notification")
public class Notification {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long id;

    @ColumnInfo(name = "is_read", defaultValue = "false")
    private boolean isRead;

    @ColumnInfo(name = "title")
    private final String title;

    //id of request, invoice or transportation
    @ColumnInfo(name = "body")
    private final String body;

    @ColumnInfo(name = "link")
    private final String link;

    @ColumnInfo(name = "receive_date")
    private final Date receiveDate;


    public Notification(final boolean isRead,
                        final String title,
                        final String body,
                        final String link,
                        final Date receiveDate) {
        this.isRead = isRead;
        this.title = title;
        this.body = body;
        this.link = link;
        this.receiveDate = receiveDate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public boolean isRead() {
        return isRead;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
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
                ", isRead=" + isRead +
                ", title='" + title + '\'' +
                ", body='" + body + '\'' +
                ", link='" + link + '\'' +
                ", receiveDate=" + receiveDate +
                '}';
    }
}
