package fileService.database;

import fileService.FileMetadata;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;
import java.util.List;

public class DatabaseHandler {


    private static EntityManagerFactory emFactory;

    public String storeKeys(FileMetadata fileMetadata){

        FileEntity fileEntity = new FileEntity(fileMetadata.getName(), fileMetadata.getEncryptionkey(), fileMetadata.getFile().getName(), fileMetadata.getUUID());
        EntityManager em = PersistenceManager.INSTANCE.getEntityManager();


        try {
            em.getTransaction()
                    .begin();
            em.persist(fileEntity);
            em.getTransaction()
                    .commit();
            em.flush();
            em.close();

            return "Transaction Complete";
        }
        catch (PersistenceException pe){

            return "JPA ERROR";
        }

    }

    public String retrieveEncryptionkey(String nameID, String fileID){

        EntityManager em = PersistenceManager.INSTANCE.getEntityManager();

        int id = Integer.parseInt(fileID);

        List<String> result = em.createNamedQuery("retrieveEncryptionkey")
                .setParameter("nameID", nameID)
                .setParameter("fileID", id)
                .getResultList();


        if (!result.isEmpty()){

            return result.get(0);
        }
        return "";
    }


    public enum PersistenceManager {
        INSTANCE;
        private PersistenceManager() {

            emFactory = Persistence.createEntityManagerFactory("fileservice");
        }
        public EntityManager getEntityManager() {
            return emFactory.createEntityManager();
        }
        public void close() {
            emFactory.close();
        }

    }

}
