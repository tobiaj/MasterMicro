package fileService.database;

import fileService.FileMetadata;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;

public class DatabaseHandler {


    private static EntityManagerFactory emFactory;

    public String storeFileInformation(FileMetadata fileMetadata){

        long time = System.currentTimeMillis();
        Date date = new Date(time);

        FileEntity fileEntity = new FileEntity(fileMetadata.getName(), fileMetadata.getFile().getName(), fileMetadata.getUUID(), date);
        if(fileMetadata.getEncryptionkey() != null){
            fileEntity.setENCRYPTIONKEY(fileMetadata.getEncryptionkey());
        }
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


    public String getFileInformation(String nameID, String fileID){

        EntityManager em = PersistenceManager.INSTANCE.getEntityManager();

        int id = Integer.parseInt(fileID);

        List<String> result = em.createNamedQuery("getFileInformation")
                .setParameter("nameID", nameID)
                .setParameter("fileID", id)
                .getResultList();

        if (!result.isEmpty()){

            return result.get(0);
        }
        return "";
    }

    public String getFileInformationAndKey(String nameID, String fileID) {
        EntityManager em = PersistenceManager.INSTANCE.getEntityManager();

        int id = Integer.parseInt(fileID);

        List<String> result = em.createNamedQuery("getFileInformationWithKey")
                .setParameter("nameID", nameID)
                .setParameter("fileID", id)
                .getResultList();


        if (!result.isEmpty()){

            return result.get(0);
        }
        return "";
    }

    public List<String> getNoneSafe(String id) {

        EntityManager em = PersistenceManager.INSTANCE.getEntityManager();

        List<String> result = em.createNamedQuery("getNoneSafeFiles")
                .setParameter("nameID", id)
                .getResultList();


        if (!result.isEmpty()){

            return result;
        }
        return null;
    }

    public List<String> getSafeFiles(String id) {

        EntityManager em = PersistenceManager.INSTANCE.getEntityManager();

        List<String> result = em.createNamedQuery("getSafeFiles")
                .setParameter("nameID", id)
                .getResultList();


        if (!result.isEmpty()){

            return result;
        }
        return null;
    }

    public List<String> getFiles(String id) {

        EntityManager em = PersistenceManager.INSTANCE.getEntityManager();

        List<String> result = em.createNamedQuery("getAllFiles")
                .setParameter("nameID", id)
                .getResultList();


        if (!result.isEmpty()){

            return result;
        }
        return null;
    }

    public boolean removeFile(String uuid) {

        EntityManager em = PersistenceManager.INSTANCE.getEntityManager();

        em.getTransaction().begin();
        Query query = em.createQuery("Delete from FileEntity entity where entity.UUID = :uuid");
        query.setParameter("uuid", uuid);
        int rows = query.executeUpdate();
        em.getTransaction().commit();
        if (rows > 0 ){
            return true;
        }

        return false;
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
