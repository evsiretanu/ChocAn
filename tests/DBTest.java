import com.psu.group9.*;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.util.Vector;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DBTest {
    Database db = new Database("testDB.db");
    
    @Test
    public void A0000redoDatabase() {
            addPatients("testdata/patients.csv");
            addProviders("testdata/providers.csv");
            addServices("testdata/services.csv");
            addTransactions("testdata/transactions.csv");
    }
    
    // Patient Unit Tests

    @Test
        public void A001addPatientTest() {
            int ID;    
            try {
                Patient newPatient = new Patient(0, "Test name", "4500 NE Sandy Blvd.", "Portland", "OR", "97233", true, true);
                ID = db.addPatient(newPatient);
                assertTrue(ID > 99999999);

            } catch (InputException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }        
        }

    // We add a duplicate here.
    @Test
        public void A002addPatientTest2() {
            int ID;    
            try {
                Patient newPatient = new Patient(0, "Tom Bottini", "24 Dartmouth Dr", "Framingham", "MA", "01701", true, true);
                ID = db.addPatient(newPatient);
                assertTrue(ID < 0);

            } catch (InputException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                fail();
            }        
        }
    
    // We add a *close* match, meaning one that has the same name and address but different city.
    
    @Test
        public void A003addPatientTest3() {
        int ID;
        try {
            Patient newPatient = new Patient(0, "Test name", "4500 NE Sandy Blvd.", "Hillsboro", "OR", "97123", true, true);
            ID = db.addPatient(newPatient);
            assertTrue(ID > 99999999);
        }
        catch (InputException e) {
            e.printStackTrace();
            fail();
        }
    }
    
    // We update Craig Strickland, ID #100000003, to living in 1911 Angie Dr.
    @Test
        public void A004updatePatientTest() {
        Vector<Entity> serviceVec = db.getPatientsByName("Craig Strickland");
        if(!(serviceVec.isEmpty())) {
            int ID = serviceVec.get(0).getIdNumber();
            
            try {
                Patient updatePatient = new Patient(ID, "Craig Strickland", "1911 Angie Dr", "Irvine", "CA", "92614", true, true);
                Boolean updated = db.updatePatient(ID, updatePatient);
                assertTrue(updated);
            }
            catch(InputException e) {
                fail("InputException thrown.");
                e.printStackTrace();
            }
        }
        
        else {
            fail("Patient not found.");
        }
        
    }
    
    // We try to update Michael Bottini to become Tom Bottini, 24 Dartmouth Dr, Framingham, MA, 01701, true, true.
    
    @Test
    public void A005updatePatientTest2() {
        Vector<Entity> serviceVec = db.getPatientsByName("Michael Bottini");
        if(!(serviceVec.isEmpty())) {
            int ID = serviceVec.get(0).getIdNumber();
            
            try {
                Patient updatePatient = new Patient(ID, "Tom Bottini", "24 Dartmouth Dr", "Framingham", "MA", "01701", true, true);
                Boolean updated = db.updatePatient(ID, updatePatient);
                assertFalse(updated);
            }
            catch(InputException e) {
                fail("InputException thrown.");
                e.printStackTrace();
            }
        }
        
        else {
            fail("Patient not found.");
        }
    }
    
    // We remove Catherine Olmstead and then verify that she was removed.
    @Test
        public void A006removePatientTest(){
            Vector<Entity> serviceVector = db.getPatientsByName("Catherine Olmstead");
            int ID = serviceVector.get(0).getIdNumber();
            
            Boolean removed = db.removePatient(ID);
            assertTrue(removed);
            
            serviceVector = db.getPatientsByName("Catherine Olmstead");
            assertFalse(serviceVector.get(0).getStatus());
        }
    
    // We attempt to remove an ID that is not in the database.
    @Test
        public void A008removePatientTest2() {
            Boolean status = db.removePatient(891279199);
            assertFalse(status);
    }
    
    // We suspend Gloria Lofton and then reinstate her.
    @Test
        public void A009suspendPatientTest() {
            int ID;
            Vector<Entity> patientVector = db.getPatientsByName("Gloria Lofton");
            ID = patientVector.get(0).getIdNumber();
            Boolean suspended = db.suspendPatient(ID);
            assertTrue("Suspended", suspended);
            
            Vector<Entity> vec = db.getPatientsByName("Gloria Lofton");
            ID = vec.get(0).getIdNumber();
            Boolean reinstated = db.reinstatePatient(ID);
            assertTrue("Reinstated", reinstated);
        }
    
    // We suspend a nonexistent ID.
    @Test
        public void A010suspendPatientTest2() {
            Boolean suspended = db.suspendPatient(189012980);
            assertFalse(suspended);
        }
    
    // We reinstate a nonexistent ID.
    @Test
        public void A012reinstatePatientTest() {
            int ID = 108121241;
            Boolean reinstated = db.reinstatePatient(ID);
            assertFalse(reinstated);
        }
    
    
    // Provider Unit Tests

    @Test
        public void B001addProviderTest() {
            int ID;    
            try {
                Provider newProvider = new Provider(0, "Test Provider", "24 Dartmouth Dr", "Framingham", "MA", "01701", true);
                ID = db.addProvider(newProvider);
                assertTrue(ID > 99999999);

            } catch (InputException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                fail();
            }        
        }

    // We add a duplicate here.
    @Test
        public void B002addProviderTest2() {
            int ID;    
            try {
                Provider newProvider = new Provider(0, "Overlab", "28 George St", "Muncie", "IN", "47302", true);
                ID = db.addProvider(newProvider);
                assertTrue(ID < 0);

            } catch (InputException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                fail();
            }        
        }
    
    // We add a *close* match, meaning one that has the same name and address but different city.
    
    @Test
        public void B003addProviderTest3() {
        int ID;
        try {
            Provider newProvider = new Provider(0, "Test Provider", "24 Dartmouth Dr", "Southborough", "MA", "01772", true);
            ID = db.addProvider(newProvider);
            assertTrue(ID > 99999999);
        }
        catch (InputException e) {
            e.printStackTrace();
            fail();
        }
    }
    
    // We update Ganztam, setting it equal to "53 Sunnyslope St", "Springfield", "NY", "13333", true.
    @Test
        public void B004updateProviderTest() {
        Vector<Entity> providerVec = db.getProvidersByName("Ganztam");
        if(!(providerVec.isEmpty())) {
            int ID = providerVec.get(0).getIdNumber();
            
            try {
                Provider updateProvider = new Provider(0, "Ganztam", "53 Sunnyslope St", "Springfield", "NY", "13333", true);
                Boolean updated = db.updateProvider(ID, updateProvider);
                assertTrue(updated);
            }
            catch(InputException e) {
                fail("InputException thrown.");
                e.printStackTrace();
            }
        }
        
        else {
            fail("Patient not found.");
        }
        
    }
    
    // We update Quoelectrics, trying to set it equal to "Zoodexon", "868 Grove Dr", "Michigan City", "IN", "46360", true.
    // This should be a duplicate.
    
    @Test
    public void B005updateProviderTest2() {
        Vector<Entity> ProviderVec = db.getProvidersByName("Quoelectrics");
        if(!(ProviderVec.isEmpty())) {
            int ID = ProviderVec.get(0).getIdNumber();
            
            try {
                Provider updateProvider = new Provider(0, "Zoodexon", "868 Grove Dr", "Michigan City", "IN", "46360", true);
                Boolean updated = db.updateProvider(ID, updateProvider);
                assertFalse(updated);
            }
            catch(InputException e) {
                fail("InputException thrown.");
                e.printStackTrace();
            }
        }
        
        else {
            fail("Provider not found.");
        }
    }
    
    // We set Blackcore to Inactive and then verify that it is inactive.
    @Test
        public void B006removeProviderTest(){
            Vector<Entity> ProviderVector = db.getProvidersByName("Blackcore");
            int ID = ProviderVector.get(0).getIdNumber();
            
            Boolean removed = db.removeProvider(ID);
            assertTrue("removed Boolean is true", removed);
            
            ProviderVector = db.getProvidersByName("Blackcore");
            assertFalse("Active flag on object is false", ProviderVector.get(0).getStatus());
        }
    
    
    // We attempt to remove an ID that is not in the database.
    @Test
        public void B008removeProviderTest2() {
            Boolean status = db.removeProvider(891279199);
            assertFalse(status);
    }
    
    // Service Unit Tests
    
    // Adding a service.
    @Test
    public void C001addServiceTest() {
        int ID;    
        try {
            Service newService = new Service("Test Service", 45.00F);
            ID = db.addService(newService);
            assertTrue(ID > 99999);

        } catch (InputException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            fail();
        }        
    }

    // We add a duplicate here.
    @Test
    public void C002addServiceTest2() {
        int ID;    
        try {
            Service newService = new Service("Deep Massage", 150.00F);
            ID = db.addService(newService);
            assertTrue(ID < 0);

        } catch (InputException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            fail();
        }            
    }

    // We add a *close* match, meaning one that has the same name but different fee.
    
    @Test
    public void C003addServiceTest3() {
    int ID;
    try {
        Service newService = new Service("Test Service", 54.00F);
        ID = db.addService(newService);
        assertTrue(ID > 99999);
    }
    catch (InputException e) {
        e.printStackTrace();
        fail();
    }
}

    // We update the Hypnosis, changing its price to 75.00.
    @Test
    public void C004updateServiceTest() {
    Vector<Service> serviceVec = db.getServicesByName("Hypnosis");
    if(!(serviceVec.isEmpty())) {
        int ID = serviceVec.get(0).getCode();
        
        try {
            Service updateService = new Service("Hypnosis", 75.00F);
            Boolean updated = db.updateService(ID, updateService);
            assertTrue(updated);
        }
        catch(InputException e) {
            fail("InputException thrown.");
            e.printStackTrace();
        }
    }
    
    else {
        fail("Service not found.");
    }
    
}

    // We update Counseling to Acupuncture, 25.00F. This should be rejected, as it's an exact duplicate.
    
    @Test
    public void C005updateServiceTest2() {
        Vector<Service> serviceVec = db.getServicesByName("Counseling");
        if(!(serviceVec.isEmpty())) {
            int ID = serviceVec.get(0).getCode();
            
            try {
                Service updateService = new Service("Acupuncture", 25.00F);
                Boolean updated = db.updateService(ID, updateService);
                assertFalse(updated);
            }
            catch(InputException e) {
                fail("InputException thrown.");
                e.printStackTrace();
            }
        }
        
        else {
            fail("Service not found.");
        }
    }

    // We set Heated Acupuncture to inactive.
    // We then verify that it was removed.
    @Test
    public void C006removeServiceTest(){
        Vector<Service> serviceVector = db.getServicesByName("Heated Acupuncture");
        int ID = serviceVector.get(0).getCode();
        
        Boolean removed = db.removeService(ID);
        assertTrue(removed);
        
        serviceVector = db.getServicesByName("Heated Acupuncture");
        assertFalse(serviceVector.get(0).getStatus());
    }

    // We attempt to remove an ID that is not in the database.
    @Test
        public void C008removeServiceTest2() {
            Boolean status = db.removeService(891270);
            assertFalse(status);
    }
    
    // Transaction Unit Tests
    
    // Adding a Transaction.
    
    @Test
    public void D001addTransactionTest() {
        int ID;    
        try {
            Transaction newTransaction = new Transaction(100000000, 100000000, 100000, 100000000, "11-01-2016", "Stuff");
            ID = db.addTransaction(newTransaction);
            assertTrue(ID > 99999999);

        } catch (InputException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            fail();
        }        
    }

    // Note that duplicates *CAN* be added, so we don't check for those.

    // We add a transaction with an inactive PatientID.
    @Test
    public void D002addTransactionTest2() {
        try {
            Patient newPatient = new Patient(0, "Inactive Loser", "123 Aleph St", "Hillsboro", "OR", "97123", true, true);
            int ID = db.addPatient(newPatient);
            db.removePatient(ID);
            Transaction newTransaction = new Transaction(ID, 100000000, 100000, 100000000, "11-01-2016", "Stuff");
            ID = db.addTransaction(newTransaction);
            assertTrue(ID < 0);
        }
        catch(InputException e) {
            e.printStackTrace();
            fail();
        }
    }
    
    // We add a transaction with a nonexistent PatientID.
    @Test
    public void D003addTransactionTest3() {
        int ID;
        try {
            Transaction newTransaction = new Transaction(100198211, 100000000, 100000, 100000007, "11-01-2016", "Stuff");
            ID = db.addTransaction(newTransaction);
            assertTrue(ID < 0);
        }
        catch(InputException e) {
            e.printStackTrace();
            fail();
        }
    }
    
    // We add a transaction with an inactive ProviderID.
    @Test
    public void D004addTransactionTest4() {
        try {
            Provider newProvider = new Provider(0, "Inactive Loser 2", "123 Aleph St", "Hillsboro", "OR", "97123", true);
            int ID = db.addProvider(newProvider);
            db.removeProvider(ID);
            Transaction newTransaction = new Transaction(100000000, ID, 100000, 100000008, "11-01-2016", "Stuff");
            ID = db.addTransaction(newTransaction);
            assertTrue(ID < 0);
        }
        catch(InputException e) {
            e.printStackTrace();
            fail();
        }
    }
    
    // We add a transaction with a nonexistent ProviderID.
    @Test
    public void D005addTransactionTest5() {
        int ID;
        try {
            Transaction newTransaction = new Transaction(100000000, 189129301, 100000, 100000000, "11-01-2016", "Stuff");
            ID = db.addTransaction(newTransaction);
            assertTrue(ID < 0);
        }
        catch(InputException e) {
            e.printStackTrace();
        }
    }
    
    // We add a transaction with an inactive ServiceID.
    @Test
    public void D006addTransactionTest6() {
        try {
            Service newService = new Service("Inactive Servive", 55.00F);
            int ID = db.addService(newService);
            db.removeService(ID);
            Transaction newTransaction = new Transaction(100000000, 100000000, ID, 100000000, "11-01-2016", "Stuff");
            ID = db.addTransaction(newTransaction);
            assertTrue(ID < 0);
        }
        catch(InputException e) {
            e.printStackTrace();
            fail();
        }
    }
    
    // We add a transaction with a nonexistent ServiceID.
    @Test
    public void D007addTransactionTest7() {
        int ID;
        try {
            Transaction newTransaction = new Transaction(100000000, 100000000, 198237, 100000000, "11-01-2016", "Stuff");
            ID = db.addTransaction(newTransaction);
            assertTrue(ID < 0);
        }
        catch(InputException e) {
            e.printStackTrace();
            fail();
        }
    }
    
    /*
    // We update the first service, setting it equal to "Test Service2", 94.00F.
    @Test
    public void C004updateServiceTest() {
    Vector<Service> serviceVec = db.getServicesByName("Test Service");
    if(!(serviceVec.isEmpty())) {
        int ID = serviceVec.get(0).getIdNumber();
        
        try {
            Service updateService = new Service("Test Service2", 94.00F);
            Boolean updated = db.updateService(ID, updateService);
            assertTrue(updated);
        }
        catch(InputException e) {
            fail("InputException thrown.");
            e.printStackTrace();
        }
    }
    
    else {
        fail("Service not found.");
    }
    
}

    // We update that same service, setting it equal to "Test Service", 54.00.
    // Note that this is an exact duplicate of the service added in C003addServiceTest3, so the database should refuse it.
    
    @Test
    public void C005updateServiceTest2() {
        Vector<Service> serviceVec = db.getServicesByName("Test Service2");
        if(!(serviceVec.isEmpty())) {
            int ID = serviceVec.get(0).getIdNumber();
            
            try {
                Service updateService = new Service("Test Service", 54.00F);
                Boolean updated = db.updateService(ID, updateService);
                assertFalse(updated);
            }
            catch(InputException e) {
                fail("InputException thrown.");
                e.printStackTrace();
            }
        }
        
        else {
            fail("Service not found.");
        }
    }

    // We set that service to Inactive.
    @Test
    public void C006removeServiceTest(){
        Vector<Service> serviceVector = db.getServicesByName("Test Service");
        int ID = serviceVector.get(0).getIdNumber();
        
        Boolean removed = db.removeService(ID);
        assertTrue(removed);
    }

    // We get the service that was removed and verify that it was actually set to Inactive.
    @Test
        public void C007removeServiceVerify() {
            Vector<Service> serviceVector = db.getServicesByName("Test Service");
            assertFalse(serviceVector.get(0).getStatus());
    }

    // We attempt to remove an ID that is not in the database.
    @Test
        public void C008removeServiceTest2() {
            Boolean status = db.removeService(891270);
            assertFalse(status);
    }
    
    // I don't think that we need this test, but I'm keeping it just in case. We'll comment it out in the meantime.
    /*
    @Test 
        public void C005removeRow(){
            try {
                Class.forName("org.sqlite.JDBC");
                conn = DriverManager.getConnection("jdbc:sqlite:database.db");
                PreparedStatement stmt = conn.prepareStatement("DELETE FROM Services WHERE ServiceID=?");
                stmt.setInt(1, serviceID);
                stmt.executeUpdate();

                stmt = conn.prepareStatement("DELETE FROM Services WHERE ServiceID=?");
                stmt.setInt(1, serviceID);
                stmt.executeUpdate();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    */
    
    @BeforeClass
    public static void removeDB() {
        // Delete the database if it doesn't already exist.
        Path dbPath = Paths.get("testDB.db");
        try {
            Files.deleteIfExists(dbPath);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    public void addPatients(String filename) {
        String line;
        Patient currentPatient;
        int currentPatientID;
        int lineNumber = 1;

        // Fatal exception try.
            try {
                BufferedReader reader = new BufferedReader(
                new FileReader(filename));

                while((line = reader.readLine()) != null) {
                    String [] splitLine = line.split(",");
                    // Individual line exception try.
                    try {
                        currentPatient = new Patient(
                                0,
                                splitLine[0], // Name
                                splitLine[1], // Address
                                splitLine[2], // City
                                splitLine[3], // State
                                splitLine[4], // Zipcode
                                true,            // Enrollment Status
                                true            // Financial Standing
                                );
                        currentPatientID = db.addPatient(currentPatient);
                    }
                    
                    catch(InputException e) {
                        System.out.println("Error for " + splitLine[0] + ": " +
                                e.getMessage());
                    }
                    catch(ArrayIndexOutOfBoundsException e) {
                        System.out.println("ArrayIndexOutOfBounds exception on line " +
                                Integer.toString(lineNumber));
                    }
                    catch(NumberFormatException e) {
                        System.out.println("NumberFormatException on line " +
                                Integer.toString(lineNumber));
                    }


                    lineNumber++;
                }
                reader.close();
            }
            catch(IOException e) {
                System.out.println(e.getMessage());
                return;
            }

            return;
    }

    public void addProviders(String filename) {
        String line;
        Provider currentProvider;
        int currentProviderID;
        int lineNumber = 1;

        // Fatal exception try.
        try {
            BufferedReader reader = new BufferedReader(
                    new FileReader(filename));

            while((line = reader.readLine()) != null) {
                String [] splitLine = line.split(",");
                // Individual line exception try.
                try {
                    currentProvider = new Provider(
                            0,
                            splitLine[0], // Name
                            splitLine[1], // Address
                            splitLine[2], // City
                            splitLine[3], // State
                            splitLine[4], // Zipcode
                            true             // Enrollment Status
                    );
                    currentProviderID = db.addProvider(currentProvider);
                }
                catch(InputException e) {
                    System.out.println("Error for " + splitLine[0] + ": " +
                            e.getMessage());
                }
                catch(ArrayIndexOutOfBoundsException e) {
                    System.out.println("ArrayIndexOutOfBounds exception on line " +
                            Integer.toString(lineNumber));
                }
                catch(NumberFormatException e) {
                    System.out.println("NumberFormatException on line " +
                            Integer.toString(lineNumber));
                }


                lineNumber++;
            }

            reader.close();

        }
        catch(IOException e) {
            System.out.println(e.getMessage());
            return;
        }
        return;
    }

    public void addServices(String filename) {
        String line;
        Service currentService;
        int currentServiceID;
        int lineNumber = 1;

        // Fatal exception try.
        try {
           BufferedReader reader = new BufferedReader(
                   new FileReader(filename));

           while((line = reader.readLine()) != null) {
               String [] splitLine = line.split(",");
               // Individual line exception try.
               try {
                   currentService = new Service(
                           splitLine[0],                  // Name
                           Float.parseFloat(splitLine[1]) // Price
                           );
                   currentServiceID = db.addService(currentService);
               }
               catch(InputException e) {
                   System.out.println("Error for " + splitLine[0] + ": " +
                           e.getMessage());
               }
               catch(ArrayIndexOutOfBoundsException e) {
                   System.out.println("ArrayIndexOutOfBounds exception on line " +
                           Integer.toString(lineNumber));
               }
               catch(NumberFormatException e) {
                   System.out.println("NumberFormatException on line " +
                           Integer.toString(lineNumber));
               }


               lineNumber++;
           }

           reader.close();

        }
        catch(IOException e) {
            System.out.println(e.getMessage());
            return;
        }

        return;
    }
  
    public void addTransactions(String filename) {
        String line;
        Transaction currentTransaction;
        int currentTransactionID;
        int lineNumber = 1;

        // Fatal exception try.
        try {
            BufferedReader reader = new BufferedReader(
                    new FileReader(filename));

            while((line = reader.readLine()) != null) {
                String [] splitLine = line.split(",");
                // Individual line exception try.
                try {
                    currentTransaction = new Transaction(
                            Integer.parseInt(splitLine[1]),    // Provider ID 
                            Integer.parseInt(splitLine[2]),    // Patient ID
                            Integer.parseInt(splitLine[3]),    // Service ID
                            Integer.parseInt(splitLine[4]),    // Consult ID
                            splitLine[0],                      // Service Date
                            splitLine[5]                       // Comment
                            );
                    currentTransactionID = db.addTransaction(currentTransaction);
                }
                catch(InputException e) {
                    System.out.println("Error for " + 
                            splitLine[1] + " -> " + splitLine[2] + ": " +
                                         e.getMessage());
                }
                catch(ArrayIndexOutOfBoundsException e) {
                    System.out.println("ArrayIndexOutOfBounds exception on line " +
                            Integer.toString(lineNumber));
                }
                catch(NumberFormatException e) {
                    System.out.println("NumberFormatException on line " +
                            Integer.toString(lineNumber));
                }

                lineNumber++;
            }
          
            reader.close();
        }
        catch(IOException e) {
            System.out.println(e.getMessage());
            return;
        }

        return;
    }
}
