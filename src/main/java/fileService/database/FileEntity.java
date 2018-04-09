package fileService.database;

import javax.persistence.*;
import java.sql.Date;

@NamedQueries({
        @NamedQuery(
                name="getFileInformation",
                query="SELECT CONCAT(FILENAME, ',', UUID) AS RESULT FROM FileEntity entity WHERE entity.NAME LIKE :nameID and " +
                        "entity.FILE_ID LIKE :fileID"
        ),
        @NamedQuery(
                name="getFileInformationWithKey",
                query="SELECT CONCAT(ENCRYPTIONKEY,',', FILENAME, ',', UUID) AS RESULT FROM FileEntity entity WHERE entity.NAME LIKE :nameID and " +
                        "entity.FILE_ID LIKE :fileID"
        ),
        @NamedQuery(
                name="getAllFiles",
                query= "SELECT CONCAT(FILENAME, ',', FILE_ID, ',', DATE) AS RESULT FROM FileEntity entity  WHERE entity.NAME LIKE :nameID"
        ),
        @NamedQuery(
                name="getSafeFiles",
                query= "SELECT CONCAT(FILENAME, ',', FILE_ID, ',', DATE) AS RESULT FROM FileEntity entity  WHERE entity.NAME LIKE :nameID and entity.ENCRYPTIONKEY IS NOT NULL "
        ),
        @NamedQuery(
                name="getNoneSafeFiles",
                query= "SELECT CONCAT(FILENAME, ',', FILE_ID, ',', DATE) AS RESULT FROM FileEntity entity  WHERE entity.NAME LIKE :nameID and entity.ENCRYPTIONKEY IS NULL"
        ),
        @NamedQuery(
                name="removeFile",
                query= "DELETE FROM FileEntity entity WHERE entity.UUID LIKE :providedUUID"
        )

})


@Entity
@Table(name = "fileholder")
public class FileEntity {

    private String NAME;
    private String ENCRYPTIONKEY;
    private String FILENAME;
    @GeneratedValue(generator = "increment")
    private int FILE_ID;
    @Id
    private String UUID;
    private Date DATE;

    public FileEntity() {

    }

    public FileEntity(String name, String filename, String uuid, Date date){
        this.NAME = name;
        this.FILENAME = filename;
        this.UUID = uuid;
        this.DATE = date;
    }


    public void setFILENAME(String FILENAME) {
        this.FILENAME = FILENAME;
    }

    public void setENCRYPTIONKEY(String ENCRYPTIONKEY) {
        this.ENCRYPTIONKEY = ENCRYPTIONKEY;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }
}
